package com.github.guilhermemonte21.Ecommerce.API.Idempotency;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IdempotencyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse httpResponse) {
            IdempotencyResponseWrapper wrapper = new IdempotencyResponseWrapper(httpResponse);
            chain.doFilter(request, wrapper);

            if (wrapper.getCaptureAsBytes().length > 0 && !httpResponse.isCommitted()) {
                httpResponse.getOutputStream().write(wrapper.getCaptureAsBytes());
                httpResponse.getOutputStream().flush();
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
