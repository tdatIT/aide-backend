package com.aide.service.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        Instant startTime = Instant.now();
        Exception exception = null;
        
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            Instant endTime = Instant.now();
            long timeElapsed = endTime.toEpochMilli() - startTime.toEpochMilli();
            
            String userAgent = request.getHeader("User-Agent");
            String remoteIp = getClientIp(request);
            String method = request.getMethod();
            String path = request.getRequestURI();
            int status = response.getStatus();
            
            log.info("Request: [{}] {} {} from {} - UA: {} - Status: {} - Time: {}ms{}",
                    requestId,
                    method,
                    path,
                    remoteIp,
                    userAgent,
                    status,
                    timeElapsed,
                    exception != null ? " - Error: " + exception.getMessage() : ""
            );
            
            MDC.clear();
            responseWrapper.copyBodyToResponse();
        }
    }
    
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
} 