package br.com.enginer.domain.bre.port;

import br.com.enginer.domain.bre.dto.BreResponseDto;

public interface BreOutboundPort {

	BreResponseDto buscaDadosBre(String endpoint);

}
