package com.github.guilhermemonte21.Ecommerce.API.Idempotency;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.OperacaoDuplicadaException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.IdempotencyGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.IdempotencyRecord;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.OffsetDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyInterceptor.class);
    private static final String IDEMPOTENCY_HEADER = "Idempotency-Key";
    private static final String IDEMPOTENCY_RECORD_ATTRIBUTE = "idempotency_record_key";

    private final IdempotencyGateway idempotencyGateway;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        if (!handlerMethod.hasMethodAnnotation(Idempotent.class)) {
            return true;
        }

        String key = request.getHeader(IDEMPOTENCY_HEADER);
        if (key == null || key.isBlank()) {
            throw new OperacaoDuplicadaException("O header 'Idempotency-Key' é obrigatório para esta operação.");
        }

        Optional<IdempotencyRecord> existing = idempotencyGateway.findByKey(key);
        if (existing.isPresent()) {
            log.info("Requisição duplicada detectada para a chave: {}. Retornando resposta em cache.", key);
            IdempotencyRecord record = existing.get();
            response.setStatus(record.getResponseStatus());
            response.setContentType("application/json");
            response.getWriter().write(record.getResponseBody());
            response.getWriter().flush();
            return false;
        }

        request.setAttribute(IDEMPOTENCY_RECORD_ATTRIBUTE, key);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String key = (String) request.getAttribute(IDEMPOTENCY_RECORD_ATTRIBUTE);
        if (key != null && ex == null && response.getStatus() >= 200 && response.getStatus() < 300) {
            if (response instanceof IdempotencyResponseWrapper wrapper) {
                IdempotencyRecord record = IdempotencyRecord.builder()
                        .idempotencyKey(key)
                        .httpMethod(request.getMethod())
                        .requestPath(request.getRequestURI())
                        .responseStatus(response.getStatus())
                        .responseBody(wrapper.getCaptureAsString())
                        .createdAt(OffsetDateTime.now())
                        .build();
                
                idempotencyGateway.save(record);
            }
        }
    }
}
