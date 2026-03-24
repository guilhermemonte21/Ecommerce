package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.CarrinhoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;

import java.util.UUID;

public class GetCarrinhoById implements IGetCarrinhoById {

    private final CarrinhoGateway gateway;
    private final CarrinhoMapperApl mapper;
    private final UsuarioAutenticadoGateway authGateway;

    public GetCarrinhoById(CarrinhoGateway gateway, CarrinhoMapperApl mapper,
                           UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.mapper = mapper;
        this.authGateway = authGateway;
    }

    @Override
    public CarrinhoResponse findCarrinhoById(UUID id) {
        Carrinho carrinho = gateway.getById(id)
                .orElseThrow(() -> new CarrinhoNotFoundException(id));
        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(carrinho.getComprador().getId())) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        return mapper.domainToResponse(carrinho);
    }
}
