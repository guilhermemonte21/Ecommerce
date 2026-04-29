package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDoVendedor {

    private UUID id;

    private UUID vendedorId;

    private Pedidos pedido;

    private UUID produtoId;
    private String nomeProduto;
    private BigDecimal precoUnitario;
    private Long quantidade;

    private BigDecimal valor;

    private String stripeAccountId;

    private StatusPedido status;

}
