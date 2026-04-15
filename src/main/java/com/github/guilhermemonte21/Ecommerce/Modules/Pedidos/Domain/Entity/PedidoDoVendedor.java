package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity;



import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
