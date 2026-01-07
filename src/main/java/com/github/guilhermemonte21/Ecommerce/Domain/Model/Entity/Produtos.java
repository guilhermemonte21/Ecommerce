package com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Enum.StatusProdutos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produtos {
    private UUID Id;
    @NotBlank
    private String NomeProduto;
    @NotNull
    private Usuarios Vendedor;
    @NotBlank
    private String Descricao;
    @NotNull
    private BigDecimal Preco;
    @NotNull
    private Long Estoque;

    public void AtualizarEstoque(Long quantity){
        if(quantity == null){
            return;
        }
        if(this.Estoque == null){
            this.Estoque = 0L;
        }
        this.Estoque += quantity;
    }

    public void EstoquePosCompra(Pedidos pedido){
        if (pedido.getStatus().equals(StatusPedido.PAGO) ){
            Estoque -= 1;
        }
    }

}
