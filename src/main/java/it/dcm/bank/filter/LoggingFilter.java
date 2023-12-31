package it.dcm.bank.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER_NAME = "X-Trace-Id";

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId = generateUniqueTraceId();
        MDC.put("traceId", correlationId);

        try {
            response.addHeader(CORRELATION_ID_HEADER_NAME, correlationId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("traceId");
        }
    }

    private String generateUniqueTraceId() {
        return UUID.randomUUID().toString();
    }
}
