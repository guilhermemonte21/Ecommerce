package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Persistence.Entity.Data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "carrinho_itens")
@Getter
@Setter
@NoArgsConstructor
public class CarrinhoItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrinho_id")
    private CarrinhoEntity carrinho;

    @Column(name = "produto_id", nullable = false)
    private UUID produtoId;

    @Column(name = "nome_produto_snapshot")
    private String nomeProduto;

    @Column(name = "preco_snapshot")
    private BigDecimal preco;
    
    @Column(name = "quantidade")
    private Long quantidade;

    public CarrinhoItemEntity(CarrinhoEntity carrinho, UUID produtoId, String nomeProduto, BigDecimal preco, Long quantidade) {
        this.carrinho = carrinho;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.preco = preco;
        this.quantidade = quantidade;
    }
}
