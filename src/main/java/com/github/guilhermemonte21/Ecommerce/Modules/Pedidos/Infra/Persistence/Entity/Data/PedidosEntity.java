package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Data;



import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Enum.StatusPedido;
import jakarta.persistence.*;
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

    @Column(name = "comprador_id", nullable = false)
    private UUID compradorId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PedidoDoVendedorEntity> pedidos = new ArrayList<>();

    @Column(name = "preco_pedido")
    private BigDecimal preco;

    @Column(name = "Endereco_entrega")
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido")
    private StatusPedido status = StatusPedido.PENDENTE;

    @Column(name = "criado_em")
    private OffsetDateTime criadoEm = OffsetDateTime.now();

}
