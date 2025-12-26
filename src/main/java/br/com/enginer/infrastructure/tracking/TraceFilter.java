package br.com.enginer.infrastructure.tracking;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.enginer.infrastructure.configuration.DomainResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
@Component
public class TraceFilter extends OncePerRequestFilter {

    /**
     *
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        try {
            
            
            String url = request.getHeader("X-UI-Url");
            String modal = request.getHeader("X-UIModal");
            String domain = request.getHeader("X-UIDomain");
            String requestId = request.getHeader("X-Request-Id");
            String username = request.getHeader("X-Username");
            String userid = request.getHeader("X-User-Id");
            String mobile = request.getHeader("X-Is-Mobile");
            String mode = request.getHeader("X-UI-Mode");
            Boolean isDisabled = DomainResolver.parseDisabled(mode);

            MDC.put("url", url);
            MDC.put("domain", domain);
            MDC.put("requestId", requestId);
            MDC.put("username", username);
            MDC.put("userid", userid);
            MDC.put("modal", modal);
            MDC.put("disabled", isDisabled.toString());
            MDC.put("mobile", mobile);
            
            System.out.println("MDC TRACE FILTER: " + MDC.getCopyOfContextMap());

            filterChain.doFilter(request, response);
            
        } finally {
            MDC.clear();
        }
    }
}