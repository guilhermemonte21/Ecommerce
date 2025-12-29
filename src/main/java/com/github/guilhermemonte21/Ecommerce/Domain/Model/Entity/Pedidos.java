package com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Enum.StatusPedido;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Pedidos {
    private UUID Id;
    private Usuarios Comprador;
    private Usuarios Vendedor;
    private List<Produtos> Itens = new ArrayList<>();
    private BigDecimal Preco;
    private StatusPedido Status = StatusPedido.CRIADO;
    private OffsetDateTime CriadoEm = OffsetDateTime.now();
}
