package com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Enum.StatusProdutos;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Produtos {
    private UUID Id;
    private String NomeProduto;
    private Usuarios Vendedor;
    private String Descricao;
    private BigDecimal Preco;
    private Long Estoque;
    private StatusProdutos Status = StatusProdutos.Ativo;
}
