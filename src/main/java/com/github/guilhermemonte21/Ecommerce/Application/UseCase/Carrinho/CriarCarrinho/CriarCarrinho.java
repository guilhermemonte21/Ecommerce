package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.CriarCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoDTO.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.CarrinhoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CriarCarrinho implements ICriarCarrinho{
    private final CarrinhoGateway gateway;
    private final CarrinhoMapperApl mapper;
    private final UsuarioAutenticadoGateway AuthGateway;

    public CriarCarrinho(CarrinhoGateway gateway, CarrinhoMapperApl mapper, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.mapper = mapper;
        AuthGateway = authGateway;
    }

    public CarrinhoResponse Criar(CreateCarrinhoRequest carrinho){
        UsuarioAutenticado user = AuthGateway.get();
        Carrinho carrinho1 = mapper.CreateResquesttoDomain(carrinho, user.getUser().getId());
        carrinho1.atualizarValorTotal();
        carrinho1.AtualizadoAgora();
        Carrinho salvo = gateway.save(carrinho1);
        return mapper.DomainToResponse(salvo);
    }
}
