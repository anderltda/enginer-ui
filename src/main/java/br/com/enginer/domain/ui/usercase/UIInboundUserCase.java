package br.com.enginer.domain.ui.usercase;

import br.com.enginer.domain.ui.port.inbound.UIInboundPort;
import br.com.enginer.domain.ui.port.outbound.LoggerOutboundPort;
import br.com.enginer.domain.ui.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.schema.Form;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.domain.ui.usercase.utils.ReflectionUtils;

/**
 * 
 */
public class UIInboundUserCase implements UIInboundPort {

	private final LoggerOutboundPort logger;
	private final RepositoryOutboundPort repositoryOutboundPort;

	/**
	 * @param logger
	 * @param repositoryOutboundPort
	 */
	public UIInboundUserCase(LoggerOutboundPort logger, RepositoryOutboundPort repositoryOutboundPort) {
		this.logger = logger;
		this.repositoryOutboundPort = repositoryOutboundPort;
	}
	
	/**
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	private Object injectedDependency(Domain<?> domain) throws Exception {
		return ReflectionUtils.executeInjectedDependencyUserCase(domain.getClass(), repositoryOutboundPort);
	}

	/**
	 *
	 */
	@Override
	public Form form(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUserCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUserCase, "form", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUserCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 *
	 */
	@Override
	public Form tab(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUserCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUserCase, "tab", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUserCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	public Form filter(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUserCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUserCase, "filter", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUserCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */	
	@Override
	public Form row(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUserCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUserCase, "row", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUserCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
}
