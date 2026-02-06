package br.com.enginer.infrastructure.service;

import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtUnsafeReader {
	
	private static final ObjectMapper om = new ObjectMapper();

	public static String readSub(String jwt) {
		try {
			String[] parts = jwt.split("\\.");
			String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
			JsonNode node = om.readTree(payload);
			return node.get("sub").asText();
		} catch (Exception e) {
			throw new RuntimeException("Falha ao ler JWT", e);
		}
	}
}
