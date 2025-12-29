package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data;

import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Enum.StatusProdutos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
public class ProdutosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_produto")
    private UUID id;

    @Column(name = "nome_produto")
    private String nomeProduto;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private UsuariosEntity vendedor;

    @Column(name = "descricao_produto")
    private String descricao;

    @Column(name = "preco_produto")
    private BigDecimal preco;

    @Column(name = "estoque_produto")
    private Long estoque;

}

