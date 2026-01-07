package com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Enum.StatusPedido;
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
    private List<Produtos> Itens = new ArrayList<>();
    private BigDecimal Preco;
    private StatusPedido Status = StatusPedido.CRIADO;
    private OffsetDateTime CriadoEm = OffsetDateTime.now();

//    public void CompradorNaoVendedor(){
//        if (this.Comprador == this.Itens.get().getVendedor().getId()){
//            throw new RuntimeException("Não pode comprar o proprio produto");
//        }
//    }

    public void ChangeToPago(){
        this.Status = StatusPedido.PAGO;
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
