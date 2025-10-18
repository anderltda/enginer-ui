package br.com.enginer.infrastructure.tracking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TrackingLogConfigurer {

	private static final Logger LOGGER = LogManager.getLogger(TrackingLogConfigurer.class);

	private static final String KEY_INNER_ID = "innerId";

	private static final String KEY_CID = "cid";

	@PostConstruct
	public void configure() {
		TrackingProvider.getInstance().addInnerIdObserver(this::setInnerId);
		TrackingProvider.getInstance().addCidObserver(this::setCid);
	}

	public void setInnerId(String innerId) {
		MDC.put(KEY_INNER_ID, innerId);
		LOGGER.trace("InnerId {} setted", innerId);
	}

	public void setCid(String cid) {
		MDC.put(KEY_CID, cid);
		LOGGER.trace("Cid {} setted", cid);
	}

}
