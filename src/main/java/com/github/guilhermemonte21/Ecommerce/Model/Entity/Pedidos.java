package com.github.guilhermemonte21.Ecommerce.Model.Entity;

import com.github.guilhermemonte21.Ecommerce.Model.Enum.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Pedidos")
@Getter
@Setter
public class Pedidos {

    @Id
    @Column(name = "IdPedido")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(name = "Comprador")
    private Usuarios Comprador;

    @Column(name = "Vendedor")
    private Usuarios Vendedor;

    @Column(name = "ItensPedido")
    private List<Produtos> Itens = new ArrayList<>();

    @Column(name = "PrecoPedido")
    private BigDecimal Preco;

    @Enumerated
    @Column(name = "StatusPedido")
    private StatusPedido Status = StatusPedido.CRIADO;

    @Column(name = "CriadoEm")
    private OffsetDateTime CriadoEm = OffsetDateTime.now();
}
