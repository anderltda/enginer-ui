package br.com.enginer.infrastructure.utils;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.web.util.UriBuilder;

import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

/**
 * 
 */
public class UriUtils {

	/**
	 * @param path
	 * @param params
	 * @return
	 */
	public static Function<UriBuilder, URI> buildUriWithQueryParams(String path, Map<String, Object> params) {
		return uriBuilder -> {
			UriBuilder builder = uriBuilder.path(path);

			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (value == null)
					continue;

				if (value instanceof List<?> list) {
					for (Object item : list) {
						if (item != null) {
							builder.queryParam(key, item.toString());
						}
					}
				} else if (value.getClass().isArray()) {
					for (Object item : (Object[]) value) {
						if (item != null) {
							builder.queryParam(key, item.toString());
						}
					}
				} else {
					builder.queryParam(key, value.toString());
				}
			}

			return builder.build();
		};
	}
	
	/**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return
	 */
	public static String buildUriPaginator(Domain<?> domain, Map<String, Object> filter, String... method) {
		
		StringBuilder uri = new StringBuilder();
		uri.append(File.separator).append("paginator").append(File.separator).append(domain.getClass().getSimpleName());

		if(method != null && method.length > 0) {
			StringBuilder uriJpql = new StringBuilder();
			uriJpql.append(File.separator).append("jpql").append(uri.toString()).append(File.separator).append(method[0]);
			filter.entrySet().removeIf(entry -> entry.getKey().endsWith("_op"));
			return uriJpql.toString();
		}
		
		return uri.toString();
	}
	
	/**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return
	 */
	public static String buildUriFindAll(Domain<?> domain, Map<String, Object> filter, String... method) {
		
		StringBuilder uri = new StringBuilder();
		uri.append(File.separator).append(domain.getClass().getSimpleName());

		if(method != null && method.length > 0) {
			StringBuilder uriJpql = new StringBuilder();
			uriJpql.append(File.separator).append("jpql").append(uri.toString()).append(File.separator).append(method[0]);
			filter.entrySet().removeIf(entry -> entry.getKey().endsWith("_op"));
			return uriJpql.toString();
		}
		
		return uri.toString();
	}

	/**
	 * @param domain
	 * @return
	 */
	public static String buildUriId(Domain<?> domain) {
		
		StringBuilder uri = new StringBuilder();
		
		if(domain.getClass().getSimpleName().endsWith("Data")) {
			uri.append(File.separator).append("mapper").append(File.separator).append("by-id").append(File.separator).append(domain.getClass().getSimpleName()).append(File.separator).append("{id}");
		} else {
			uri.append(File.separator).append(domain.getClass().getSimpleName()).append(File.separator).append("{id}");
		}
		
		return uri.toString();
	}
	
	/**
	 * @param domain
	 * @return
	 */
	public static String buildUriIdComposite(Domain<?> domain) {
		
		StringBuilder uri = new StringBuilder();
		
		if(domain.getClass().getSimpleName().endsWith("Data")) {
			uri.append(File.separator).append("mapper").append(File.separator).append("by-ids").append(File.separator).append(domain.getClass().getSimpleName());
		} else {
			uri.append(File.separator).append(domain.getClass().getSimpleName()).append(File.separator).append("ids");
		}
		
		return uri.toString();
	}	
	
	/**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return
	 */
	public static String buildUriSingle(Domain<?> domain, Map<String, Object> filter, String... method) {
		
		StringBuilder uri = new StringBuilder();
		uri.append(File.separator).append("single").append(File.separator).append(domain.getClass().getSimpleName());
		
		if(method != null && method.length > 0) {
			StringBuilder uriJpql = new StringBuilder();
			uriJpql.append(File.separator).append("jpql").append(uri.toString()).append(File.separator).append(method[0]);
			filter.entrySet().removeIf(entry -> entry.getKey().endsWith("_op"));
			return uriJpql.toString();
		}
		
		return uri.toString();
	}
	
	/**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return
	 */
	public static String buildUriCount(Domain<?> domain, Map<String, Object> filter, String... method) {
		
		StringBuilder uri = new StringBuilder();
		uri.append(File.separator).append("count").append(File.separator).append(domain.getClass().getSimpleName());
		
		if(method != null && method.length > 0) {
			StringBuilder uriJpql = new StringBuilder();
			uriJpql.append(File.separator).append("jpql").append(uri.toString()).append(File.separator).append(method[0]);
			filter.entrySet().removeIf(entry -> entry.getKey().endsWith("_op"));
			return uriJpql.toString();
		}
		
		return uri.toString();
	}
	
	/**
	 * @param domain
	 * @param filter
	 * @param flush
	 * @return
	 */
	public static String buildUriSave(Domain<?> domain, Boolean... flush) {
		
		StringBuilder uri = new StringBuilder();
		uri.append(File.separator).append(domain.getClass().getSimpleName());
		
		if(flush != null && flush.length > 0 && flush[0]) {
			StringBuilder uriFlush = new StringBuilder();
			uriFlush.append(File.separator).append("flush").append(File.separator).append(domain.getClass().getSimpleName());
			return uriFlush.toString();
		}
		
		return uri.toString();
	}
	
	/**
	 * @param domain
	 * @param filter
	 * @param flush
	 * @return
	 */
	public static String buildUriSaveAll(Domain<?> domain, Boolean... flush) {
		
		StringBuilder uri = new StringBuilder();
		uri.append(File.separator).append("all").append(File.separator).append(domain.getClass().getSimpleName());
		
		if(flush != null && flush.length > 0 && flush[0]) {
			StringBuilder uriFlush = new StringBuilder();
			uriFlush.append(File.separator).append("all").append(File.separator).append("flush").append(File.separator).append(domain.getClass().getSimpleName());
			return uriFlush.toString();
		}
		
		return uri.toString();
	}

	/**
	 * @param params
	 * @return
	 */
	public static String toQueryString(Map<String, Object> params) {
		StringBuilder query = new StringBuilder();

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value == null)
				continue;

			if (value instanceof List<?> list) {
				for (Object item : list) {
					if (item != null) {
						query.append(key).append("=").append(item.toString()).append("&");
					}
				}
			} else if (value.getClass().isArray()) {
				for (Object item : (Object[]) value) {
					if (item != null) {
						query.append(key).append("=").append(item.toString()).append("&");
					}
				}
			} else {
				query.append(key).append("=").append(value.toString()).append("&");
			}
		}

		// Remove o último "&" se houver
		if (query.length() > 0) {
			query.setLength(query.length() - 1);
		}

		return query.toString();
	}
}