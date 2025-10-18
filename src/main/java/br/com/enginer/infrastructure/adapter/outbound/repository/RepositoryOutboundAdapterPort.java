package br.com.enginer.infrastructure.adapter.outbound.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.port.outbound.LoggerOutboundPort;
import br.com.enginer.domain.ui.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.exception.GlobalWebClientErrorHandler;
import br.com.enginer.infrastructure.utils.UriUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 */
@Component
public class RepositoryOutboundAdapterPort implements RepositoryOutboundPort {

	private final String dataSourceBasePath;
	private final LoggerOutboundPort logger;
	private final ObjectMapper objectMapper;

	/**
	 * @param dataSourceBasePath
	 * @param logger
	 * @param objectMapper
	 */
	public RepositoryOutboundAdapterPort(@Value("${datasource.api}") String dataSourceBasePath, LoggerOutboundPort logger, ObjectMapper objectMapper) {
		this.logger = logger;
		this.objectMapper = objectMapper;
		this.dataSourceBasePath = dataSourceBasePath;
	}

	/**
	 * @return
	 */
	private WebClient getWebClient() {
		return WebClient
				.builder()
				.baseUrl(dataSourceBasePath)
				.defaultHeader(
						"Authorization", "SECRET_TOKEN", 
						"Content-Type", MediaType.APPLICATION_JSON_VALUE, 
						"Accept", MediaType.APPLICATION_JSON_VALUE)
		        .codecs(cfg -> {
		            cfg.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
		            cfg.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
		        })				
				.build();
	}

	/**
	 *
	 */
	@Override
	public Domain<?> findById(Domain<?> domain, Object id) throws UncheckedException {

		Object object = null;

		try {

			String uri = UriUtils.buildUriId(domain);

			Mono<?> mono = getWebClient()
					.get()
					.uri(uri, id)
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(ParameterizedTypeReference.forType(domain.getClass()))
					.switchIfEmpty(Mono.error(new CheckedException("Nenhum registro encontrado para ID: " + id)));

			object = mono.block();

		} catch (CheckedException ex) {
			if (ex.getMessage().contains("Nenhum registro encontrado")) {
				logger.info(RepositoryOutboundAdapterPort.class, ex.getMessage());
				return domain;
			} else {
				logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
				throw ex;
			}
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return (Domain<?>) object;

	}
	
	/**
	 *
	 */
	@Override
	public Domain<?> findByIdComposite(Domain<?> domain, Map<String, Object> ids) throws UncheckedException {
		
		Object object = null;

		try {

			String uri = UriUtils.buildUriIdComposite(domain);

			Mono<?> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, ids))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(ParameterizedTypeReference.forType(domain.getClass()))
					.switchIfEmpty(Mono.error(new CheckedException("Nenhum registro encontrado para esses ID(s): " + ids)));

			object = mono.block();

		} catch (CheckedException ex) {
			if (ex.getMessage().contains("Nenhum registro encontrado")) {
				logger.info(RepositoryOutboundAdapterPort.class, ex.getMessage());
				return domain;
			} else {
				logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
				throw ex;
			}
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return (Domain<?>) object;
	}

	/**
	 *
	 */
	@Override
	public Domain<?> findBySingle(Domain<?> domain, Map<String, Object> filter, String... method) throws UncheckedException {

		Object object = null;

		try {

			String uri = UriUtils.buildUriSingle(domain, filter, method);

			Mono<?> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(ParameterizedTypeReference.forType(domain.getClass()));

			object = mono.block();

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return (Domain<?>) object;
	}

	/**
	 *
	 */
	@Override
	public Domain<?> findBySingle(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {

		Object object = null;

		try {

			String uri = File.separator + typeRepository.getValue() + File.separator + File.separator + "single" + File.separator + domain.getClass().getSimpleName() + File.separator + queryName;

			Mono<?> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(ParameterizedTypeReference.forType(domain.getClass()));

			object = mono.block();

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return (Domain<?>) object;
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Domain<?>> findAll(Domain<?> domain, Map<String, Object> filter, String... method) throws UncheckedException {

		List<Domain<?>> list = new ArrayList<>();

		try {

			String uri = UriUtils.buildUriFindAll(domain, filter, method);

			Flux<?> flux = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri.toString(), filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToFlux(ParameterizedTypeReference.forType(domain.getClass()))
					.buffer();

			Iterator<List<?>> iterator = (Iterator<List<?>>) flux.toIterable().iterator();

			while (iterator.hasNext()) {
				for (Object object : iterator.next()) {
					list.add((Domain<?>) object);
				}
			}

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return (List<Domain<?>>) list;
	}
	
	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Domain<?>> findAll(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {

		List<Domain<?>> list = new ArrayList<>();

		try {

			String uri = File.separator + typeRepository.getValue() + File.separator + domain.getClass().getSimpleName() + File.separator + queryName;

			Flux<?> flux = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri.toString(), filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToFlux(ParameterizedTypeReference.forType(domain.getClass()))
					.buffer();

			Iterator<List<?>> iterator = (Iterator<List<?>>) flux.toIterable().iterator();

			while (iterator.hasNext()) {
				for (Object object : iterator.next()) {
					list.add((Domain<?>) object);
				}
			}

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return (List<Domain<?>>) list;
	}

	/**
	 *
	 */
	public List<Domain<?>> findAllById(Domain<?> domain, List<?> ids) throws UncheckedException {
		return findAllById(domain, ids.toArray());
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Domain<?>> findAllById(Domain<?> domain, Object... id) throws UncheckedException {

		List<Domain<?>> list = new ArrayList<>();

		try {
			
			String idsString = Arrays.stream(id)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

			String uri = File.separator + domain.getClass().getSimpleName() + File.separator + "ids" + File.separator + idsString;

			Flux<?> flux = getWebClient()
					.get()
					.uri(uri)
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToFlux(ParameterizedTypeReference.forType(domain.getClass()))
					.buffer();

			Iterator<List<?>> iterator = (Iterator<List<?>>) flux.toIterable().iterator();

			while (iterator.hasNext()) {
				for (Object object : iterator.next()) {
					list.add((Domain<?>) object);
				}
			}

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return (List<Domain<?>>) list;
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageResult<Domain<?>> paginator(Domain<?> domain, Map<String, Object> filter, String... method) throws UncheckedException {

		PageResult<Domain<?>> result = null;

		try {

			String uri = UriUtils.buildUriPaginator(domain, filter, method);

			ParameterizedTypeReference<PageResult<?>> typeRef = new ParameterizedTypeReference<>() {};

			PageResult<?> pageResult = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(typeRef)
					.block();
			
			if (pageResult != null) {

				List<Domain<?>> entities = (List<Domain<?>>) pageResult.getContent().stream()
						.map(o -> objectMapper.convertValue(o, domain.getClass())).toList();
				
				result = new PageResult<>();
				result.setPage(pageResult.getPage());
				result.setContent(entities);
			}

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return result;
	}
	
	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageResult<Domain<?>> paginator(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {

		PageResult<Domain<?>> result = null;

		try {

			String uri = File.separator + typeRepository.getValue() + File.separator + File.separator + "paginator" + File.separator + domain.getClass().getSimpleName() + File.separator + queryName;

			ParameterizedTypeReference<PageResult<?>> typeRef = new ParameterizedTypeReference<>() {};

			PageResult<?> pageResult = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(typeRef)
					.block();
			
			if (pageResult != null) {

				List<Domain<?>> entities = (List<Domain<?>>) pageResult.getContent().stream()
						.map(o -> objectMapper.convertValue(o, domain.getClass())).toList();
				
				result = new PageResult<>();
				result.setPage(pageResult.getPage());
				result.setContent(entities);
			}			

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return result;
	}

	/**
	 *
	 */
	@Override
	public Integer count(Domain<?> domain, Map<String, Object> filter, String... method) throws UncheckedException {

		Integer count = 0;

		try {

			String uri = UriUtils.buildUriCount(domain, filter, method);

			Mono<Integer> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(Integer.class);

			count = mono.block();

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return count;
	}

	/**
	 *
	 */
	@Override
	public Integer count(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		
		Integer count = 0;

		try {
			
			String uri = File.separator + typeRepository.getValue() + File.separator + File.separator + "count" + File.separator + domain.getClass().getSimpleName() + File.separator + queryName;

			Mono<Integer> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(Integer.class);

			count = mono.block();

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return count;
	}
	
	/**
	 *
	 */
	@Override
	public boolean existsById(Domain<?> domain, Object id) throws UncheckedException {

		Boolean exist = false;

		try {
			
			String uri = File.separator + "exist" + File.separator + domain.getClass().getSimpleName() + File.separator + "{id}";

			Mono<Boolean> mono = getWebClient()
					.get()
					.uri(uri, id)
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(Boolean.class);

			exist = mono.block();

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return exist;
	}
	
	/**
	 *
	 */
	@Override
	public void delete(Domain<?> domain, Object id) throws UncheckedException {

		try {

			String uri = UriUtils.buildUriId(domain);

			getWebClient()
				.delete()
				.uri(uri, id)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
				.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
				.toBodilessEntity()
				.block();

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[CheckedException] " + ex.getMessage(), ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - " + ex.getStatusText(),
					ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}
	}
	
	/**
	 *
	 */
	@Override
	public void delete(Domain<?> domain, Map<String, Object> ids) throws UncheckedException {
		
		try {

			String uri = UriUtils.buildUriIdComposite(domain);

			getWebClient()
				.delete()
				.uri(UriUtils.buildUriWithQueryParams(uri, ids))
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
				.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
				.toBodilessEntity()
				.block();

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[CheckedException] " + ex.getMessage(), ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - " + ex.getStatusText(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}
	}

	/**
	 *
	 */
	@Override
	public Domain<?> save(Domain<?> domain, Boolean... flush) throws UncheckedException {

		try {

			String uri = UriUtils.buildUriSave(domain, flush);

			String json = objectMapper.writeValueAsString(domain);

			Mono<?> mono = getWebClient()
					.post()
					.uri(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(json)
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(domain.getClass());

			domain = (Domain<?>) mono.block();

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}

		return domain;
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Domain<?>> save(List<Domain<?>> entities, Boolean... flush) throws UncheckedException {

		try {

			List<Domain<?>> savedList = new ArrayList<>();
			
			Domain<?> domain = (Domain<?>) entities.getFirst();
			
			String uri = UriUtils.buildUriSaveAll(domain, flush);

			String json = objectMapper.writeValueAsString(entities);

			Flux<?> flux = getWebClient()
				.post()
				.uri(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(json)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
				.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
				.bodyToFlux(ParameterizedTypeReference.forType(domain.getClass()))
				.buffer();

			Iterator<List<?>> iterator = (Iterator<List<?>>) flux.toIterable().iterator();

			while (iterator.hasNext()) {
				for (Object object : iterator.next()) {
					savedList.add((Domain<?>) object);
				}
			}
			
			if(savedList.size() > 0) {
				entities.clear();
				entities.addAll(savedList);
			}

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[CheckedException] " + ex.getMessage(), ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - " + ex.getStatusText(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro inesperado] - " + ex.getMessage(), ex);
			throw new UncheckedException("[Erro inesperado]", ex);
		}
		
		return entities;
	}
}
