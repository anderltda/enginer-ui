package br.com.enginer.infrastructure.adapter.inbound.audit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ActionLoggerInboundAuditAspect {
	
	private static final Map<String, String> TYPES = new HashMap<>();

	private final ActionLoggerRepositoryOutboundAdapterPort actionLoggerRepositoryOutboundAdapterPort;
    private final RequestContextProvider requestContext;     // ip/requestId
    private final UserContextResolver userResolver;          // headers X-Username, etc (opcional)
    private final ObjectMapper objectMapper;

    /**
     * @param actionLoggerRepositoryOutboundAdapterPort
     * @param requestContext
     * @param userResolver
     * @param objectMapper
     */
    public ActionLoggerInboundAuditAspect(
			ActionLoggerRepositoryOutboundAdapterPort actionLoggerRepositoryOutboundAdapterPort,
			RequestContextProvider requestContext, 
			UserContextResolver userResolver, 
			ObjectMapper objectMapper) {
		super();
		this.actionLoggerRepositoryOutboundAdapterPort = actionLoggerRepositoryOutboundAdapterPort;
		this.requestContext = requestContext;
		this.userResolver = userResolver;
		this.objectMapper = objectMapper;
	}
    
	static {
		
		TYPES.put("create_salvar", "CREATE");
		TYPES.put("update_salvar", "UPDATE");
		
		TYPES.put("create_salvarLista", "CREATE");
		TYPES.put("update_salvarLista", "UPDATE");
		
		TYPES.put("create_excluir", "DELETE");
		TYPES.put("create_excluirLista", "DELETE");
		TYPES.put("update_excluir", "DELETE");
		TYPES.put("update_excluirLista", "DELETE");
		
	}

    /**
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* br.com.enginer.infrastructure.adapter.inbound.api.ActionInboundAdapterPort.action(..))")
    public Object auditActionEndpoint(ProceedingJoinPoint pjp) throws Throwable {
        
    	long start = System.currentTimeMillis();
    	
        ObjectNode origens = null;
        Object result = null;
        Throwable error = null;

    	HttpServletRequest request = requestContext.currentRequest().orElse(null);

    	Object[] args = pjp.getArgs();
    	
        Domain<?> domain = (args != null && args.length > 0 && args[0] instanceof Domain<?> d) ? d : null;
        
        JsonNode json = (args != null && args.length > 1 && args[1] instanceof JsonNode j) ? j : null;
        
        ActionLogger actionLogger = extractFrontLogger(json);

        String userAgent = request.getHeader("User-Agent");

        String username = userResolver.username(request).orElse(null);
        String userId = userResolver.userId(request).orElse(null);

        String source = requestContext.source(request).orElse("WEB");
        String ip = requestContext.ipAddress(request);
        String url = requestContext.uiUrl(request);
        Boolean isModal = requestContext.isModal(request);
        Boolean isDisabled = requestContext.isDisabled(request);
        UUID requestId = requestContext.requestId(request);
        
        String domainName = actionLogger.getDomain();
        String action     = actionLogger.getAction();
        String domainId   = actionLogger.getDomainId();
        
        if (json instanceof ObjectNode original) {
            origens = original.deepCopy();
            origens.remove("actionLogger");
        }
        
        try {
        	
            result = pjp.proceed();
            
            return result;
            
        } catch (Throwable t) {
        	
            error = t;
            
            throw t;
            
        } finally {
        	
			try {
				
				long duration = System.currentTimeMillis() - start;
				boolean success = (error == null);

				ActionLogger logger = new ActionLogger();
				
				String actionType = resolveActionType(domainId, actionLogger);

				String resultId = tryExtractIdFromResponse(result);
				
				if(domainId == null) {
					domainId = resultId;
				}

				logger.setDomain(domainName == null ? domain.getClass().getSimpleName() : domainName);
				logger.setAction(action);
				logger.setModal(isModal);
				logger.setUrl(url);
				logger.setDisabled(isDisabled);
				logger.setUsername(username);
				logger.setUserId(userId);
				logger.setSource(source);
				logger.setIpAddress(ip);
				logger.setUserAgent(userAgent);
				logger.setRequestId(requestId);
				logger.setDurationMs(duration);
				logger.setSuccess(success);
				logger.setErrorMessage(success ? null : safeErrorMessage(error));
				logger.setDomainId(domainId);
				logger.setType(actionType);
				logger.setOldValue(origens); // payload original do front (JSONB)
				logger.setNewValue(result); // payload novo retorno

				actionLoggerRepositoryOutboundAdapterPort.save(logger);
				
			} catch (Exception ignored) {
			}
        }
    }

    private ActionLogger extractFrontLogger(JsonNode json) {
        try {
            if (json != null && json.has("actionLogger") && !json.get("actionLogger").isNull()) {
                return objectMapper.convertValue(json.get("actionLogger"), ActionLogger.class);
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String resolveActionType(String domainId, ActionLogger actionLogger) {
        // Se não tiver, pode inferir por actionName (create/update/delete).
    	String fallback = "UNKNOWN";
        try {
            
        	String at = (actionLogger == null) ? null : actionLogger.getAction();
            
        	String atWithId = (domainId == null) ? "create_" + at : "update_" + at; 
			
			fallback = TYPES.get(atWithId);
			
			if(fallback == null) {
				fallback = "UNKNOWN";
			}
			
			return fallback;

        } catch (Exception ignored) {}

        return fallback;
    }

    /**
     * @param result
     * @return
     */
    private String tryExtractIdFromResponse(Object result) {
        try {
            if (result instanceof ResponseEntity<?> re) {
                Object body = re.getBody();
                if (body instanceof Domain<?> d && !d.isIdNull()) {
                    return String.valueOf(d.getId());
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * @param t
     * @return
     */
    private String safeErrorMessage(Throwable t) {
        String msg = t.getMessage();
        if (msg == null || msg.isBlank()) msg = t.getClass().getName();
        return msg.length() > 2000 ? msg.substring(0, 2000) : msg;
    }
}