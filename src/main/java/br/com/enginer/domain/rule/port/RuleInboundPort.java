package br.com.enginer.domain.rule.port;

import java.util.Map;

public interface RuleInboundPort {
	
	public void executeRuleFraude(Map<String, Object> json);
	
	public void executeRuleAcaoCivil(Map<String, Object> json);

}
