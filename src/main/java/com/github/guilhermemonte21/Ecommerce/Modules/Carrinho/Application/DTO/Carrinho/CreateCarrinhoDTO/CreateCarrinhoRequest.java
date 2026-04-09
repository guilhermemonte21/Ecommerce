package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CreateCarrinhoDTO;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarrinhoRequest {
    @Nullable
    private List<UUID> produtosIds;
}
