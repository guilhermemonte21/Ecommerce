package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoGateway extends ProdutoCommandGateway, ProdutoBatchGateway {
}
