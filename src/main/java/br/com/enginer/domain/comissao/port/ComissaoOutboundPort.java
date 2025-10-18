package br.com.enginer.domain.comissao.port;

import br.com.enginer.domain.comissao.dto.ComissaoDto;

public interface ComissaoOutboundPort {
	
	ComissaoDto findByComissao(String codigo);

}
