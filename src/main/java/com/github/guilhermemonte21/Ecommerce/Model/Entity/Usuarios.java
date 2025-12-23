package com.github.guilhermemonte21.Ecommerce.Model.Entity;

import com.github.guilhermemonte21.Ecommerce.Model.Enum.TipoUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Usuarios")
public class Usuarios {
    @Id
    @Column(name = "IdUsuario")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(name = "NomeUsuario")
    private String Nome;

    @Column(name = "EmailUsuario")
    private String Email;

    @Column(name = "CPFusuario")
    private String Cpf;

    @Column(name = "SenhaUsuario")
    private String Senha;

    @Column(name = "Ativo")
    private Boolean ativo = true;

    @Column(name = "RoleUsuario")
    @Enumerated(EnumType.STRING)
    private TipoUsuario role;
}
