package br.com.enginer.infrastructure.tracking;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
        	
            String traceId = java.util.UUID.randomUUID().toString();
            
            String user = request.getHeader("X-User") != null ? request.getHeader("X-User") : "anderson";

            MDC.put("traceId", traceId);
            MDC.put("user", user);

            filterChain.doFilter(request, response);
            
        } finally {
            MDC.clear();
        }
    }
}