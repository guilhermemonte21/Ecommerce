package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data;

import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Enum.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
public class PedidosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_pedido")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "comprador_id", nullable = false)
    private UsuariosEntity comprador;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private UsuariosEntity vendedor;

    @ManyToMany
    @JoinTable(
            name = "pedido_produtos",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<ProdutosEntity> itens = new ArrayList<>();

    @Column(name = "preco_pedido")
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido")
    private StatusPedido status = StatusPedido.CRIADO;

    @Column(name = "criado_em")
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public PedidosEntity(UsuariosEntity comprador, UsuariosEntity vendedor, List<ProdutosEntity> itens, BigDecimal preco, StatusPedido status) {
        this.comprador = comprador;
        this.vendedor = vendedor;
        this.itens = itens;
        this.preco = preco;
        this.status = status;
    }
}
