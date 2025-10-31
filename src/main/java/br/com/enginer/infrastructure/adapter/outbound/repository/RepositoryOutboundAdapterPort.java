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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.enginer.domain.system.usercase.exception.CheckedException;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.page.PageResult;
import br.com.enginer.domain.system.usercase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.exception.GlobalWebClientErrorHandler;
import br.com.enginer.infrastructure.utils.UriUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementação do RepositoryOutboundPort<T> usando WebClient.
 * Totalmente tipado com segurança de tipo (sem Domain<?>).
 */
@Component
public class RepositoryOutboundAdapterPort<T extends Domain<?>> implements RepositoryOutboundPort<T> {

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
		            cfg.defaultCodecs().maxInMemorySize(10 * 1024 * 1024);
		        })				
				.build();
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T findById(T domain, Object id) throws UncheckedException {

	    long start = System.currentTimeMillis();
	    
		T object = null;

		try {

			String uri = UriUtils.buildUriId(domain);
	        logger.info(RepositoryOutboundAdapterPort.class, "[findById] URI: " + uri);			

			Mono<T> mono = getWebClient()
					.get()
					.uri(uri, id)
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(ParameterizedTypeReference.forType(domain.getClass()))
                    .cast((Class<T>) domain.getClass())
					.switchIfEmpty(Mono.error(new CheckedException("Nenhum registro encontrado para ID: " + id)));

			object = mono.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[findById] concluído em " + duration + "ms ");
			

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
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro busca por id] - " + ex.getMessage(), ex);
			throw new UncheckedException(ex.getMessage(), ex);
		}

		return object;

	}
	
	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T findByIdComposite(T domain, Map<String, Object> ids) throws UncheckedException {
		
	    long start = System.currentTimeMillis();
	    
		T object = null;

		try {

			String uri = UriUtils.buildUriIdComposite(domain);
			
	        logger.info(RepositoryOutboundAdapterPort.class, "[findByIdComposite] URI: " + uri);			

			Mono<T> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, ids))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(ParameterizedTypeReference.forType(domain.getClass()))
					.cast((Class<T>) domain.getClass())
					.switchIfEmpty(Mono.error(new CheckedException("Nenhum registro encontrado para esses ID(s): " + ids)));

			object = mono.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[findByIdComposite] concluído em " + duration + "ms ");

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
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro busca por id composto] - " + ex.getMessage(), ex);
			throw new UncheckedException(ex.getMessage(), ex);
		}

		return object;
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T findBySingle(T domain, Map<String, Object> filter, String... method) throws UncheckedException {

	    long start = System.currentTimeMillis();
	    
		T object = null;

		try {

			String uri = UriUtils.buildUriSingle(domain, filter, method);
			
	        logger.info(RepositoryOutboundAdapterPort.class, "[findBySingle] URI: " + uri);		

			Mono<T> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(ParameterizedTypeReference.forType(domain.getClass()))
					.cast((Class<T>) domain.getClass())
					.switchIfEmpty(Mono.error(new CheckedException("Nenhum registro encontrado para esse(s) filtro(s): " + filter)));

			object = mono.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[findBySingle] concluído em " + duration + "ms ");

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro busca por unico valor filtrado] - " + ex.getMessage(), ex);
			throw new UncheckedException(ex.getMessage(), ex);
		}

		return object;
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T findBySingle(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {

	    long start = System.currentTimeMillis();
	    
		T object = null;

		try {

			String uri = File.separator + typeRepository.getValue() + File.separator + File.separator + "single" + File.separator + domain.getClass().getSimpleName() + File.separator + queryName;

	        logger.info(RepositoryOutboundAdapterPort.class, "[findBySingle] URI: " + uri);		
	        
			Mono<T> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(ParameterizedTypeReference.forType(domain.getClass()))
					.cast((Class<T>) domain.getClass())
					.switchIfEmpty(Mono.error(new CheckedException("Nenhum registro encontrado para esse(s) filtro(s): " + filter)));

			object = mono.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[findBySingle] concluído em " + duration + "ms ");			

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro busca por unico valor filtrado e com queryName] - " + ex.getMessage(), ex);
			throw new UncheckedException(ex.getMessage(), ex);
		}

		return object;
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(T domain, Map<String, Object> filter, String... method) throws UncheckedException {

	    long start = System.currentTimeMillis();
	    
	    List<T> list = new ArrayList<>();

		try {

			String uri = UriUtils.buildUriFindAll(domain, filter, method);
			
			logger.info(RepositoryOutboundAdapterPort.class, "[findAll] URI: " + uri);	

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
					list.add((T) object);
				}
			}
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[findAll] concluído em " + duration + "ms ");

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro busca pelo findAll] - " + ex.getMessage(), ex);
			throw new UncheckedException(ex.getMessage(), ex);
		}

		return list;
	}
	
	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {

	    long start = System.currentTimeMillis();
	    
		List<T> list = new ArrayList<>();

		try {

			String uri = File.separator + typeRepository.getValue() + File.separator + domain.getClass().getSimpleName() + File.separator + queryName;

			logger.info(RepositoryOutboundAdapterPort.class, "[findAll] URI: " + uri);
			
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
					list.add((T) object);
				}
			}
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[findAll] concluído em " + duration + "ms ");

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro busca pelo findAll com queryName] - " + ex.getMessage(), ex);
			throw new UncheckedException(ex.getMessage(), ex);
		}

		return list;
	}

	/**
	 *
	 */
	public List<T> findAllById(T domain, List<?> ids) throws UncheckedException {
		return findAllById(domain, ids.toArray());
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllById(T domain, Object... id) throws UncheckedException {
		
	    long start = System.currentTimeMillis();

		List<T> list = new ArrayList<>();

		try {
			
			String idsString = Arrays.stream(id).map(Object::toString).collect(Collectors.joining(","));

			String uri = File.separator + domain.getClass().getSimpleName() + File.separator + "ids" + File.separator + idsString;
			
			logger.info(RepositoryOutboundAdapterPort.class, "[findAllById] URI: " + uri);

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
					list.add((T) object);
				}
			}
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[findAllById] concluído em " + duration + "ms ");

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro findAllById] - " + ex.getMessage(), ex);
			throw new UncheckedException(ex.getMessage(), ex);
		}

		return list;
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageResult<T> paginator(T domain, Map<String, Object> filter, String... method) throws UncheckedException {
		
	    long start = System.currentTimeMillis();
	    
	    PageResult<T> result = null;

	    try {
	    	
	        String uri = UriUtils.buildUriPaginator(domain, filter, method);
	        
	        logger.info(RepositoryOutboundAdapterPort.class, "[paginator] URI: " + uri);

	        ParameterizedTypeReference<PageResult<JsonNode>> typeRef = new ParameterizedTypeReference<>() {};
	        
	        PageResult<JsonNode> rawPage = getWebClient()
	                .get()
	                .uri(UriUtils.buildUriWithQueryParams(uri, filter))
	                .retrieve()
	                .onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
	                .onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
	                .bodyToMono(typeRef)
	                .block();

	        if (rawPage != null) {
	        	
	        	result = new PageResult<>();
	            
	        	List<T> content = rawPage.getContent().stream().map(node -> objectMapper.convertValue(node, (Class<T>) domain.getClass())).toList();

	            result.setContent(content);
	            result.setPage(rawPage.getPage());
	        }

	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[Paginator] concluído em " + duration + "ms - total itens: " + (result != null && result.getContent() != null ? result.getContent().size() : 0));

	    } catch (CheckedException ex) {
	        logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
	        throw ex;
	    } catch (WebClientResponseException ex) {
	        logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
	        throw new UncheckedException("[WebClientResponseException]", ex);
	    } catch (Exception ex) {
	        logger.error(RepositoryOutboundAdapterPort.class, "[Erro ao paginar] - " + ex.getMessage(), ex);
	        throw new UncheckedException(ex.getMessage(), ex);
	    }

	    return result;
	}
	
	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageResult<T> paginator(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {

	    long start = System.currentTimeMillis();
	    
	    PageResult<T> result = null;

		try {

			String uri = File.separator + typeRepository.getValue() + File.separator + File.separator + "paginator" + File.separator + domain.getClass().getSimpleName() + File.separator + queryName;

	        logger.info(RepositoryOutboundAdapterPort.class, "[paginator] URI: " + uri);
	        
	        ParameterizedTypeReference<PageResult<JsonNode>> typeRef = new ParameterizedTypeReference<>() {};

	        PageResult<JsonNode> rawPage = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(typeRef)
					.block();
			
	        if (rawPage != null) {
	        	
	        	result = new PageResult<>();

	            List<T> content = rawPage.getContent().stream().map(node -> objectMapper.convertValue(node, (Class<T>) domain.getClass())).toList();

	            result.setContent(content);
	            result.setPage(rawPage.getPage());
	        }

	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[Paginator] concluído em " + duration + "ms - total itens: " + (result != null && result.getContent() != null ? result.getContent().size() : 0));

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro ao paginar com queryName] - " + ex.getMessage(), ex);
			throw new UncheckedException(ex.getMessage(), ex);
		}

		return result;
	}

	/**
	 *
	 */
	@Override
	public Integer count(T domain, Map<String, Object> filter, String... method) throws UncheckedException {
		
	    long start = System.currentTimeMillis();

		Integer count = 0;

		try {

			String uri = UriUtils.buildUriCount(domain, filter, method);
			
	        logger.info(RepositoryOutboundAdapterPort.class, "[count] URI: " + uri);

			Mono<Integer> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(Integer.class);

			count = mono.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[count] concluído em " + duration + "ms ");

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
            logger.error(getClass(), "[Erro ao contar registros] - " + ex.getMessage(), ex);
            throw new UncheckedException(ex.getMessage(), ex);
		}

		return count;
	}

	/**
	 *
	 */
	@Override
	public Integer count(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		
	    long start = System.currentTimeMillis();
		
		Integer count = 0;

		try {
			
			String uri = File.separator + typeRepository.getValue() + File.separator + File.separator + "count" + File.separator + domain.getClass().getSimpleName() + File.separator + queryName;

	        logger.info(RepositoryOutboundAdapterPort.class, "[count] URI: " + uri);
	        
			Mono<Integer> mono = getWebClient()
					.get()
					.uri(UriUtils.buildUriWithQueryParams(uri, filter))
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(Integer.class);

			count = mono.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[count] concluído em " + duration + "ms ");

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
            logger.error(getClass(), "[Erro ao contar registros com queryName] - " + ex.getMessage(), ex);
            throw new UncheckedException(ex.getMessage(), ex);
		}

		return count;
	}
	
	/**
	 *
	 */
	@Override
	public boolean existsById(T domain, Object id) throws UncheckedException {
		
	    long start = System.currentTimeMillis();

		Boolean exist = false;

		try {
			
			String uri = File.separator + "exist" + File.separator + domain.getClass().getSimpleName() + File.separator + "{id}";

	        logger.info(RepositoryOutboundAdapterPort.class, "[existsById] URI: " + uri);
	        
			Mono<Boolean> mono = getWebClient()
					.get()
					.uri(uri, id)
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(Boolean.class);

			exist = mono.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[existsById] concluído em " + duration + "ms ");

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
            logger.error(getClass(), "[Erro ao verificar existência] - " + ex.getMessage(), ex);
            throw new UncheckedException(ex.getMessage(), ex);
		}

		return exist;
	}
	
	/**
	 *
	 */
	@Override
	public void delete(T domain, Object id) throws UncheckedException {
		
	    long start = System.currentTimeMillis();

		try {

			String uri = UriUtils.buildUriId(domain);
			
	        logger.info(RepositoryOutboundAdapterPort.class, "[delete] URI: " + uri);

			getWebClient()
				.delete()
				.uri(uri, id)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
				.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
				.toBodilessEntity()
				.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[delete] concluído em " + duration + "ms ");

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[CheckedException] " + ex.getMessage(), ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - " + ex.getStatusText(),
					ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
            logger.error(getClass(), "[Erro ao excluir registro por id] - " + ex.getMessage(), ex);
            throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 *
	 */
	@Override
	public void delete(T domain, Map<String, Object> ids) throws UncheckedException {
		
	    long start = System.currentTimeMillis();
		
		try {

			String uri = UriUtils.buildUriIdComposite(domain);
			
			logger.info(RepositoryOutboundAdapterPort.class, "[delete] URI: " + uri);

			getWebClient()
				.delete()
				.uri(UriUtils.buildUriWithQueryParams(uri, ids))
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
				.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
				.toBodilessEntity()
				.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.info(RepositoryOutboundAdapterPort.class, "[delete] concluído em " + duration + "ms ");			

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[CheckedException] " + ex.getMessage(), ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - " + ex.getStatusText(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
            logger.error(getClass(), "[Erro ao excluir registro por um map de ids] - " + ex.getMessage(), ex);
            throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T save(T domain, Boolean... flush) throws UncheckedException {
		
	    long start = System.currentTimeMillis();
		
		T result = null;

		try {

			String uri = UriUtils.buildUriSave(domain, flush);
			
			logger.info(RepositoryOutboundAdapterPort.class, "[save] URI: " + uri);

			String json = objectMapper.writeValueAsString(domain);

			Mono<T> mono = getWebClient()
					.post()
					.uri(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(json)
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, GlobalWebClientErrorHandler::handle4xxError)
					.onStatus(HttpStatusCode::is5xxServerError, GlobalWebClientErrorHandler::handle5xxError)
					.bodyToMono(domain.getClass())
                    .cast((Class<T>) domain.getClass());

			result = mono.block();
			
	        long duration = System.currentTimeMillis() - start;
	        logger.audit(RepositoryOutboundAdapterPort.class, domain.getActionLogger(), "Ação [save] concluída com sucesso", duration);

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[4XX or 5XX ERROR]", ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - Status: " + ex.getStatusText() + ", Body: " + ex.getResponseBodyAsString(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
            logger.error(getClass(), "[Erro ao salvar] - " + ex.getMessage(), ex);
            throw new UncheckedException(ex.getMessage(), ex);
		}

		return result;
	}
	

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> save(List<T> entities, Boolean... flush) throws UncheckedException {
		
	    long start = System.currentTimeMillis();
		
        if (entities == null || entities.isEmpty()) return List.of();
        
        T domain = entities.get(0);
        
		List<T> savedList = new ArrayList<>();

		try {
			
			String uri = UriUtils.buildUriSaveAll(domain, flush);
			
			logger.info(RepositoryOutboundAdapterPort.class, "[save] URI: " + uri);

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
				.cast((Class<T>) domain.getClass());

			savedList = (List<T>) flux.collectList().blockOptional().orElse(List.of());	
			
	        long duration = System.currentTimeMillis() - start;
	        logger.audit(RepositoryOutboundAdapterPort.class, domain.getActionLogger(), "Ação [save] concluída com sucesso", duration);

		} catch (CheckedException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[CheckedException] " + ex.getMessage(), ex);
			throw ex;
		} catch (WebClientResponseException ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[WebClientResponseException] - " + ex.getStatusText(), ex);
			throw new UncheckedException("[WebClientResponseException]", ex);
		} catch (Exception ex) {
			logger.error(RepositoryOutboundAdapterPort.class, "[Erro ao salvar uma lista] - " + ex.getMessage(), ex);
            throw new UncheckedException(ex.getMessage(), ex);
		}
		
		return savedList;
	}
}
