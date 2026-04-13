package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.CriarCarrinho;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CreateCarrinhoDTO.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.EstoqueInsuficienteException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Mappers.CarrinhoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.UUID;

public class CriarCarrinho implements ICriarCarrinho {

    private static final Logger log = LoggerFactory.getLogger(CriarCarrinho.class);

    private final CarrinhoGateway gateway;
    private final CarrinhoMapperApl mapper;
    private final UsuarioAutenticadoGateway authGateway;
    private final ProdutoGateway produtoGateway;

    public CriarCarrinho(CarrinhoGateway gateway, CarrinhoMapperApl mapper, UsuarioAutenticadoGateway authGateway, ProdutoGateway produtoGateway) {
        this.gateway = gateway;
        this.mapper = mapper;
        this.authGateway = authGateway;
        this.produtoGateway = produtoGateway;
    }

    @Override
    public CarrinhoResponse criar(CreateCarrinhoRequest carrinho) {
        UsuarioAutenticado user = authGateway.get();
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        Carrinho novoCar = mapper.createRequestToDomain(carrinho, user.getUser().getId());
        BigDecimal total = BigDecimal.ZERO;
        for (UUID pId : novoCar.getProdutoIds()) {
            Produtos p = produtoGateway.getById(pId).orElseThrow(() -> new ProdutoNotFoundException(pId));
            if (p.getEstoque() <= 0) {
                throw new EstoqueInsuficienteException(p.getNomeProduto());
            }
            total = total.add(p.getPreco());
        }
        novoCar.setValorTotal(total);
        novoCar.atualizadoAgora();
        Carrinho salvo = gateway.save(novoCar);
        log.info("Carrinho criado: id={}", salvo.getId());
        return mapper.domainToResponse(salvo);
    }
}
