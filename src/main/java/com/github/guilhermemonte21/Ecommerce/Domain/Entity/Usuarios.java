package com.github.guilhermemonte21.Ecommerce.Domain.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
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
    private Boolean ativo ;
    private String TipoUsuario;
    private UUID gatewayAccountId;

}
