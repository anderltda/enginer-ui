package br.com.enginer.domain.rule.usercase;

import java.util.Map;

import br.com.enginer.domain.bre.dto.BreResponseDto;
import br.com.enginer.domain.bre.port.BreOutboundPort;
import br.com.enginer.domain.comissao.dto.ComissaoDto;
import br.com.enginer.domain.comissao.port.ComissaoOutboundPort;
import br.com.enginer.domain.logger.dto.LoggerDto;
import br.com.enginer.domain.logger.port.LoggerOutboundPort;
import br.com.enginer.domain.rule.port.RuleInboundPort;

public class RuleUserCase implements RuleInboundPort {

	private final LoggerOutboundPort loggerOutboundPort;
	private final BreOutboundPort breOutboundPort;
	private final ComissaoOutboundPort comissaoOutboundPort;

	public RuleUserCase(LoggerOutboundPort loggerOutboundPort, BreOutboundPort breOutboundPort, ComissaoOutboundPort comissaoOutboundPort) {
		this.loggerOutboundPort = loggerOutboundPort;
		this.breOutboundPort = breOutboundPort;
		this.comissaoOutboundPort = comissaoOutboundPort;
	}

	@Override
	public void executeRuleFraude(Map<String, Object> json) {
		// TODO Auto-generated method stub
		String endpoint = "http://localhost:8081/actuator/health";
		BreResponseDto breResponseDto = breOutboundPort.buscaDadosBre(endpoint);

		loggerOutboundPort.info(new LoggerDto(RuleUserCase.class, breResponseDto.toString()));

		String codigo = "123456";
		ComissaoDto comissaoDto = comissaoOutboundPort.findByComissao(codigo);
		loggerOutboundPort.info(new LoggerDto(RuleUserCase.class, comissaoDto.getId().toString()));
		//loggerOutboundPort.error(new LoggerDto(RuleUserCase.class, new Exception()));
	}

	@Override
	public void executeRuleAcaoCivil(Map<String, Object> json) {
		// TODO Auto-generated method stub
		String endpoint = "http://localhost:8081/actuator/health";
		BreResponseDto breResponseDto = breOutboundPort.buscaDadosBre(endpoint);
		loggerOutboundPort.info(new LoggerDto(RuleUserCase.class, breResponseDto.toString()));

		String codigo = "654321";
		ComissaoDto comissaoDto = comissaoOutboundPort.findByComissao(codigo);
		loggerOutboundPort.info(new LoggerDto(RuleUserCase.class, comissaoDto.getCodigo().toString()));
		//loggerOutboundPort.error(new LoggerDto(RuleUserCase.class, new Exception()));
	}

}
