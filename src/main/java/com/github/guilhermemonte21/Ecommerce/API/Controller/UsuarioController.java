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
@RequestMapping("/Usuario")
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

    @PostMapping("/usuarios")
    public ResponseEntity<Usuarios> Criar(@RequestBody @Valid CreateUserRequest usuarios){
        Usuarios usuarios1 = createUser.CreateUser(usuarios);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarios1);
    }
    @PutMapping("/DesativarConta")
    public ResponseEntity<Boolean> MudarAtividadeDaConta(@RequestBody @Valid LoginRequest login){
        Boolean isUnactive = changeActivity.MudarAtividadeDaConta(login);
        return ResponseEntity.ok(isUnactive);
    }
    @GetMapping("/{ID}")
    public ResponseEntity<Usuarios> getById(@PathVariable UUID ID){
        Usuarios user = getUserById.getUser(ID);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/CreateSellerAcount")
    public ResponseEntity<Usuarios> sellerAcount(LoginRequest log, UUID GatewayId){
       Usuarios user = sellerAcount.create(log, GatewayId);
       return ResponseEntity.ok(user);
    }
}
