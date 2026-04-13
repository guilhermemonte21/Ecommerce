package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetPedidoById.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.ChangePedidoStatus.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.CriarPedido.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetItensByPedido.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Mappers.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetPedidosByComprador.*;

@Configuration
public class PedidoModuleConfig {


    @Bean
    public IChangePedidoStatus changePedidoStatus(PedidoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        return new ChangePedidoStatus(gateway, authGateway);
    }

    @Bean
    public ICriarPedido criarPedido(PedidoGateway pedidoGateway, CarrinhoGateway carrinhoGateway, ProdutoGateway produtoGateway, PedidoMapperApl mapper, UsuarioAutenticadoGateway auth, EventPublisher eventPublisher) {
        return new CriarPedido(pedidoGateway, carrinhoGateway, produtoGateway, mapper, auth, eventPublisher);
    }

    @Bean
    public IGetItensByPedido getItensByPedido(PedidoGateway pedidoGateway, UsuarioAutenticadoGateway auth, PedidoDoVendedorMapperApl mapperApl) {
        return new GetItensByPedido(pedidoGateway, auth, mapperApl);
    }

    @Bean
    public IGetPedidoById getPedidoById(PedidoGateway gateway, PedidoMapperApl mapperApl, UsuarioAutenticadoGateway authGateway) {
        return new GetPedidoById(gateway, mapperApl, authGateway);
    }

    @Bean
    public IGetPedidosByComprador getPedidosByComprador(PedidoGateway gateway, UsuarioAutenticadoGateway authGateway, PedidoMapperApl mapperApl) {
        return new GetPedidosByComprador(gateway, authGateway, mapperApl);
    }
}
