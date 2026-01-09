package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
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

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private UsuariosEntity Vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Pedido")
    private PedidosEntity Pedido;

    @ManyToMany
    @JoinTable(
            name = "produto_vendedor_produtos",
            joinColumns = @JoinColumn(name = "produto_vendedor_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<ProdutosEntity> produtos = new ArrayList<>();

    @Column(name = "valorDoPedido")
    private BigDecimal Valor;

    @Column(name = "StatusDoPedido")
    private String Status;


}
