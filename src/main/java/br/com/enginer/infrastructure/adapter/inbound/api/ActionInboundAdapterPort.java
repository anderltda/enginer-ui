package br.com.enginer.infrastructure.adapter.inbound.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import br.com.enginer.domain.system.usecase.core.page.PageResult;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;
import br.com.enginer.domain.system.usecase.port.inbound.api.ActionInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.user.vo.JwtVo;
import br.com.enginer.infrastructure.configuration.security.KeycloakTenantResolver;
import br.com.enginer.infrastructure.utils.NormalizeUtils;

/**
 * Adaptador REST responsável por receber requisições externas e delegar a
 * execução de ações aos casos de uso (UseCases) correspondentes.
 */
@RestController
@RequestMapping("/v1/enginer-ui/action")
public class ActionInboundAdapterPort {

	private final ActionInboundPort<?> actionInboundPort;
	private final ObjectMapper objectMapper;
	private final LoggerOutboundPort logger;

	/**
	 * @param actionInboundPort
	 * @param objectMapper
	 * @param logger
	 */
	public ActionInboundAdapterPort(ActionInboundPort<?> actionInboundPort, ObjectMapper objectMapper, LoggerOutboundPort logger) {
		this.actionInboundPort = actionInboundPort;
		this.objectMapper = objectMapper;
		this.logger = logger;
	}

	// ============================================================================================
	// ME (AUTH)
	// ============================================================================================
	/**
	 * Resolve o usuário do seu sistema baseado no JWT (Keycloak).
	 *
	 * Fluxo esperado: - Front autentica direto no Keycloak (Authorization Code +
	 * PKCE) - Front chama GET /me com Authorization: Bearer <token> - Backend
	 * valida o token automaticamente via Resource Server e retorna UserAccount
	 */
	@GetMapping("/me")
	public ResponseEntity<UserAccount> me(@AuthenticationPrincipal Jwt jwt, @RequestHeader(value = "X-Tenant", required = false) String tenantHeader) throws CheckedException {

		try {

			// tenant pode vir do header ou cair no default
			final String provider = "KEYCLOAK";
			final String subject = jwt.getSubject();
			final String tenant = KeycloakTenantResolver.fromIssuer(jwt.getIssuer() != null ? jwt.getIssuer().toString() : null);
			final String email = jwt.getClaimAsString("email");
			// claims úteis (podem variar conforme seu Keycloak)
			final String username = StringsUtils.firstNonBlank(jwt.getClaimAsString("preferred_username"), jwt.getClaimAsString("username"));
			final String name = StringsUtils.firstNonBlank(jwt.getClaimAsString("name"), jwt.getClaimAsString("given_name"), username, subject);

			JwtVo jwtVo = new JwtVo(provider, subject, tenant, email, username, name);

			logger.info(ActionInboundAdapterPort.class, "Executando /me - sub=" + subject + ", username=" + username + ", tenant=" + tenant);

			UserAccount userAccount = (UserAccount) actionInboundPort.methodName(new UserAccount(), "login", jwtVo);

			if (userAccount == null) {
				userAccount = (UserAccount) actionInboundPort.methodName(new UserAccount(), "onboard", jwtVo);
			}

			if (userAccount == null) {
				// identity aponta pra um user inexistente (inconsistência)
				return ResponseEntity.status(401).build();
			}

			return ResponseEntity.ok(userAccount);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	/**
	 * @param jwt
	 */
	private void credentials(Jwt jwt) {
		String subject = jwt.getSubject(); // sub (id único do usuário no Keycloak)
		String username = jwt.getClaimAsString("preferred_username"); // username
		String email = jwt.getClaimAsString("email"); // email (se existir)

		// Exemplo: roles do realm
		Map<String, Object> realmAccess = jwt.getClaim("realm_access");
		// realmAccess.get("roles") -> lista

		logger.info(ActionInboundAdapterPort.class, "User authenticated: sub=" + subject + " username=" + username + " email=" + email);
	}

	// ============================================================================================
	// CALENDAR
	// ============================================================================================

	@GetMapping("/calendar")
	public ResponseEntity<List<Domain<?>>> calendar(@UIDomain Domain<?> domain, @RequestParam String year,
			@RequestParam String month, @RequestParam String day, @AuthenticationPrincipal Jwt jwt) {

		try {

			credentials(jwt);

			logger.info(ActionInboundAdapterPort.class, "Executando calendar: " + domain);

			if (year == null || year.trim().isEmpty() || month == null || month.trim().isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			logger.info(ActionInboundAdapterPort.class, day + "/" + month + "/" + year);

			LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
			LocalDateTime endDateTime = LocalDateTime.of(2035, 12, 31, 23, 59, 59);

			Map<String, Object> filters = new HashMap<>();
			filters.put("startDateTime", startDateTime);
			filters.put("startDateTime_op", Constants.MAIOR_OU_IGUAL);
			filters.put("endDateTime", endDateTime);
			filters.put("endDateTime_op", Constants.MENOR_OU_IGUAL);

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

	@GetMapping("/search")
	public ResponseEntity<SearchOverlay> search(@UIDomain Domain<?> domain,
			@RequestParam(required = false) String params, @AuthenticationPrincipal Jwt jwt) {

		try {

			credentials(jwt);

			logger.info(ActionInboundAdapterPort.class, "Executando search: " + domain);

			if (params == null || params.trim().isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			boolean hasComma = params.contains(";");

			List<String> parts = hasComma
					? Arrays.stream(params.split(";")).map(String::trim).filter(s -> !s.isBlank()).toList()
					: Arrays.stream(params.trim().split("\\s+")).map(String::trim).filter(s -> !s.isBlank()).toList();

			String result = parts.stream().map(ReflectionUtils::normalizeAlphaNumeric).distinct()
					.collect(Collectors.joining(","));

			Map<String, Object> filters = new HashMap<>();
			filters.put("id.normalizedName", result);
			filters.put("id.normalizedName_op", Constants.IN);

			filters.put("type", TagType.GLOBAL);
			List<Domain<?>> globais = actionInboundPort.searchByConditions(domain, filters);

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

	@PostMapping("/session")
	public ResponseEntity<Map<String, String>> startSession(@AuthenticationPrincipal Jwt jwt) throws CheckedException {
		credentials(jwt);
		String uploadId = (String) actionInboundPort.methodName(new UploadFile(), "startSession");
		return ResponseEntity.ok(Map.of("uploadId", Objects.toString(uploadId, "")));
	}

	@PostMapping("/chunk")
	public ResponseEntity<Void> uploadChunk(@RequestParam String uploadId, @RequestParam int chunkIndex, @RequestParam("file") MultipartFile part, @AuthenticationPrincipal Jwt jwt) throws Exception {
		
		credentials(jwt);
		
		actionInboundPort.methodName(new UploadFile(), "uploadChunk", uploadId, chunkIndex, part.getInputStream());
		
		return ResponseEntity.ok().build();
	}

	@PostMapping("/finalize")
	public ResponseEntity<UploadFile> finalizeUpload(@RequestParam String uploadId, @RequestPart("meta") String jsonMeta, @AuthenticationPrincipal Jwt jwt) throws Exception {
		
		credentials(jwt);

		UploadFile uploadFile = objectMapper.readValue(jsonMeta, UploadFile.class);
		
		uploadFile.setUid(uploadId);
		
		uploadFile = (UploadFile) actionInboundPort.methodName(uploadFile);

		return ResponseEntity.ok(uploadFile);
	}

	// ============================================================================================
	// DOWNLOAD
	// ============================================================================================

	@GetMapping("/download")
	public ResponseEntity<byte[]> download(@UIDomain Domain<?> domain, @RequestParam Map<String, Object> filter, @RequestParam(defaultValue = "true") boolean inline, @AuthenticationPrincipal Jwt jwt) throws IOException {

		credentials(jwt);

		if (filter != null && filter.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		UploadFile file = (UploadFile) actionInboundPort.searchWithBySingleConditions(new UploadFile(), filter);

		Path path = Path.of(file.getPath());
		byte[] content = Files.readAllBytes(path);

		String dispositionType = inline ? "inline" : "attachment";

		return ResponseEntity.ok().contentType(org.springframework.http.MediaType.parseMediaType(file.getType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, dispositionType + "; filename=\"" + file.getName() + "\"")
				.body(content);
	}

	// ============================================================================================
	// VALIDATE ASYNC
	// ============================================================================================

	@PostMapping("/validate/{method}/async")
	public ResponseEntity<Map<String, Boolean>> validate(@UIDomain Domain<?> domain, @PathVariable String method, @RequestBody String value, @AuthenticationPrincipal Jwt jwt) throws CheckedException {

		try {

			credentials(jwt);

			logger.info(ActionInboundAdapterPort.class, "Executando domínio no validate: " + domain);

			logger.info(ActionInboundAdapterPort.class, "Executando método: " + method + " com valor: " + value);

			List<String> blockedUsers = List.of("johndoe", "admin", "user123");
			boolean exists = blockedUsers.contains(value);

			Map<String, Boolean> response = Map.of("validators", exists);
			return ResponseEntity.ok(response);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	// ============================================================================================
	// AUTOCOMPLETE
	// ============================================================================================

	@GetMapping("/autocomplete")
	public ResponseEntity<List<Domain<?>>> autocomplete(@UIDomain Domain<?> domain, @RequestParam Map<String, Object> filter, @AuthenticationPrincipal Jwt jwt) throws CheckedException {

		try {

			credentials(jwt);

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

	@GetMapping("/research")
	public ResponseEntity<PageResult<?>> research(@UIDomain Domain<?> domain, @RequestParam Map<String, Object> filter, @AuthenticationPrincipal Jwt jwt) throws Exception {

		try {

			credentials(jwt);

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

	@PostMapping
	public ResponseEntity<?> action(@UIDomain Domain<?> domain, @RequestBody JsonNode json, @AuthenticationPrincipal Jwt jwt) throws CheckedException {

		try {

			credentials(jwt);

			logger.info(ActionInboundAdapterPort.class, "Executando domínio no save: " + domain);

			logger.info(ActionInboundAdapterPort.class, "Payload recebido: \n" + json.toPrettyString());

			List<Domain<?>> newDomains = new ArrayList<>();

			if (json.has("data") && json.get("data").isArray()) {

				ArrayNode dataArray = (ArrayNode) json.get("data");

				for (JsonNode itemNode : dataArray) {

					JsonNode normalizedNode = NormalizeUtils.normalizer(itemNode);
					logger.info(ActionInboundAdapterPort.class,
							"Payload normalizado: \n" + normalizedNode.toPrettyString());

					Domain<?> itemDomain = objectMapper.convertValue(normalizedNode, domain.getClass());
					newDomains.add(itemDomain);
				}

				ActionLogger actionLogger = objectMapper.convertValue(json.get("actionLogger"), ActionLogger.class);
				domain.setActionLogger(actionLogger);

				List<Domain<?>> resultDomains = actionInboundPort.methodName(domain, newDomains);
				return ResponseEntity.ok(resultDomains);
			}

			JsonNode normalizedNode = NormalizeUtils.normalizer(json);
			logger.info(ActionInboundAdapterPort.class, "Payload normalizado: \n" + normalizedNode.toPrettyString());

			Domain<?> newDomain = objectMapper.convertValue(normalizedNode, domain.getClass());
			Domain<?> resultDomain = actionInboundPort.methodName(newDomain);

			logger.info(ActionInboundAdapterPort.class, "Payload processado com sucesso: " + resultDomain);
			return ResponseEntity.ok(resultDomain);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}
}