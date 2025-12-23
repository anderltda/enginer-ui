package br.com.enginer.infrastructure.adapter.inbound.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.enginer.domain.system.usecase.annotation.instance.UIDomain;
import br.com.enginer.domain.system.usecase.port.inbound.api.UIInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.schema.Form;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;
import br.com.enginer.infrastructure.adapter.outbound.repository.RepositoryOutboundAdapterPort;

/**
 * 
 */
@RestController
@RequestMapping("/v1/enginer-ui/module")
public class UIInboundAdapterPort {

	private final UIInboundPort<Domain<?>> uIInboundPort;
	private final LoggerOutboundPort logger;

	/**
	 * @param uIInboundPort
	 * @param logger
	 */
	public UIInboundAdapterPort(UIInboundPort<Domain<?>> uIInboundPort, LoggerOutboundPort logger) {
		this.uIInboundPort = uIInboundPort;
		this.logger = logger;
	}

	/**
	 * @param domain
	 * @return ResponseEntity<Form>
	 * @throws Exception
	 */
	@GetMapping({ "/tab", "/tab/{id}" })
	public ResponseEntity<Form> tab(@UIDomain Domain<?> domain) throws Exception {
		
		long start = System.currentTimeMillis();
		
		ResponseEntity<Form> response;
		
		try {
		
			Form form = uIInboundPort.tab(domain);
		
			response = ResponseEntity.ok(form);
		
		} catch (Exception ex) {
			logger.error(UIInboundAdapterPort.class, "Erro ao criar [template-tab]", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[template-tab] concluído em " + duration + "ms ");
		}
		
		long duration = System.currentTimeMillis() - start;
		
		logger.info(UIInboundAdapterPort.class, "[template-tab] processado em " + duration + "ms ");
		
		return response;
	}

	/**
	 * @param domain
	 * @return ResponseEntity<Form>
	 * @throws Exception
	 */
	@GetMapping({ "/row", "/row/{id}" })
	public ResponseEntity<Form> row(@UIDomain Domain<?> domain) throws Exception {
		
		long start = System.currentTimeMillis();
		
		ResponseEntity<Form> response;
		
		try {
			
			Form form = uIInboundPort.row(domain);
			
			response = ResponseEntity.ok(form);
			
		} catch (Exception ex) {
			logger.error(UIInboundAdapterPort.class, "Erro ao criar [template-row]", ex);
			throw ex;
		} finally {
			long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[template-row] concluído em " + duration + "ms ");
		}
		
		long duration = System.currentTimeMillis() - start;
		
		logger.info(UIInboundAdapterPort.class, "[template-row] processado em " + duration + "ms ");
		
		return response;
	}	


	/**
	 * @param domain
	 * @return ResponseEntity<Form>
	 * @throws Exception
	 */
	@GetMapping({ "/form", "/form/{id}" })
	public ResponseEntity<Form> form(@UIDomain Domain<?> domain) throws Exception {
		
		long start = System.currentTimeMillis();
		
		ResponseEntity<Form> response;
		
		try {
			
			Form form = uIInboundPort.form(domain);
			
			response = ResponseEntity.ok(form);
			
		} catch (Exception ex) {
			logger.error(UIInboundAdapterPort.class, "Erro ao criar [template-form]", ex);
			throw ex;
		} finally {
			long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[template-form] concluído em " + duration + "ms ");
		}
		
		long duration = System.currentTimeMillis() - start;
		
		logger.info(UIInboundAdapterPort.class, "[template-form] processado em " + duration + "ms ");
		
		return response;
	}
	
	/**
	 * @param domain
	 * @return ResponseEntity<Form>
	 * @throws Exception
	 */
	@GetMapping({ "/filter", "/filter/{id}" })
	public ResponseEntity<Form> filter(@UIDomain Domain<?> domain) throws Exception {
		
		long start = System.currentTimeMillis();
		
		ResponseEntity<Form> response;
		
		try {
			
			Form form = uIInboundPort.filter(domain);
			
			response = ResponseEntity.ok(form);
			
		} catch (Exception ex) {
			logger.error(UIInboundAdapterPort.class, "Erro ao criar [template-filter]", ex);
			throw ex;
		} finally {
			long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[template-filter] concluído em " + duration + "ms ");
		}
		
		long duration = System.currentTimeMillis() - start;
		
		logger.info(UIInboundAdapterPort.class, "[template-filter] processado em " + duration + "ms ");
		
		return response;
	}
}