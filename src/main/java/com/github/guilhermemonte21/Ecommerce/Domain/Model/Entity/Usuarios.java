package com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Enum.TipoUsuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Usuarios {
    private UUID Id;
    private String Nome;
    private String Email;
    private String Cpf;
    private String Senha;
    private Boolean ativo = true;
    private TipoUsuario role;

    public Usuarios(String Nome, String Email, String Cpf, String Senha) {
        this.Nome = Nome;
        this.Email = Email;
        this.Cpf = Cpf;
        this.Senha = Senha;
        this.ativo = true;
    }
}
