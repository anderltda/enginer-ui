package br.com.enginer.infrastructure.adapter.outbound.comissao;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.comissao.dto.ComissaoDto;
import br.com.enginer.domain.comissao.port.ComissaoOutboundPort;

@Component
public class ComissaoOutboundPortAdapter implements ComissaoOutboundPort {

	//private final ComissaoRepository comissaoRepository;

	//public ComissaoOutboundPortAdapter(ComissaoRepository comissaoRepository) {
	//	this.comissaoRepository = comissaoRepository;
	//}

	@Override
	public ComissaoDto findByComissao(String codigo) {
		ComissaoDto comissaoDto = new ComissaoDto();
		comissaoDto.setCnpj("32.234.234.0001/10");
		comissaoDto.setCodigo(codigo);
		comissaoDto.setId(11l);
		comissaoDto.setValor(234.66);
		return comissaoDto;
	}

}
