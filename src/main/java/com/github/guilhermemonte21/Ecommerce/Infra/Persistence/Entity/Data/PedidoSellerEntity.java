package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoSellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_pedido_seller")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidosEntity pedido;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private UsuariosEntity seller;

    @OneToOne
    @JoinTable(
            name = "pedido_seller_produtos",
            joinColumns = @JoinColumn(name = "pedido_seller_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private ProdutosEntity produtos ;


    @Column(name = "status_seller", nullable = false)
    private String status ;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;}
