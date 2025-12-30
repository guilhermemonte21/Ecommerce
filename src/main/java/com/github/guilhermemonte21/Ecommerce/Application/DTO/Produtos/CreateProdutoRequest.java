package com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProdutoRequest {

    private UUID id;
    private String nomeProduto;
    private UUID vendedor;
    private String descricao;
    private BigDecimal preco;
    private Long estoque;
}
