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
    private UUID id;
    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private Boolean ativo = true;
    private String role;

}
