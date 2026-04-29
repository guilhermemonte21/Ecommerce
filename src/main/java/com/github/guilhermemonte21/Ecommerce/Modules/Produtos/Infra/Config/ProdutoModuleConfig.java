package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.GetProdutoById.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarEstoque.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.BuscarTodosOsProdutos.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.DeletarProduto.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarProduto.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.RegistrarProduto.*;

@Configuration
public class ProdutoModuleConfig {


    @Bean
    public IRegistrarProduto registrarProduto(ProdutoGateway gateway, ProdutoMapperApl mapper,
                                              UsuarioAutenticadoGateway auth, EventPublisher eventPublisher) {
        return new RegistrarProduto(gateway, mapper, auth, eventPublisher);
    }

    @Bean
    public IGetProdutoById getProdutoById(IProdutoQueryGateway gateway) {
        return new GetProdutoById(gateway);
    }

    @Bean
    public IBuscarTodosOsProdutos buscarTodosOsProdutos(IProdutoQueryGateway gateway) {
        return new BuscarTodosOsProdutos(gateway);
    }

    @Bean
    public IDeletarProduto deletarProduto(ProdutoGateway gateway, UsuarioAutenticadoGateway auth, EventPublisher eventPublisher) {
        return new DeletarProduto(gateway, auth, eventPublisher);
    }

    @Bean
    public IAtualizarEstoque atualizarEstoque(ProdutoGateway gateway, UsuarioAutenticadoGateway auth, EventPublisher eventPublisher) {
        return new AtualizarEstoque(gateway, auth, eventPublisher);
    }

    @Bean
    public IAtualizarProduto atualizarProduto(ProdutoGateway gateway, ProdutoMapperApl mapper,
                                              UsuarioAutenticadoGateway auth, EventPublisher eventPublisher) {
        return new AtualizarProduto(gateway, mapper, auth, eventPublisher);
    }
}
