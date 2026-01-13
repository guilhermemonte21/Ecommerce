package com.github.guilhermemonte21.Ecommerce.Domain.Entity;

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

    private Usuarios Vendedor;

    private UUID Pedido;

    private List<Produtos> produtos = new ArrayList<>();

    private BigDecimal Valor;

    private String Status;

}
