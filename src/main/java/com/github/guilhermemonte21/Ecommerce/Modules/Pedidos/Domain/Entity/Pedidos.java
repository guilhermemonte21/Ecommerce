package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
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
    private String endereco;
    private StatusPedido status = StatusPedido.PENDENTE;
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public void syncStatus() {
        if (this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.ENTREGUE)) {
            this.status = StatusPedido.ENTREGUE;
        } else if (this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.ENVIADO)) {
            this.status = StatusPedido.ENVIADO;
        } else if (this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.APROVADO)) {
            this.status = StatusPedido.APROVADO;
        } else if (this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.CANCELADO)) {
            this.status = StatusPedido.CANCELADO;
        } else {
            this.status = StatusPedido.PENDENTE;
        }
    }

    public void confirmarPagamento() {
        this.status = StatusPedido.APROVADO;
        for (PedidoDoVendedor item : this.itens) {
            item.setStatus(StatusPedido.APROVADO);
        }
    }

    public void cancelar() {
        this.status = StatusPedido.CANCELADO;
        for (PedidoDoVendedor item : this.itens) {
            item.setStatus(StatusPedido.CANCELADO);
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
