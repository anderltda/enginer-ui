package br.com.enginer.domain.bre.dto;

import java.util.List;

public class BreResponseDto {

	private String conclusion;
	private String analysisDate;
	private String derivacaoParceiro;
	private List<String> reasonCodes;

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getAnalysisDate() {
		return analysisDate;
	}

	public void setAnalysisDate(String analysisDate) {
		this.analysisDate = analysisDate;
	}

	public String getDerivacaoParceiro() {
		return derivacaoParceiro;
	}

	public void setDerivacaoParceiro(String derivacaoParceiro) {
		this.derivacaoParceiro = derivacaoParceiro;
	}

	public List<String> getReasonCodes() {
		return reasonCodes;
	}

	public void setReasonCodes(List<String> reasonCodes) {
		this.reasonCodes = reasonCodes;
	}

}
