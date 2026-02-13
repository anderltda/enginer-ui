package br.com.enginer.infrastructure.adapter.inbound.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.dto.entity.tag.SearchOverlay;
import br.com.enginer.domain.system.dto.entity.tag.TagType;
import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UIDomain;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.fn.SerializableConsumer;
import br.com.enginer.domain.system.usecase.core.fn.SerializableLambda;
import br.com.enginer.domain.system.usecase.core.fn.SerializableRunnable;
import br.com.enginer.domain.system.usecase.core.fn.SerializableTriConsumer;
import br.com.enginer.domain.system.usecase.core.page.PageResult;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.port.inbound.api.ActionInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.upload.UploadFileUseCase;
import br.com.enginer.domain.system.usecase.user.UserAccountUseCase;
import br.com.enginer.domain.system.usecase.user.vo.ProviderIdentityVo;
import br.com.enginer.infrastructure.configuration.security.ProviderIdentity;
import br.com.enginer.infrastructure.utils.NormalizeUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * ActionInboundAdapterPort
 * -----------------------------------------------------------------------------
 * Controller REST “genérico” do Enginer UI.
 *
 * Responsabilidades:
 * 1) Receber requisições do frontend (UI).
 * 2) Resolver um {@link Domain} pelo parâmetro {@link UIDomain} (via resolver do Spring).
 * 3) Delegar a execução ao {@link ActionInboundPort}, que encaminha para o UseCase correto.
 *
 * Observações importantes:
 * - O Domain recebido via {@link UIDomain} já vem “enriquecido” com informações de contexto
 *   (principalmente {@link ActionLogger} e o {@link UserAccount} resolvido no backend).
 * - Quando o controller cria um Domain manualmente via ObjectMapper (convertValue),
 *   esse novo objeto NÃO passa pelo resolver. Por isso existe o propagateContext(...),
 *   garantindo ActionLogger/UserAccount coerentes e evitando erros de JPA (ex.: createdBy null/transient).
 */
@RestController
@RequestMapping("/v1/enginer-ui/action")
public class ActionInboundAdapterPort {

	/**
	 * Porta inbound da camada de aplicação.
	 * Centraliza chamadas como:
	 * - searchByConditions / searchPaginated / methodName(...)
	 */
	private final ActionInboundPort<?> actionInboundPort;

	/** Mapper JSON usado para normalização e conversão JsonNode -> Domain. */
	private final ObjectMapper objectMapper;

	/** Logger de aplicação via porta outbound (Clean Architecture). */
	private final LoggerOutboundPort logger;

	/**
	 * UseCases instanciados manualmente para extrair o nome do método via lambda serializável
	 * e repassar para actionInboundPort.methodName(...).
	 */
	private final UserAccountUseCase userAccountUseCase;
	private final UploadFileUseCase uploadFileUseCase;

	public ActionInboundAdapterPort(ActionInboundPort<?> actionInboundPort, ObjectMapper objectMapper, LoggerOutboundPort logger) {
		this.actionInboundPort = actionInboundPort;
		this.objectMapper = objectMapper;
		this.logger = logger;
		// UseCases “puros” (sem dependências injetadas) usados só para obter o nome de métodos.
		this.userAccountUseCase = new UserAccountUseCase();
		this.uploadFileUseCase = new UploadFileUseCase();
	}

	// ============================================================================================
	// ME (AUTH)
	// ============================================================================================

	/**
	 * Retorna o usuário autenticado (ou cria o usuário se não existir).
	 *
	 * Fluxo:
	 * 1) Extrai identidade do JWT (Keycloak) via ProviderIdentity.
	 * 2) Monta ProviderIdentityVo com dados básicos + metadados de login (ip/agent/instante).
	 * 3) Executa UseCase login(...) via actionInboundPort.methodName.
	 * 4) Se não existir usuário, executa onboard(...) para criar.
	 *
	 * Retornos:
	 * - 200 OK com UserAccount se autenticado/registrado.
	 * - 401 se não conseguiu autenticar/registrar.
	 */
	@GetMapping("/me")
	public ResponseEntity<UserAccount> me(@AuthenticationPrincipal Jwt jwt, @RequestHeader(value = "X-Tenant", required = false) String tenantHeader, HttpServletRequest request) throws CheckedException {

		try {
			
			// Converte o JWT em um objeto de identidade padronizado do Enginer.
			ProviderIdentity identity = ProviderIdentity.fromJwt("KEYCLOAK", jwt);

			// Metadados do “último login” (úteis para persistir no user_account).
			String ip = resolveClientIp(request);
			String userAgent = request.getHeader("User-Agent");
			Instant lastLoginAt = Instant.now();

			// VO que trafega para o UseCase login/onboard.
			ProviderIdentityVo providerIdentity = new ProviderIdentityVo(
					identity.provider(),
					identity.providerSubject(),
					identity.providerTenant(),
					identity.email(),
					identity.username(),
					identity.name(),
					lastLoginAt,
					ip,
					userAgent
			);

			// Pega o nome do método em tempo de compilação (evita string “solta”).
			String login = SerializableLambda.extractMethodName((SerializableConsumer<ProviderIdentityVo>) userAccountUseCase::login);

			// Executa o UseCase “por nome” no actionInboundPort.
			UserAccount userAccount = (UserAccount) actionInboundPort.methodName(new UserAccount(), login, providerIdentity);

			// Se ainda não existir (primeiro acesso), faz onboarding.
			if (userAccount == null) {
				String onboard = SerializableLambda.extractMethodName((SerializableConsumer<ProviderIdentityVo>) userAccountUseCase::onboard);
				userAccount = (UserAccount) actionInboundPort.methodName(new UserAccount(), onboard, providerIdentity);
			}

			if (userAccount == null) {
				return ResponseEntity.status(401).build();
			}

			return ResponseEntity.ok(userAccount);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	// ============================================================================================
	// CALENDAR
	// ============================================================================================

	/**
	 * Retorna eventos do calendário para o usuário logado.
	 *
	 * Ponto-chave:
	 * - domain.getActionLogger().getUserAccount() já existe porque o Domain é resolvido via @UIDomain.
	 *
	 * Obs:
	 * - Os parâmetros year/month/day não estão sendo usados na janela (start/end fixos).
	 *   Mantém compatibilidade, mas se quiser filtrar por mês/dia dá pra ajustar.
	 */
	@GetMapping("/calendar")
	public ResponseEntity<List<Domain<?>>> calendar(@UIDomain Domain<?> domain, @RequestParam String year, @RequestParam String month, @RequestParam String day) {
		
		try {
		
			logger.info(ActionInboundAdapterPort.class, "Executando calendar: " + domain);

			// Validação básica.
			if (year == null || year.trim().isEmpty() || month == null || month.trim().isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			// Janela “hardcoded” (pode ser refinada usando year/month/day).
			LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
			LocalDateTime endDateTime = LocalDateTime.of(2035, 12, 31, 23, 59, 59);

			// Monta filtro dinâmico.
			Map<String, Object> filters = new HashMap<>();
			filters.put("startDateTime", startDateTime);
			filters.put("startDateTime_op", Constants.MAIOR_OU_IGUAL);
			filters.put("endDateTime", endDateTime);
			filters.put("endDateTime_op", Constants.MENOR_OU_IGUAL);

			// userAccount já vem no domain (DomainResolver) -> filtra por usuário logado.
			filters.put("userAccount.id", domain.getActionLogger().getUserAccount().getId());

			List<Domain<?>> events = actionInboundPort.searchByConditions(domain, filters);
			
			return ResponseEntity.ok(events);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	// ============================================================================================
	// SEARCH FOR TAGGING
	// ============================================================================================

	/**
	 * Busca tags para “overlay”/autocomplete de tagging.
	 *
	 * Entrada:
	 * - params pode vir separado por ';' ou por espaços.
	 *
	 * Processo:
	 * 1) Quebra params em termos.
	 * 2) Normaliza cada termo para alphaNumeric (ReflectionUtils.normalizeAlphaNumeric).
	 * 3) Faz busca IN por normalizedName.
	 * 4) Busca em dois tipos: GLOBAL e SYSTEM.
	 */
	@GetMapping("/search")
	public ResponseEntity<SearchOverlay> search(@UIDomain Domain<?> domain, @RequestParam(required = false) String params) {
		
		try {
			
			logger.info(ActionInboundAdapterPort.class, "Executando search: " + domain);

			if (params == null || params.trim().isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			// Se veio “;” usa como separador, senão quebra por espaços.
			boolean hasComma = params.contains(";");

			List<String> parts = hasComma
					? Arrays.stream(params.split(";")).map(String::trim).filter(s -> !s.isBlank()).toList()
					: Arrays.stream(params.trim().split("\\s+")).map(String::trim).filter(s -> !s.isBlank()).toList();

			// Normaliza + remove duplicados + junta em CSV para uso no filtro IN.
			String result = parts.stream()
					.map(ReflectionUtils::normalizeAlphaNumeric)
					.distinct()
					.collect(Collectors.joining(","));

			Map<String, Object> filters = new HashMap<>();
			filters.put("id.normalizedName", result);
			filters.put("id.normalizedName_op", Constants.IN);

			// Busca tags globais
			filters.put("type", TagType.GLOBAL);
			List<Domain<?>> globais = actionInboundPort.searchByConditions(domain, filters);

			// Busca tags do sistema
			filters.put("type", TagType.SYSTEM);
			List<Domain<?>> system = actionInboundPort.searchByConditions(domain, filters);

			return ResponseEntity.ok(new SearchOverlay(globais, system));

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	// ============================================================================================
	// CHUNKED UPLOAD
	// ============================================================================================

	/**
	 * Inicia uma sessão de upload “chunked”.
	 * Retorna uploadId para o frontend mandar os chunks.
	 */
	@PostMapping("/session")
	public ResponseEntity<Map<String, String>> startSession() throws CheckedException {
		
		String startSession = SerializableLambda.extractMethodName((SerializableRunnable) uploadFileUseCase::startSession);
		
		String uploadId = (String) actionInboundPort.methodName(new UploadFile(), startSession);

		return ResponseEntity.ok(Map.of("uploadId", Objects.toString(uploadId, "")));
	}

	/**
	 * Recebe um chunk (parte) do arquivo.
	 *
	 * Parâmetros:
	 * - uploadId: sessão
	 * - chunkIndex: índice da parte (ordem)
	 * - file: Multipart com os bytes do chunk
	 */
	@PostMapping("/chunk")
	public ResponseEntity<Void> uploadChunk(@RequestParam String uploadId, @RequestParam int chunkIndex, @RequestParam("file") MultipartFile part) throws Exception {

		String uploadChunk = SerializableLambda.extractMethodName((SerializableTriConsumer<String, Integer, InputStream>) uploadFileUseCase::uploadChunk);

		actionInboundPort.methodName(new UploadFile(), uploadChunk, uploadId, chunkIndex, part.getInputStream());
		
		return ResponseEntity.ok().build();
	}

	/**
	 * Finaliza o upload.
	 *
	 * - Recebe metadados (jsonMeta) + uploadId (uid).
	 * - Converte meta -> UploadFile e chama actionInboundPort.methodName(uploadFile) para persistir.
	 */
	@PostMapping("/finalize")
	public ResponseEntity<UploadFile> finalizeUpload(@RequestParam String uploadId, @RequestPart("meta") String jsonMeta) throws Exception {
		
		UploadFile uploadFile = objectMapper.readValue(jsonMeta, UploadFile.class);
		
		uploadFile.setUid(uploadId);

		uploadFile = (UploadFile) actionInboundPort.methodName(uploadFile);
		
		return ResponseEntity.ok(uploadFile);
	}

	// ============================================================================================
	// DOWNLOAD
	// ============================================================================================

	/**
	 * Download de arquivo por filtros (search por condições simples).
	 *
	 * - filter: mapa de parâmetros (ex.: id=..., uid=..., etc.)
	 * - inline: se true, abre no navegador; se false, força download.
	 *
	 * Observação:
	 * - O método lê diretamente do filesystem via Files.readAllBytes(Path).
	 *   Para arquivos grandes, pode ser melhor stream (InputStreamResource).
	 */
	@GetMapping("/download")
	public ResponseEntity<byte[]> download(@UIDomain Domain<?> domain, @RequestParam Map<String, Object> filter, @RequestParam(defaultValue = "true") boolean inline) throws IOException {

		if (filter != null && filter.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		UploadFile file = (UploadFile) actionInboundPort.searchWithBySingleConditions(new UploadFile(), filter);

		Path path = Path.of(file.getPath());
		byte[] content = Files.readAllBytes(path);

		String dispositionType = inline ? "inline" : "attachment";

		return ResponseEntity.ok()
				.contentType(org.springframework.http.MediaType.parseMediaType(file.getType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, dispositionType + "; filename=\"" + file.getName() + "\"")
				.body(content);
	}

	// ============================================================================================
	// VALIDATE ASYNC
	// ============================================================================================

	/**
	 * Endpoint de validação assíncrona (mock/exemplo).
	 *
	 * Hoje ele apenas valida se o valor está numa lista fixa de usuários bloqueados.
	 * Você pode substituir pela chamada real via actionInboundPort/UseCase.
	 */
	@PostMapping("/validate/{method}/async")
	public ResponseEntity<Map<String, Boolean>> validate(@UIDomain Domain<?> domain, @PathVariable String method, @RequestBody String value) throws CheckedException {

		try {
			
			logger.info(ActionInboundAdapterPort.class, "Executando domínio no validate: " + domain);
			logger.info(ActionInboundAdapterPort.class, "Executando método: " + method + " com valor: " + value);

			List<String> blockedUsers = List.of("johndoe", "admin", "user123");
			boolean exists = blockedUsers.contains(value);

			return ResponseEntity.ok(Map.of("validators", exists));

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	// ============================================================================================
	// AUTOCOMPLETE
	// ============================================================================================

	/**
	 * Autocomplete genérico via searchByConditions.
	 * O filtro vem do frontend (chave/valor + operadores, dependendo da sua infra).
	 */
	@GetMapping("/autocomplete")
	public ResponseEntity<List<Domain<?>>> autocomplete(@UIDomain Domain<?> domain, @RequestParam Map<String, Object> filter) throws CheckedException {
		
		try {
		
			logger.info(ActionInboundAdapterPort.class, "Executando domínio no autocomplete: " + domain);

			List<Domain<?>> list = actionInboundPort.searchByConditions(domain, filter);
			
			return ResponseEntity.ok(list);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	// ============================================================================================
	// RESEARCH PAGINADO
	// ============================================================================================

	/**
	 * Pesquisa paginada (paginator do frontend).
	 * - Loga o filtro recebido
	 * - Delega para actionInboundPort.searchPaginated(...)
	 */
	@GetMapping("/research")
	public ResponseEntity<PageResult<?>> research(@UIDomain Domain<?> domain, @RequestParam Map<String, Object> filter) throws Exception {
		
		try {
		
			logger.info(ActionInboundAdapterPort.class, "Executando domínio no paginator: " + domain);
			logger.info(ActionInboundAdapterPort.class, "Valores do filtro no paginator:\n" + objectMapper.writeValueAsString(filter));

			PageResult<?> pageResult = actionInboundPort.searchPaginated(domain, filter);
			
			return ResponseEntity.ok(pageResult);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	// ============================================================================================
	// ACTION (CREATE / UPDATE / DELETE)
	// ============================================================================================

	/**
	 * Endpoint principal para executar ações (CRUD / métodos do UseCase).
	 *
	 * Comportamento:
	 * - Se vier "data": [...] -> batch (lista de domains).
	 * - Senão -> payload único.
	 *
	 * Ponto mais importante:
	 * - O Domain de entrada (param @UIDomain) já vem com ActionLogger/UserAccount resolvido.
	 * - Ao converter JsonNode em Domain via objectMapper.convertValue, esse novo Domain NÃO tem contexto.
	 * - Então chamamos propagateContext(...) para evitar:
	 *   - createdBy null (quando regra exige user obrigatório)
	 *   - transient instance (UserAccount com id null sendo persistido como entidade nova)
	 */
	@PostMapping
	public ResponseEntity<?> action(@UIDomain Domain<?> domain, @RequestBody JsonNode json) throws CheckedException {

		try {
			
			logger.info(ActionInboundAdapterPort.class, "Executando domínio no save: " + domain);
			logger.info(ActionInboundAdapterPort.class, "Payload recebido: \n" + json.toPrettyString());

			// ----------------------------
			// Batch
			// ----------------------------
			if (json.has("data") && json.get("data").isArray()) {

				List<Domain<?>> newDomains = new ArrayList<>();
				ArrayNode dataArray = (ArrayNode) json.get("data");

				for (JsonNode itemNode : dataArray) {
					// Normaliza o payload (ex.: ids, campos, nomes, etc.)
					JsonNode normalizedNode = NormalizeUtils.normalizer(itemNode);
					logger.info(ActionInboundAdapterPort.class, "Payload normalizado: \n" + normalizedNode.toPrettyString());

					// Converte o JSON normalizado no tipo de Domain “real”
					Domain<?> itemDomain = objectMapper.convertValue(normalizedNode, domain.getClass());

					// Propaga ActionLogger/UserAccount do Domain resolvido
					// (útil caso algum UseCase acesse itemDomain.getActionLogger()).
					propagateContext(domain, itemDomain);

					newDomains.add(itemDomain);
				}

				// ActionLogger vindo do front pode ter informações úteis (requestId, modal, url, etc.)
				// MAS o userAccount deve ser sempre o resolvido no backend.
				ActionLogger frontLogger = objectMapper.convertValue(json.get("actionLogger"), ActionLogger.class);
				
				mergeFrontLoggerPreservingUser(domain, frontLogger);

				// Executa o método padrão de batch (a porta decide a ação).
				List<Domain<?>> resultDomains = actionInboundPort.methodName(domain, newDomains);
				return ResponseEntity.ok(resultDomains);
			}

			// ----------------------------
			// Payload único
			// ----------------------------
			JsonNode normalizedNode = NormalizeUtils.normalizer(json);
			logger.info(ActionInboundAdapterPort.class, "Payload normalizado: \n" + normalizedNode.toPrettyString());

			Domain<?> newDomain = objectMapper.convertValue(normalizedNode, domain.getClass());

			// newDomain NÃO passou pelo resolver, então precisa receber o contexto.
			propagateContext(domain, newDomain);

			// Se o front mandou actionLogger nesse payload único, merge preservando user do backend.
			if (json.has("actionLogger") && json.get("actionLogger") != null && !json.get("actionLogger").isNull()) {
				ActionLogger frontLogger = objectMapper.convertValue(json.get("actionLogger"), ActionLogger.class);
				mergeFrontLoggerPreservingUser(newDomain, frontLogger);
			}

			Domain<?> resultDomain = actionInboundPort.methodName(newDomain);

			logger.info(ActionInboundAdapterPort.class, "Payload processado com sucesso: " + resultDomain);
			return ResponseEntity.ok(resultDomain);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	// ============================================================================================
	// HELPERS (CONTEXTO / LOGGER)
	// ============================================================================================

	/**
	 * Propaga o contexto do domain resolvido (@UIDomain) para um domain criado via convertValue.
	 *
	 * Por que isso existe?
	 * - O Spring injeta o Domain “resolvido” no parâmetro @UIDomain (com ActionLogger + UserAccount).
	 * - Mas você cria novos Domain(s) manualmente a partir do JSON, que não passam pelo resolver.
	 * - Sem isso, o UseCase pode tentar persistir entidades com createdBy null, ou com UserAccount “transient”.
	 *
	 * Regra aplicada:
	 * - Se o "to" não tem ActionLogger, copia inteiro.
	 * - Se tem ActionLogger mas não tem userAccount, seta o userAccount do "from".
	 */
	private void propagateContext(Domain<?> from, Domain<?> to) {
		
		if (from == null || to == null) return;

		try {
			
			if (to.getActionLogger() == null) {
				to.setActionLogger(from.getActionLogger());
			} else if (to.getActionLogger().getUserAccount() == null && from.getActionLogger() != null) {
				to.getActionLogger().setUserAccount(from.getActionLogger().getUserAccount());
			}
			
		} catch (Exception ignored) {
			// Silencioso por segurança: não derruba a request por falha de contexto.
			// Se preferir, logue como debug.
		}
	}

	/**
	 * Aplica ActionLogger vindo do front sem sobrescrever o userAccount já resolvido pelo backend.
	 *
	 * Por que?
	 * - O frontend pode mandar requestId, url, modal, etc.
	 * - Mas userAccount do front NÃO é confiável/nem sempre existe.
	 * - userAccount deve vir do backend (DomainResolver / segurança).
	 */
	private void mergeFrontLoggerPreservingUser(Domain<?> domain, ActionLogger frontLogger) {
		
		if (domain == null || frontLogger == null) return;

		try {
			
			UserAccount resolvedUser = null;
			
			if (domain.getActionLogger() != null) {
				resolvedUser = domain.getActionLogger().getUserAccount();
			}

			// userAccount sempre do backend.
			frontLogger.setUserAccount(resolvedUser);

			domain.setActionLogger(frontLogger);

		} catch (Exception ignored) {
			// Mesmo motivo: não derrubar request por falha no merge.
		}
	}

	// ============================================================================================
	// HELPERS (HTTP)
	// ============================================================================================

	/**
	 * Resolve o IP real do cliente considerando cenários com proxy/gateway.
	 *
	 * Ordem:
	 * 1) X-Forwarded-For (pega o primeiro IP quando vem em lista)
	 * 2) X-Real-IP
	 * 3) request.getRemoteAddr()
	 */
	private static String resolveClientIp(HttpServletRequest request) {
		
		String xff = request.getHeader("X-Forwarded-For");
		
		if (xff != null && !xff.isBlank()) {
			return xff.split(",")[0].trim();
		}
		
		String xri = request.getHeader("X-Real-IP");
		
		if (xri != null && !xri.isBlank()) {
			return xri.trim();
		}
		
		return request.getRemoteAddr();
	}
}