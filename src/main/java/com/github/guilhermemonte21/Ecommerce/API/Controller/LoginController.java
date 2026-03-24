package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.API.DTO.TokenResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Infra.Config.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@AllArgsConstructor
@RestController
public class LoginController {
    private final TokenService tokenService;
    private final ILogin login;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid LoginRequest request) {
        boolean autenticado = login.login(request.email(), request.senha());

        if (autenticado) {
            String token = tokenService.gerarToken(request.email());
            return ResponseEntity.ok(new TokenResponse(token));
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
