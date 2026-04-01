package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.CreateUserRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.UsuarioResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateSellerAcount.ICreateSellerAcount;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser.ICreateUser;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.DesativarConta.IMudarAtividadeDaConta;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.GetUserById.IGetUserById;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de contas de usuários")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final ICreateUser createUser;
    private final IMudarAtividadeDaConta changeActivity;
    private final IGetUserById getUserById;
    private final ICreateSellerAcount sellerAcount;

    public UsuarioController(ICreateUser createUser, IMudarAtividadeDaConta changeActivity,
            IGetUserById getUserById, ICreateSellerAcount sellerAcount) {
        this.createUser = createUser;
        this.changeActivity = changeActivity;
        this.getUserById = getUserById;
        this.sellerAcount = sellerAcount;
    }

    @Operation(summary = "Criar novo usuário", description = "Registra um novo usuário comprador na plataforma")
    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid CreateUserRequest usuarios) {
        log.info("Requisição para criar usuário: email={}", usuarios.email());
        UsuarioResponse newUser = createUser.createUser(usuarios);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @Operation(summary = "Buscar usuário por ID")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable("id") UUID id) {
        UsuarioResponse user = getUserById.getUser(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Ativar/Desativar conta", description = "Alterna o status ativo/inativo da conta do usuário autenticado")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/status")
    public ResponseEntity<Boolean> mudarAtividadeDaConta() {
        Boolean isActive = changeActivity.mudarAtividade();
        return ResponseEntity.ok(isActive);
    }

    @Operation(summary = "Criar conta de vendedor", description = "Transforma o usuário autenticado em vendedor")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/vendedores")
    public ResponseEntity<UsuarioResponse> createSellerAccount(
            @RequestParam String stripeAccountId) {

        UsuarioResponse user = sellerAcount.create(stripeAccountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
