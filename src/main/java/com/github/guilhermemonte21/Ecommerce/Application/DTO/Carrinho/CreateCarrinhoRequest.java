package com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarrinhoRequest {
    @NotNull
    private UUID comprador;

    private List<UUID> produtosIds;
}
