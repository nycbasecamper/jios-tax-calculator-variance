package gov.irs.jios.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Limit the request payload to 1.5 MB
 */
@Slf4j
@Component
public class PayloadSizeLimitFilter implements Filter {

    private static final int MAX_PAYLOAD_SIZE = 1500000;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        int requestSize = servletRequest.getContentLength();
        if (requestSize > MAX_PAYLOAD_SIZE) {
            log.error("ERROR: Request size " + requestSize + " bytes is too large");
            sendError((HttpServletResponse) servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void sendError(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
