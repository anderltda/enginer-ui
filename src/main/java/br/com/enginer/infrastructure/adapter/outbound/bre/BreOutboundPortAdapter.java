package br.com.enginer.infrastructure.adapter.outbound.bre;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.enginer.domain.bre.dto.BreResponseDto;
import br.com.enginer.domain.bre.port.BreOutboundPort;

@Component
public class BreOutboundPortAdapter implements BreOutboundPort {

	private final WebClient webClient;

	public BreOutboundPortAdapter(WebClient webClient) {
		this.webClient = WebClient.builder().build();
	}

	@Override
	public BreResponseDto buscaDadosBre(String endpoint) {
		// TODO Auto-generated method stub
		String json = webClient.get().uri(endpoint).retrieve().bodyToMono(String.class).block();
		BreResponseDto breResponseDto = new BreResponseDto();
		breResponseDto.setAnalysisDate(json);
		breResponseDto.setConclusion(json);
		breResponseDto.setReasonCodes(null);
		return breResponseDto;
	}

}
