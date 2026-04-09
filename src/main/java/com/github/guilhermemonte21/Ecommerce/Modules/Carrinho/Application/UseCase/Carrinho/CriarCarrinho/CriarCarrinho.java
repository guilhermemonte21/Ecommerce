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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CriarCarrinho implements ICriarCarrinho {

    private static final Logger log = LoggerFactory.getLogger(CriarCarrinho.class);

    private final CarrinhoGateway gateway;
    private final CarrinhoMapperApl mapper;
    private final UsuarioAutenticadoGateway authGateway;

    public CriarCarrinho(CarrinhoGateway gateway, CarrinhoMapperApl mapper, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.mapper = mapper;
        this.authGateway = authGateway;
    }

    @Override
    public CarrinhoResponse criar(CreateCarrinhoRequest carrinho) {
        UsuarioAutenticado user = authGateway.get();
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        Carrinho novoCar = mapper.createRequestToDomain(carrinho, user.getUser().getId());
        for (Produtos p : novoCar.getItens()) {
            if (p.getEstoque() <= 0) {
                throw new EstoqueInsuficienteException(p.getNomeProduto());
            }
        }
        novoCar.atualizarValorTotal();
        novoCar.atualizadoAgora();
        Carrinho salvo = gateway.save(novoCar);
        log.info("Carrinho criado: id={}", salvo.getId());
        return mapper.domainToResponse(salvo);
    }
}
