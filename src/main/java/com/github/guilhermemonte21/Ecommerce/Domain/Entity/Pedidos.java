package com.github.guilhermemonte21.Ecommerce.Domain.Entity;

import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedidos {
    private UUID id;
    private Usuarios comprador;
    private List<PedidoDoVendedor> itens = new ArrayList<>();
    private BigDecimal preco;
    private String endereço;
    private StatusPedido status = StatusPedido.CRIADO;
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public void syncStatus() {
        boolean todosPagos = this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.PAGO);
        boolean todosEntregues = this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.ENTREGUE);
        boolean todosEnviados = this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.ENVIADO);

        if (todosEntregues) {
            this.status = StatusPedido.ENTREGUE;
        } else if (todosEnviados) {
            this.status = StatusPedido.ENVIADO;
        } else if (todosPagos) {
            this.status = StatusPedido.PAGO;
        } else {
            this.status = StatusPedido.CRIADO;
        }
    }

    public void confirmarPagamento() {
        this.status = StatusPedido.PAGO;
        for (PedidoDoVendedor item : this.itens) {
            item.setStatus(StatusPedido.PAGO);
        }
    }

    public void changeToCancelado() {
        this.status = StatusPedido.CANCELADO;
    }

    public void changeToEnviado() {
        this.status = StatusPedido.ENVIADO;
    }

    public void changeToEntregue() {
        this.status = StatusPedido.ENTREGUE;
    }
}
