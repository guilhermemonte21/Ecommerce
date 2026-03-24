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
    private UUID Id;
    private Usuarios Comprador;
    private List<PedidoDoVendedor> Itens = new ArrayList<>();
    private BigDecimal Preco;
    private String Endereço;
    private StatusPedido Status = StatusPedido.CRIADO;
    private OffsetDateTime CriadoEm = OffsetDateTime.now();

//    public void CompradorNaoVendedor(){
//        if (this.Comprador == this.Itens.get().getVendedor().getId()){
//            throw new RuntimeException("Não pode comprar o proprio produto");
//        }
//    }

    public void syncStatus() {
        boolean todosPagos = this.Itens.stream().allMatch(p -> p.getStatus() == StatusPedido.PAGO);
        boolean todosEntregues = this.Itens.stream().allMatch(p -> p.getStatus() == StatusPedido.ENTREGUE);
        boolean todosEnviados = this.Itens.stream().allMatch(p -> p.getStatus() == StatusPedido.ENVIADO);

        if (todosEntregues) {
            this.Status = StatusPedido.ENTREGUE;
        } else if (todosEnviados) {
            this.Status = StatusPedido.ENVIADO;
        } else if (todosPagos) {
            this.Status = StatusPedido.PAGO;
        } else {
            this.Status = StatusPedido.CRIADO;
        }
    }

    public void confirmarPagamento(){
        this.Status = StatusPedido.PAGO;
        for (PedidoDoVendedor item : this.Itens){
            item.setStatus(StatusPedido.PAGO);
        }
    }

    public void ChangeToCancelado(){
        this.Status = StatusPedido.CANCELADO;
    }
    public void ChangeToEnviado(){
        this.Status = StatusPedido.ENVIADO;
    }
    public void ChangeToEntregue(){
        this.Status = StatusPedido.ENTREGUE;
    }

}
