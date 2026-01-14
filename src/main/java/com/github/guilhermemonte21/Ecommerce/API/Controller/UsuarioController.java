package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser.ICreateUser;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.DesativarConta.IDesativarConta;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Usuario")
public class UsuarioController {
    private final ICreateUser createUser;
    private final IDesativarConta desativarConta;

    public UsuarioController(ICreateUser createUser, IDesativarConta desativarConta) {
        this.createUser = createUser;
        this.desativarConta = desativarConta;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Usuarios> Criar(@RequestBody Usuarios usuarios){
        Usuarios usuarios1 = createUser.CreateUser(usuarios);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarios1);
    }
    @PutMapping("/DesativarConta")
    public ResponseEntity<Boolean> Desativar(@RequestBody String Email, String Senha){
        Boolean isUnactive = desativarConta.DesativarConta(Email, Senha);
        return ResponseEntity.ok(isUnactive);
    }
}
