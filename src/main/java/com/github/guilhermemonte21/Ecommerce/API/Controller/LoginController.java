package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.API.DTO.TokenResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Infra.Config.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
