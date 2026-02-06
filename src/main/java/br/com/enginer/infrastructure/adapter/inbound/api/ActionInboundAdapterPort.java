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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import br.com.enginer.domain.system.dto.entity.user.LoginRequest;
import br.com.enginer.domain.system.dto.entity.user.LoginResponse;
import br.com.enginer.domain.system.dto.entity.user.RefreshRequest;
import br.com.enginer.domain.system.dto.entity.user.RefreshResponse;
import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.dto.entity.user.UserAccountIdentity;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UIDomain;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.page.PageResult;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.port.inbound.api.ActionInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.infrastructure.service.JwtUnsafeReader;
import br.com.enginer.infrastructure.service.KeycloakTokenService;
import br.com.enginer.infrastructure.utils.NormalizeUtils;

/**
 * Adaptador REST responsável por receber requisições externas
 * e delegar a execução de ações aos casos de uso (UseCases) correspondentes.
 */
@RestController
@RequestMapping("/v1/enginer-ui/action")
public class ActionInboundAdapterPort {
	
	private final ActionInboundPort<?> actionInboundPort;
	private final KeycloakTokenService keycloakTokenService;
	private final ObjectMapper objectMapper;
	private final LoggerOutboundPort logger;

	/**
	 * @param actionInboundPort
	 * @param keycloakTokenService
	 * @param objectMapper
	 * @param logger
	 */
	public ActionInboundAdapterPort(ActionInboundPort<?> actionInboundPort, KeycloakTokenService keycloakTokenService, ObjectMapper objectMapper, LoggerOutboundPort logger) {
		this.actionInboundPort = actionInboundPort;
		this.keycloakTokenService = keycloakTokenService;
		this.objectMapper = objectMapper;
		this.logger = logger;
	}
	
	// ============================================================================================
	// LOGIN
	// Status codes esperados
	// •	200 OK: login ok
	// •	401 Unauthorized: provider/subject inválido (ou usuário inativo)
	// •	403 Forbidden: usuário bloqueado / sem permissão
	// •	422 Unprocessable Entity: payload inválido (faltando provider/subject)
	// •	500: erro inesperado
	// ============================================================================================
	/**
	 * @return
	 * @throws CheckedException
	 */
	@PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@UIDomain Domain<?> domain, @RequestBody LoginRequest req) throws CheckedException {
		
		logger.info(ActionInboundAdapterPort.class, "Executando login: " + domain);
		
		// 1) autentica no provider (Keycloak) e pega token
		if (!"KEYCLOAK".equalsIgnoreCase(req.provider())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	    KeycloakTokenService.TokenResponse token = keycloakTokenService.passwordGrant(req.username(), req.password());

	    // 2) extrai subject (sub) do access token
	    String subject = JwtUnsafeReader.readSub(token.accessToken());

	    // 3) acha identidade no seu banco (provider + tenant + subject)
		Map<String, Object> filters = new HashMap<>();
		filters.put("provider", req.provider());
		filters.put("providerTenant", req.tenant());
		filters.put("providerSubject", subject);

		UserAccountIdentity identity = (UserAccountIdentity) actionInboundPort.searchWithBySingleConditions(new UserAccountIdentity(), filters);
        
	    if (identity == null) {
	        // aqui você decide:
	        // - criar automaticamente user/identity (provisioning), OU
	        // - bloquear login
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		
	    // 4) carrega UserAccount do seu sistema
		filters.clear();
		filters.put("id", identity.getUserId());
		
		UserAccount userAccount = (UserAccount) actionInboundPort.searchWithBySingleConditions(domain, filters);
		
		// 5) devolve o contrato recomendado
		LoginResponse response = new LoginResponse(
				token.accessToken(), 
				token.refreshToken(), 
				token.expiresIn(),
				token.tokenType(), 
				userAccount);
	    
        return ResponseEntity.ok(response);
    }
	
	/**
	 * @param req
	 * @return
	 */
	@PostMapping("/refresh")
	public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshRequest req) {

	    if (req == null || req.refreshToken() == null || req.refreshToken().isBlank()) {
	        return ResponseEntity.unprocessableEntity().build(); // 422
	    }

	    try {
	    	
	        KeycloakTokenService.TokenResponse token = keycloakTokenService.refreshGrant(req.refreshToken());

	        RefreshResponse response = new RefreshResponse(
	            token.accessToken(),
	            token.refreshToken(), 
	            token.expiresIn(),
	            token.tokenType()
	        );

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        // refresh inválido/expirado => 401
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	}
	
	/**
	 * @param jwt
	 * @return
	 */
	@GetMapping("/secure-test")
	public Map<String, Object> secureTest(@AuthenticationPrincipal Jwt jwt) {
	    return Map.of(
	        "sub", jwt.getSubject(),
	        "preferred_username", jwt.getClaimAsString("preferred_username"),
	        "iss", jwt.getIssuer().toString(),
	        "exp", jwt.getExpiresAt()
	    );
	}
	
	// ============================================================================================
	// CALENDAR
	// ============================================================================================
	
	/**
	 * @param domain
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	@GetMapping("/calendar")
	public ResponseEntity<List<Domain<?>>> calendar(@UIDomain Domain<?> domain, @RequestParam String year, @RequestParam String month, @RequestParam String day) {

		try {

			logger.info(ActionInboundAdapterPort.class, "Executando calendar: " + domain);

			if (year == null || year.trim().isEmpty() || month == null || month.trim().isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			logger.info(ActionInboundAdapterPort.class, day +"/"+ month +"/"+ year);

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
	// TAGS
	// ============================================================================================
	
	/**
	 * @param domain
	 * @param params
	 * @return
	 */
	@GetMapping("/search")
	public ResponseEntity<SearchOverlay> search(@UIDomain Domain<?> domain, @RequestParam(required = false) String params) {
	    
		try {
			
			logger.info(ActionInboundAdapterPort.class, "Executando search: " + domain);

		    if (params == null || params.trim().isEmpty()) {
		      return ResponseEntity.notFound().build();
		    }
		    
			boolean hasComma = params.contains(";");

			List<String> parts = hasComma
					? Arrays.stream(params.split(";")).map(String::trim)
					.filter(s -> !s.isBlank())
					.toList()
					: Arrays.stream(params.trim().split("\\s+")).map(String::trim)
					.filter(s -> !s.isBlank())
					.toList();

			String result = parts.stream()
					.map(s -> ReflectionUtils.normalizeAlphaNumeric(s))
					.distinct()
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

	/**
	 * @return
	 * @throws CheckedException
	 */
	@PostMapping("/session")
    public ResponseEntity<Map<String, String>> startSession() throws CheckedException {
		
        String uploadId = (String) actionInboundPort.methodName(new UploadFile(), "startSession");
        
        return ResponseEntity.ok(Map.of("uploadId", Objects.toString(uploadId, "")));
    }

    /**
     * @param uploadId
     * @param chunkIndex
     * @param part
     * @return
     * @throws Exception
     */
    @PostMapping("/chunk")
    public ResponseEntity<Void> uploadChunk(@RequestParam String uploadId, @RequestParam int chunkIndex, @RequestParam("file") MultipartFile part) throws Exception {

        actionInboundPort.methodName(new UploadFile(), "uploadChunk", uploadId, chunkIndex, part.getInputStream());
        
        return ResponseEntity.ok().build();
    }

    /**
     * @param uploadId
     * @param jsonMeta
     * @return
     * @throws Exception
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
	 * @param domain
	 * @param filter
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/download")
	public ResponseEntity<byte[]> download(@UIDomain Domain<?> domain, @RequestParam Map<String, Object> filter, @RequestParam(defaultValue = "true") boolean inline) throws IOException {

		if(filter != null && filter.isEmpty()) {
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
	 * @param domain
	 * @param method
	 * @param value
	 * @return
	 * @throws CheckedException
	 */
	@PostMapping("/validate/{method}/async")
	public ResponseEntity<Map<String, Boolean>> validate(@UIDomain Domain<?> domain, @PathVariable String method, @RequestBody String value) throws CheckedException {

		try {
			
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

	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws CheckedException
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
	 * @param domain
	 * @param filter
	 * @return
	 * @throws Exception
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
	 * @param domain
	 * @param json
	 * @return
	 * @throws CheckedException
	 */
	@PostMapping
	public ResponseEntity<?> action(@UIDomain Domain<?> domain, @RequestBody JsonNode json) throws CheckedException {

		try {
			
			logger.info(ActionInboundAdapterPort.class, "Executando domínio no save: " + domain);
			logger.info(ActionInboundAdapterPort.class, "Payload recebido: \n" + json.toPrettyString());

			List<Domain<?>> newDomains = new ArrayList<>();

			if (json.has("data") && json.get("data").isArray()) {

				ArrayNode dataArray = (ArrayNode) json.get("data");

				for (JsonNode itemNode : dataArray) {
					
					JsonNode normalizedNode = NormalizeUtils.normalizer(itemNode);
					
					logger.info(ActionInboundAdapterPort.class, "Payload normalizado: \n" + normalizedNode.toPrettyString());
					
					Domain<?> itemDomain = objectMapper.convertValue(normalizedNode, domain.getClass());
					
					newDomains.add(itemDomain);
				}

				ActionLogger actionLogger = objectMapper.convertValue(json.get("actionLogger"), ActionLogger.class);
				
				domain.setActionLogger(actionLogger);
				
				List<Domain<?>> resultDomains = actionInboundPort.methodName(domain, newDomains);
				
				return ResponseEntity.ok(resultDomains);
			}

			// payload único
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