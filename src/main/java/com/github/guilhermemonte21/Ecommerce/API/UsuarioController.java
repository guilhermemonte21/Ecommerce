package com.github.guilhermemonte21.Ecommerce.API;

import com.github.guilhermemonte21.Ecommerce.Application.UseCase.CreateUser;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Usuario")
public class UsuarioController {
    private final CreateUser Criar;

    public UsuarioController(CreateUser criar) {
        Criar = criar;
    }


}
