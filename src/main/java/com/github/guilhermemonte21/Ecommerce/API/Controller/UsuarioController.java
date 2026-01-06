package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser.CreateUser;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Usuario")
public class UsuarioController {
    private final CreateUser createUser;

    public UsuarioController(CreateUser createUser) {
        this.createUser = createUser;
    }

    @PostMapping
    public ResponseEntity<Usuarios> Criar(@RequestBody Usuarios usuarios){
        Usuarios usuarios1 = createUser.CreateUser(usuarios);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarios1);
    }
}
