package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.GetProdutoById.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.AtualizarEstoque.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.BuscarTodosOsProdutos.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.DeletarProduto.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.AtualizarProduto.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.RegistrarProduto.*;

@Configuration
public class ProdutoModuleConfig {


    @Bean
    public IRegistrarProduto registrarProduto(ProdutoGateway gateway, ProdutoMapperApl mapper, UsuarioAutenticadoGateway auth) {
        return new RegistrarProduto(gateway, mapper, auth);
    }

    @Bean
    public IGetProdutoById getProdutoById(ProdutoGateway gateway, ProdutoMapperApl mapper) {
        return new GetProdutoById(gateway, mapper);
    }

    @Bean
    public IBuscarTodosOsProdutos buscarTodosOsProdutos(ProdutoGateway gateway, ProdutoMapperApl mapper) {
        return new BuscarTodosOsProdutos(gateway, mapper);
    }

    @Bean
    public IDeletarProduto deletarProduto(ProdutoGateway gateway, UsuarioAutenticadoGateway auth) {
        return new DeletarProduto(gateway, auth);
    }

    @Bean
    public IAtualizarEstoque atualizarEstoque(ProdutoGateway gateway, UsuarioAutenticadoGateway auth) {
        return new AtualizarEstoque(gateway, auth);
    }

    @Bean
    public IAtualizarProduto atualizarProduto(ProdutoGateway gateway, ProdutoMapperApl mapper, UsuarioAutenticadoGateway auth) {
        return new AtualizarProduto(gateway, mapper, auth);
    }
}
