package com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuarios {
    private UUID Id;
    private String Nome;
    private String Email;
    private String Cpf;
    private String Senha;
    private Boolean ativo = true;
    private String role;

}
