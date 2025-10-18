package br.com.enginer.domain.ui.usercase.schema.tab;

import br.com.enginer.domain.ui.usercase.schema.tab.config.Config;

/**
 * 
 */
public class Tab {

	private Config config;

	public Tab(Boolean standaloneStep) {
		config = new Config();
		config.setStandaloneStep(standaloneStep);
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}
