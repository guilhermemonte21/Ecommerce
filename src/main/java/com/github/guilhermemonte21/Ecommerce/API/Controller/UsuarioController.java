package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.CreateUserRequest;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateSellerAcount.ICreateSellerAcount;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser.ICreateUser;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.DesativarConta.IMudarAtividadeDaConta;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.GetUserById.IGetUserById;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final ICreateUser createUser;
    private final IMudarAtividadeDaConta changeActivity;
    private final IGetUserById getUserById;
    private final ICreateSellerAcount sellerAcount;

    public UsuarioController(ICreateUser createUser, IMudarAtividadeDaConta changeActivity, IGetUserById getUserById, ICreateSellerAcount sellerAcount) {
        this.createUser = createUser;
        this.changeActivity = changeActivity;
        this.getUserById = getUserById;
        this.sellerAcount = sellerAcount;
    }

    @PostMapping
    public ResponseEntity<Usuarios> criar(@RequestBody @Valid CreateUserRequest usuarios) {
        Usuarios newUser = createUser.CreateUser(usuarios);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> getById(@PathVariable("id") UUID id) {
        Usuarios user = getUserById.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/status")
    public ResponseEntity<Boolean> mudarAtividadeDaConta(@RequestBody @Valid LoginRequest login) {
        Boolean isInactive = changeActivity.mudarAtividade(login);
        return ResponseEntity.ok(isInactive);
    }

    @PostMapping("/vendedores")
    public ResponseEntity<Usuarios> createSellerAccount(@RequestBody LoginRequest login, @RequestParam UUID gatewayId) {
        Usuarios user = sellerAcount.create(login, gatewayId);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
