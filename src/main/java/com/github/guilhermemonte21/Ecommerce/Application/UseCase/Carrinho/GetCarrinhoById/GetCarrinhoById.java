package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.CarrinhoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetCarrinhoById implements IGetCarrinhoById{
    private final CarrinhoGateway gateway;
    private final CarrinhoMapperApl mapper;
    private final UsuarioAutenticadoGateway AuthGateway;

    public GetCarrinhoById(CarrinhoGateway gateway, CarrinhoMapperApl mapper, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.mapper = mapper;
        AuthGateway = authGateway;
    }

    @Override
    public CarrinhoResponse FindCarrinhoById(UUID Id){
        Carrinho carrinho = gateway.getById(Id).orElseThrow(() -> new CarrinhoNotFoundException(Id));
        UsuarioAutenticado user = AuthGateway.get();
        if(!user.getUser().getId().equals(carrinho.getComprador().getId())){
            throw new AcessoNegadoException();
        }
        return mapper.DomainToResponse(carrinho);


    }
}
