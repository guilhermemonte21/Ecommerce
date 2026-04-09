package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.API.DTO.TokenResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@AllArgsConstructor
@RestController
@Tag(name = "Autenticação", description = "Login e geração de tokens JWT")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final TokenService tokenService;
    private final ILogin login;

    @Operation(summary = "Login", description = "Autentica um usuário e retorna um token JWT")
    @PostMapping("/login")
    @RateLimiter(name = "loginRateLimiter", fallbackMethod = "fallbackLogin")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        boolean autenticado = login.login(request.email(), request.senha());

        if (autenticado) {
            String token = tokenService.gerarToken(request.email());
            log.info("Login realizado com sucesso: email={}", request.email());
            return ResponseEntity.ok(new TokenResponse(token));
        } else {
            log.warn("Tentativa de login falhou: email={}", request.email());
            return ResponseEntity.status(401).build();
        }
    }

    public ResponseEntity<TokenResponse> fallbackLogin(LoginRequest request, Throwable t) {
        log.warn("Rate limit excedido para tentativas de login: email={}", request.email());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
