package br.com.enginer.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import reactor.core.publisher.Mono;

/**
 * 
 */
public class GlobalWebClientErrorHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalWebClientErrorHandler.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * @param response
	 * @return
	 */
	public static Mono<? extends Throwable> handle4xxError(ClientResponse response) {
		return response.bodyToMono(String.class).flatMap(body -> {
			ErrorResponse error = parseError(body);
			String message = buildMessage(error, response);
			LOGGER.error("Erro 4xx capturado do backend: {}", buildMessageLog(error, response));
			return Mono.error(new CheckedException(message));
		});
	}

	/**
	 * @param response
	 * @return
	 */
	public static Mono<? extends Throwable> handle5xxError(ClientResponse response) {
		return response.bodyToMono(String.class).flatMap(body -> {
			ErrorResponse error = parseError(body);
			String message = buildMessage(error, response);
			LOGGER.error("Erro 5xx capturado do backend: {}", buildMessageLog(error, response));
			return Mono.error(new UncheckedException(message));
		});
	}

	/**
	 * @param body
	 * @return
	 */
	private static ErrorResponse parseError(String body) {
		try {
			return objectMapper.readValue(body, ErrorResponse.class);
		} catch (Exception e) {
			LOGGER.warn("Erro ao parsear corpo de erro como JSON estruturado: {}", body);
			ErrorResponse fallback = new ErrorResponse();
			fallback.setMessage(body);
			return fallback;
		}
	}
	
	/**
	 * @param error
	 * @param response
	 * @return
	 */
	private static String buildMessage(ErrorResponse error, ClientResponse response) {
		return String.format("Status: %s - %s", 
				error.getStatus(), 
				error.getMessage(), 
				response.statusCode().value(),
				error.getCustomMessage());
	}

	/**
	 * @param error
	 * @param response
	 * @return
	 */
	private static String buildMessageLog(ErrorResponse error, ClientResponse response) {
		return String.format("Erro HTTP %d - status: %s, message: %s, customMessage: %s", 
				response.statusCode().value(),
				error.getStatus(), 
				error.getMessage(), 
				error.getCustomMessage());
	}
}
