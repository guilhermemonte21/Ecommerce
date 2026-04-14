package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Data;



import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Enum.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedidosDoVendedor")
@Getter
@Setter
@NoArgsConstructor
public class PedidoDoVendedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ProdutoVendedorId")
    private UUID id;

    @Column(name = "vendedor_id", nullable = false)
    private UUID vendedorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Pedido")
    private PedidosEntity pedido;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "produto_vendedor_produtos", joinColumns = @JoinColumn(name = "produto_vendedor_id"))
    @Column(name = "produto_id")
    private List<UUID> produtoIds = new ArrayList<>();

    @Column(name = "valorDoPedido")
    private BigDecimal Valor;

    @Column(name = "stripe_account_id")
    private String stripeAccountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "StatusDoPedido")
    private StatusPedido status;

}
