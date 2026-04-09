package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Persistence.Entity.Data;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Persistence.Entity.Enum.TipoUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "usuarios", indexes = {
        @Index(name = "idx_usuario_email", columnList = "email_usuario", unique = true)
})
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

    @Column(name = "cpf_usuario", unique = true)
    private String Cpf;

    @Column(name = "senha_usuario")
    private String Senha;

    @Column(name = "ativo")
    private Boolean Ativo;

    @Column(name = "tipo_usuario")
    @Enumerated(value = EnumType.STRING)
    private TipoUsuario TipoUsuario;

    @Column(name = "stripe_account_id")
    private String stripeAccountId;

}
