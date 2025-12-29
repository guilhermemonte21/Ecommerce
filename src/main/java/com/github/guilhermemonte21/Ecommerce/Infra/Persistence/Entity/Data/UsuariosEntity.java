package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor

public class UsuariosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_usuario")
    private UUID Id;

    @Column(name = "nome_usuario")
    private String Nome;

    @Column(name = "email_usuario")
    private String Email;

    @Column(name = "cpf_usuario")
    private String Cpf;

    @Column(name = "senha_usuario")
    private String Senha;

    @Column(name = "ativo")
    private Boolean Ativo = true;


    @Column(name = "role_usuario")
    private String Role ;

}

