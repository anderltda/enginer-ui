package br.com.enginer.infrastructure.adapter.inbound;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.enginer.domain.rule.port.RuleInboundPort;
import br.com.enginer.infrastructure.tracking.TrackingProvider;

@RestController
@RequestMapping("/v1/rule")
public class RuleInboundPortAdapter {

	private static final Logger LOGGER = LogManager.getLogger(RuleInboundPortAdapter.class);

	private final ObjectMapper objectMapper;
	private final RuleInboundPort ruleInboundPort;
	private final TrackingProvider trackingProvider;

	// Caminho base no macOS
	private static final String BASE_PATH = "/Users/anderson/Developer/angular/pages/src/assets/data/payloads/";

	public RuleInboundPortAdapter(ObjectMapper objectMapper, RuleInboundPort ruleInboundPort,
			TrackingProvider trackingProvider) {
		this.objectMapper = objectMapper;
		this.ruleInboundPort = ruleInboundPort;
		this.trackingProvider = trackingProvider;
	}

	@PostMapping("/{model}")
	public ResponseEntity<String> post(@PathVariable String model, @RequestBody JsonNode json) throws Exception {

		LOGGER.info("Executando modelo: {}", model);
		configureTrackingLog();
		LOGGER.info("Payload recebido: \r {} \r", json.toPrettyString());

		boolean error = false;

		if (error) {
			throw new Exception("ENGINER MESSAGE: Erro interno no servidor: Operação não pode continuar.");
		}

		try {
			Thread.sleep(1000);
			ruleInboundPort.executeRuleAcaoCivil(null);
			ruleInboundPort.executeRuleFraude(null);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOGGER.error("Erro ao executar regras", e);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(json.toPrettyString());
	}

	@PostMapping("/{method}/validatorsAsync")
	public ResponseEntity<Map<String, Boolean>> validatorsAsync(@PathVariable String method, @RequestBody String value) throws Exception {

		LOGGER.info("Executando method: {}", method);
		LOGGER.info("Valor recebido: \r {} \r", value);

		String[] array = new String[] { "johndoe", "admin", "user123" };
		Map<String, Boolean> response = new HashMap<>();
		response.put("validators", false);

		for (String string : array) {
			if (value.equals(string)) {
			    response.put("validators", true);
			}
		}

		boolean error = false;

		if (error) {
			throw new Exception("ENGINER MESSAGE: Erro interno no servidor: Operação não pode continuar.");
		}

		try {
			Thread.sleep(0);
			ruleInboundPort.executeRuleAcaoCivil(null);
			ruleInboundPort.executeRuleFraude(null);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOGGER.error("Erro ao executar regras", e);
		}

	    return ResponseEntity.ok(response);
	}

	@GetMapping("/{type}/{model}")
	public ResponseEntity<JsonNode> getJsonFile(@PathVariable String type, @PathVariable String model) {
		try {
			// Monta o caminho do arquivo com o diretório desejado
			Path filePath = Paths.get(BASE_PATH, type, model + ".json");

			LOGGER.info("Buscando arquivo JSON: {}", filePath);

			// Verifica se o arquivo existe antes de tentar ler
			if (!Files.exists(filePath)) {
				LOGGER.error("Arquivo não encontrado: {}", filePath);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			// Lê o conteúdo do arquivo
			String content = Files.readString(filePath);

			// Converte o conteúdo do JSON em um objeto JsonNode
			JsonNode jsonNode = objectMapper.readTree(content);

			return ResponseEntity.ok(jsonNode);
		} catch (IOException e) {
			LOGGER.error("Erro ao ler o arquivo JSON: {} - {}", model, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private void configureTrackingLog() {
		trackingProvider.setInnerId("1");
		trackingProvider.setCid("22");
	}
}