package com.github.guilhermemonte21.Ecommerce.Model.Entity;

import com.github.guilhermemonte21.Ecommerce.Model.Enum.StatusProdutos;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Produtos")
@NoArgsConstructor
public class Produtos {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "IdProdutos")
    private UUID Id;

    @Column(name = "NomeProduto")
    private String NomeProduto;

    @Column(name = "Vendedor")
    @ManyToOne
    private Usuarios Vendedor;

    @Column(name = "DescricaoProduto")
    private String Descricao;

    @Column(name = "PrecoProduto")
    private BigDecimal Preco;

    @Column(name = "EstoqueProduto")
    private Long Estoque;

    @Enumerated(EnumType.STRING)
    @Column(name = "StatusProduto")
    private StatusProdutos Status = StatusProdutos.Ativo;
}
