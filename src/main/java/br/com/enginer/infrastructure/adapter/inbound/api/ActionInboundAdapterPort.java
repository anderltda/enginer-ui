package br.com.enginer.infrastructure.adapter.inbound.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

import br.com.enginer.domain.example.dto.entity.File;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.dto.logger.ActionLogger;
import br.com.enginer.domain.ui.port.inbound.ActionInboundPort;
import br.com.enginer.domain.ui.port.outbound.LoggerOutboundPort;
import br.com.enginer.domain.ui.usercase.annotation.instance.UIDomain;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.schema.field.behavior.upload.UploadFile;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.utils.NormalizeUtils;

/**
 * 
 */
@RestController
@RequestMapping("/v1/enginer-ui/action")
public class ActionInboundAdapterPort {

	private final ActionInboundPort actionInboundPort;
	private final ObjectMapper objectMapper;
	private final LoggerOutboundPort logger;

	/**
	 * @param actionInboundPort
	 * @param objectMapper
	 * @param logger
	 */
	public ActionInboundAdapterPort(ActionInboundPort actionInboundPort, ObjectMapper objectMapper,
			LoggerOutboundPort logger) {
		this.actionInboundPort = actionInboundPort;
		this.objectMapper = objectMapper;
		this.logger = logger;
	}

	/**
	 * @param domain
	 * @param file
	 * @param actionLoggerJson
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload")
	public ResponseEntity<Map<String, Object>> upload(@UIDomain Domain<?> domain, @RequestParam MultipartFile file, @RequestParam("actionLogger") String actionLoggerJson) throws Exception {

		try {

	        if (file.isEmpty()) {
	            return ResponseEntity.badRequest().body(Map.of("error", "Arquivo está vazio."));
	        }

	        // Diretório base do upload
	        Path uploadPath = Path.of("/Users/anderson/Downloads/uploads/");
	        Files.createDirectories(uploadPath);

	        // Normaliza o nome do arquivo e define o destino
	        String filename = StringUtils.cleanPath(file.getOriginalFilename());
	        Path destination = uploadPath.resolve(filename);

	        // Salva o arquivo fisicamente
	        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

	        ActionLogger actionLogger = objectMapper.readValue(actionLoggerJson, ActionLogger.class);
	        
	        
	        // Cria o objeto File (entidade da tabela files)
	        UploadFile uploadFile = new UploadFile();
	        uploadFile.setName(filename);
	        uploadFile.setStatus(file.getContentType());

	        File domainFile = new File();
	        domainFile.setFileName(filename);
	        domainFile.setFileType(file.getContentType());
	        domainFile.setFileSize(file.getSize());
	        domainFile.setFilePath(destination.toString());
	        domainFile.setStorageType("LOCAL");
	        domainFile.setChecksumSha256(DigestUtils.sha256Hex(file.getBytes()));
	        domainFile.setDomain(domain.getClass().getSimpleName());
	        domainFile.setEntityId(null);            // Ainda não existe no momento do upload
	        domainFile.setIsPublic(false);
	        domainFile.setCreatedAt(LocalDateTime.now());
	        domainFile.setActionLogger(actionLogger);

	        domainFile = (File) actionInboundPort.methodName(domainFile);
			
	        // Retorna metadados úteis
	        Map<String, Object> response = Map.of(
	        	"id", domainFile.getId(),
	        	"filename", filename
	        );

	        return ResponseEntity.ok(response);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	/**
	 * @param method
	 * @param value
	 * @return
	 * @throws CheckedException
	 */
	@PostMapping("/validate/{method}/async")
	public ResponseEntity<Map<String, Boolean>> validate(@UIDomain Domain<?> domain, @PathVariable String method, @RequestBody String value) throws CheckedException {

		try {

			logger.info(ActionInboundAdapterPort.class, "Executando domínio no validate: " + domain);
			logger.info(ActionInboundAdapterPort.class, "Executando method: " + method);
			logger.info(ActionInboundAdapterPort.class, "Valor recebido: " + value);

			String[] array = new String[] { "johndoe", "admin", "user123" };
			Map<String, Boolean> response = new HashMap<>();
			response.put("validators", false);

			for (String string : array) {
				if (value.equals(string)) {
					response.put("validators", true);
				}
			}

			return ResponseEntity.ok(response);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws CheckedException
	 */
	@GetMapping({ "/autocomplete" })
	public ResponseEntity<List<Domain<?>>> autocomplete(@UIDomain Domain<?> domain,
			@RequestParam Map<String, Object> filter) throws CheckedException {

		try {

			logger.info(ActionInboundAdapterPort.class, "Executando domínio no autocomplete: " + domain);

			List<Domain<?>> list = actionInboundPort.searchByConditions(domain, filter);

			return ResponseEntity.ok(list);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}

	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws CheckedException
	 */
	@GetMapping({ "/search" })
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
			logger.info(ActionInboundAdapterPort.class, "Payload recebido: \r " + json.toPrettyString());

			List<Domain<?>> newDomains = new ArrayList<>();
			
			if (json.has("data") && json.get("data").isArray()) {
				
				ArrayNode dataArray = (ArrayNode) json.get("data");
				
				for (JsonNode itemNode : dataArray) {
					
					JsonNode normalizedNode = NormalizeUtils.normalizer(itemNode);
					
					logger.info(ActionInboundAdapterPort.class, "Payload normalized: \r " + normalizedNode.toPrettyString());
					
					Domain<?> itemDomain = objectMapper.convertValue(normalizedNode, domain.getClass());
				
					newDomains.add(itemDomain);
				
				}
				
				JsonNode actionNode = json.get("action");

				ActionLogger actionLogger = objectMapper.convertValue(actionNode, ActionLogger.class);
				
				List<Domain<?>> domains = actionInboundPort.methodName(domain, newDomains, actionLogger);
				
				return ResponseEntity.ok(domains);
			}
			
			JsonNode normalizedNode = NormalizeUtils.normalizer(json);
			
			logger.info(ActionInboundAdapterPort.class, "Payload normalized: \r " + normalizedNode.toPrettyString());

			Domain<?> newDomain = objectMapper.convertValue(normalizedNode, domain.getClass());

			domain = actionInboundPort.methodName(newDomain);

			logger.info(ActionInboundAdapterPort.class, "Payload enviado: \r " + domain);

			return ResponseEntity.ok(domain);

		} catch (Exception ex) {
			logger.error(ActionInboundAdapterPort.class, ex);
			throw ex;
		}
	}
}