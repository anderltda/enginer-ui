package br.com.enginer.infrastructure.adapter.inbound.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import br.com.enginer.domain.system.dto.entity.UploadFile;
import br.com.enginer.domain.system.usercase.annotation.instance.UIDomain;
import br.com.enginer.domain.system.usercase.exception.CheckedException;
import br.com.enginer.domain.system.usercase.logger.ActionLogger;
import br.com.enginer.domain.system.usercase.page.PageResult;
import br.com.enginer.domain.system.usercase.port.inbound.ActionInboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.LoggerOutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.utils.NormalizeUtils;

/**
 * Adaptador REST responsável por receber requisições externas
 * e delegar a execução de ações aos casos de uso (UserCases) correspondentes.
 */
@RestController
@RequestMapping("/v1/enginer-ui/action")
public class ActionInboundAdapterPort {

	private final ActionInboundPort<?> actionInboundPort;
	private final ObjectMapper objectMapper;
	private final LoggerOutboundPort logger;

	public ActionInboundAdapterPort(ActionInboundPort<?> actionInboundPort, ObjectMapper objectMapper, LoggerOutboundPort logger) {
		this.actionInboundPort = actionInboundPort;
		this.objectMapper = objectMapper;
		this.logger = logger;
	}

	// ============================================================================================
	// UPLOAD
	// ============================================================================================

	@PostMapping("/upload")
	public ResponseEntity<Map<String, Object>> upload(@UIDomain Domain<?> domain, @RequestParam MultipartFile multipartFile, @RequestParam("actionLogger") String actionLoggerJson) throws Exception {

		try {
			
			if (multipartFile.isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of("error", "Arquivo está vazio."));
			}

			Path uploadPath = Path.of("/Users/anderson/Downloads/uploads/");
			
			Files.createDirectories(uploadPath);

			String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			Path destination = uploadPath.resolve(filename);

			Files.copy(multipartFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

			ActionLogger actionLogger = objectMapper.readValue(actionLoggerJson, ActionLogger.class);

			UploadFile uploadFile = new UploadFile();
			uploadFile.setName(filename);
			uploadFile.setType(multipartFile.getContentType());
			uploadFile.setSize(multipartFile.getSize());
			uploadFile.setPath(destination.toString());
			uploadFile.setStorageType("LOCAL");
			uploadFile.setChecksumSha256(DigestUtils.sha256Hex(multipartFile.getBytes()));
			uploadFile.setDomain(domain.getClass().getSimpleName());
			uploadFile.setDomainId(null);
			uploadFile.setIsPublic(false);
			uploadFile.setCreatedAt(LocalDateTime.now());
			uploadFile.setActionLogger(actionLogger);

			uploadFile = (UploadFile) actionInboundPort.methodName(uploadFile);

			Map<String, Object> response = new LinkedHashMap<>();
			response.put("id", uploadFile.getId());
			response.put("filename", filename);
			response.put("checksum", uploadFile.getChecksumSha256());
			response.put("uploadedAt", uploadFile.getCreatedAt());

			return ResponseEntity.ok(response);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	// ============================================================================================
	// VALIDATE ASYNC
	// ============================================================================================

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
	// SEARCH PAGINADO
	// ============================================================================================

	@GetMapping("/search")
	public ResponseEntity<PageResult<?>> search(@UIDomain Domain<?> domain, @RequestParam Map<String, Object> filter) throws CheckedException {

		try {
			
			logger.info(ActionInboundAdapterPort.class, "Executando domínio no paginator: " + domain);
			
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

				ActionLogger actionLogger = objectMapper.convertValue(json.get("action"), ActionLogger.class);
				List<Domain<?>> resultDomains = actionInboundPort.methodName(domain, newDomains, actionLogger);
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