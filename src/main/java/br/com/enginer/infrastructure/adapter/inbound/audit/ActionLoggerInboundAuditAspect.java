package br.com.enginer.infrastructure.adapter.inbound.audit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.usecase.core.action.ActionUseCase;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;
import br.com.enginer.infrastructure.utils.NormalizeUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Aspect responsável por auditar ações do endpoint de Action.
 *
 * Registra logs de CREATE/UPDATE/DELETE para ações selecionadas (ex:
 * salvar/excluir), incluindo payload normalizado, requestId, usuário e
 * metadados da requisição.
 */
@Aspect
@Component
public class ActionLoggerInboundAuditAspect {

	private static final Set<String> AUDIT_ACTIONS = Set.of(ActionUseCase.salvar, ActionUseCase.excluir,
			ActionUseCase.salvarLista, ActionUseCase.excluirLista);

	private final ActionLoggerRepositoryOutboundAdapterPort actionLoggerRepositoryOutboundAdapterPort;
	private final RequestContextProvider requestContext; // ip/requestId
	private final UserContextResolver userResolver; // headers X-Username, etc (opcional)
	private final ObjectMapper objectMapper;

	public ActionLoggerInboundAuditAspect(
			ActionLoggerRepositoryOutboundAdapterPort actionLoggerRepositoryOutboundAdapterPort,
			RequestContextProvider requestContext, UserContextResolver userResolver, ObjectMapper objectMapper) {
		this.actionLoggerRepositoryOutboundAdapterPort = actionLoggerRepositoryOutboundAdapterPort;
		this.requestContext = requestContext;
		this.userResolver = userResolver;
		this.objectMapper = objectMapper;
	}

	/**
	 * Intercepta o endpoint Action e persiste auditoria apenas para ações elegíveis
	 * (salvar/excluir e variações em lote).
	 */
	@Around("execution(* br.com.enginer.infrastructure.adapter.inbound.api.ActionInboundAdapterPort.action(..))")
	public Object auditActionEndpoint(ProceedingJoinPoint pjp) throws Throwable {

		long start = System.nanoTime();

		Object result = null;
		Throwable error = null;

		Object[] args = pjp.getArgs();
		Domain<?> domain = (args != null && args.length > 0 && args[0] instanceof Domain<?> d) ? d : null;
		JsonNode jsonNode = (args != null && args.length > 1 && args[1] instanceof JsonNode j) ? j : null;

		ActionLogger actionLogger = extractFrontLogger(jsonNode);

		String domainName = (actionLogger != null && actionLogger.getDomain() != null
				&& !actionLogger.getDomain().isBlank()) ? actionLogger.getDomain()
						: (domain != null ? domain.getClass().getSimpleName() : null);

		String action = actionLogger != null ? actionLogger.getAction() : null;
		String domainId = actionLogger != null ? actionLogger.getDomainId() : null;

		if (!shouldAudit(action)) {
			return pjp.proceed();
		}

		HttpServletRequest request = requestContext.currentRequest().orElse(null);

		String userAgent = request != null ? request.getHeader("User-Agent") : null;

		String userId = request != null ? userResolver.userId(request).orElse(null) : null;

		String source = requestContext.source(request).orElse("WEB");
		String ip = requestContext.ipAddress(request);
		String url = requestContext.uiUrl(request);
		Boolean isModal = requestContext.isModal(request);
		Boolean isDisabled = requestContext.isDisabled(request);
		UUID requestId = requestContext.requestId(request);

		List<ObjectNode> jsonNodes = new ArrayList<>();
		JsonNode normalizedNode = NormalizeUtils.normalizer(jsonNode);

		if (jsonNode != null && jsonNode.has("data") && jsonNode.get("data").isArray()) {

			ArrayNode dataArray = (ArrayNode) jsonNode.get("data");

			for (JsonNode itemNode : dataArray) {
				
				normalizedNode = NormalizeUtils.normalizer(itemNode);

				ObjectNode origem = (normalizedNode != null && normalizedNode.isObject()
						? ((ObjectNode) normalizedNode).deepCopy()
						: null);

				if (origem != null) {
					origem.remove("actionLogger");
					jsonNodes.add(origem);
				}
			}

		} else {

			ObjectNode origem = (normalizedNode != null && normalizedNode.isObject()
					? ((ObjectNode) normalizedNode).deepCopy()
					: null);

			if (origem != null) {
				origem.remove("actionLogger");
				jsonNodes.add(origem);
			}
		}

		try {

			result = pjp.proceed();
			return result;

		} catch (Throwable t) {

			error = t;
			throw t;

		} finally {

			try {

				List<String> resultIds = extractIdsFromResponse(result);

				for (int i = 0; i < jsonNodes.size(); i++) {

					ObjectNode objectNode = jsonNodes.get(i);

					boolean success = (error == null);

					ActionLogger logger = new ActionLogger();

					if (domainId == null && objectNode != null && !objectNode.isNull()) {
						domainId = objectNode.get("id") != null ? String.valueOf(objectNode.get("id")) : null;
					}

					String actionType = resolveActionType(domainId, actionLogger);

					String resultId = getResultIdByIndex(resultIds, i);

					if (StringsUtils.isNullOrBlank(domainId)) {
						domainId = resultId;
					}

					if (domainId != null) {

						logger.setDomain(domainName != null ? StringsUtils.firstLower(domainName) : null);
						logger.setAction(action);
						logger.setModal(isModal);
						logger.setUrl(url);
						logger.setDisabled(isDisabled);
						logger.setIdUserAccount(Long.valueOf(userId));
						logger.setSource(source);
						logger.setIpAddress(ip);
						logger.setUserAgent(userAgent);
						logger.setRequestId(requestId);
						logger.setDurationMs(System.nanoTime() - start);
						logger.setSuccess(success);
						logger.setErrorMessage(success ? null : safeErrorMessage(error));
						logger.setDomainId(domainId);
						logger.setType(actionType);

						logger.setNewValue(objectNode); // payload original do front (JSONB)

						actionLoggerRepositoryOutboundAdapterPort.save(logger);
					}

					domainId = null;
				}

			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Extrai o ActionLogger enviado pelo frontend (actionLogger) a partir do JSON.
	 */
	private ActionLogger extractFrontLogger(JsonNode json) {
		try {
			if (json != null && json.has("actionLogger") && !json.get("actionLogger").isNull()) {
				return objectMapper.convertValue(json.get("actionLogger"), ActionLogger.class);
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	/**
	 * Determina o tipo da operação (CREATE/UPDATE/DELETE) com base na action e na
	 * presença do domainId.
	 */
	private String resolveActionType(String domainId, ActionLogger actionLogger) {

		if (actionLogger == null || actionLogger.getAction() == null) {
			return "UNKNOWN";
		}

		String action = actionLogger.getAction().toLowerCase();

		if (action.contains("excluir")) {
			return "DELETE";
		}

		if (action.contains("salvar")) {
			return StringsUtils.isNullOrBlank(domainId) ? "CREATE" : "UPDATE";
		}

		return "UNKNOWN";
	}

	/**
	 * Extrai IDs do retorno do controller, suportando ResponseEntity, Domain único
	 * ou lista de Domain.
	 */
	private List<String> extractIdsFromResponse(Object result) {

		try {
			if (result instanceof ResponseEntity<?> re) {
				result = re.getBody();
			}

			if (result == null)
				return List.of();

			if (result instanceof Domain<?> d) {
				String id = extractId(d);
				return (id != null) ? List.of(id) : List.of();
			}

			if (result instanceof Iterable<?> iterable) {
				List<String> ids = new ArrayList<>();
				for (Object obj : iterable) {
					if (obj instanceof Domain<?> d) {
						String id = extractId(d);
						if (id != null)
							ids.add(id);
					}
				}
				return ids;
			}

		} catch (Exception ignored) {
		}

		return List.of();
	}

	/**
	 * Extrai o ID de um Domain se ele estiver preenchido.
	 */
	private String extractId(Domain<?> d) {
		try {
			if (d != null && !d.isIdNull()) {
				return String.valueOf(d.getId());
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	/**
	 * Retorna o ID correspondente ao índice do payload quando a resposta é uma
	 * lista.
	 */
	private String getResultIdByIndex(List<String> ids, int index) {
		if (ids == null || ids.isEmpty())
			return null;
		if (index < 0 || index >= ids.size())
			return null;
		return ids.get(index);
	}

	/**
	 * Normaliza a mensagem de erro e limita o tamanho para persistência.
	 */
	private String safeErrorMessage(Throwable t) {
		String msg = t != null ? t.getMessage() : null;
		if (msg == null || msg.isBlank())
			msg = (t != null ? t.getClass().getName() : "UNKNOWN");
		return msg.length() > 2000 ? msg.substring(0, 2000) : msg;
	}

	/**
	 * Define quais actions devem gerar auditoria.
	 */
	private boolean shouldAudit(String action) {
		return action != null && AUDIT_ACTIONS.contains(action);
	}
}