package com.github.guilhermemonte21.Ecommerce.API.GlobalExceptionHandler;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CarrinhoNotFoundException.class)
    public ProblemDetail handleCarrinhoNotFound(CarrinhoNotFoundException ex) {
        log.warn("Carrinho não encontrado: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.NOT_FOUND, "Carrinho não encontrado", ex.getMessage());
    }

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ProblemDetail handleProdutoNotFound(ProdutoNotFoundException ex) {
        log.warn("Produto não encontrado: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.NOT_FOUND, "Produto não encontrado", ex.getMessage());
    }

    @ExceptionHandler(PedidoNotFoundException.class)
    public ProblemDetail handlePedidoNotFound(PedidoNotFoundException ex) {
        log.warn("Pedido não encontrado: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.NOT_FOUND, "Pedido não encontrado", ex.getMessage());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ProblemDetail handleUsuarioNotFound(UsuarioNotFoundException ex) {
        log.warn("Usuário não encontrado: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex.getMessage());
    }

    @ExceptionHandler(UsuarioJaExisteException.class)
    public ProblemDetail handleUsuarioJaExiste(UsuarioJaExisteException ex) {
        log.warn("Tentativa de cadastro duplicado: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.CONFLICT, "Usuário já existe", ex.getMessage());
    }

    @ExceptionHandler(QuantidadeInvalidaException.class)
    public ProblemDetail handleQuantidadeInvalida(QuantidadeInvalidaException ex) {
        log.warn("Quantidade inválida: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.BAD_REQUEST, "Quantidade inválida", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.BAD_REQUEST, "Argumento inválido", ex.getMessage());
    }

    @ExceptionHandler(OperacaoDuplicadaException.class)
    public ProblemDetail handleOperacaoDuplicada(OperacaoDuplicadaException ex) {
        log.warn("Operação duplicada ou inválida: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.BAD_REQUEST, "Chave de Idempotência inválida ou duplicada",
                ex.getMessage());
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ProblemDetail handleAcessoNegado(AcessoNegadoException ex) {
        log.warn("Acesso negado: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.FORBIDDEN, "Acesso negado", ex.getMessage());
    }

    @ExceptionHandler(UsuarioInativoException.class)
    public ProblemDetail handleInactiveUser(UsuarioInativoException ex) {
        log.warn("Usuário inativo tentou acessar recurso: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.FORBIDDEN, "Usuário inativo", ex.getMessage());
    }

    @ExceptionHandler(CarrinhoVazioException.class)
    public ProblemDetail handleCarrinhoVazio(CarrinhoVazioException ex) {
        log.warn("Carrinho vazio: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Carrinho vazio", ex.getMessage());
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ProblemDetail handleEstoqueInsuficiente(EstoqueInsuficienteException ex) {
        log.warn("Estoque insuficiente: {}", ex.getMessage());
        return createProblemDetail(HttpStatus.CONFLICT, "Estoque insuficiente", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Erro de validação: {}", errors);
        ProblemDetail problem = createProblemDetail(HttpStatus.BAD_REQUEST, "Erro de validação", errors);
        problem.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
                .map(e -> java.util.Map.of("field", e.getField(), "message", e.getDefaultMessage()))
                .toList());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        log.error("Erro interno inesperado", ex);
        return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.");
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/" + status.value()));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}