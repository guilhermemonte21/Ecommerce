package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.LimparCarrinho.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Mappers.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.AddItemAoCarrinho.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.GetCarrinhoById.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.RemoverItemDoCarrinho.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.CriarCarrinho.*;

@Configuration
public class CarrinhoModuleConfig {


    @Bean
    public ICriarCarrinho criarCarrinho(CarrinhoGateway gateway, CarrinhoMapperApl mapper, UsuarioAutenticadoGateway auth, ProdutoGateway produtoGateway) {
        return new CriarCarrinho(gateway, mapper, auth, produtoGateway);
    }

    @Bean
    public IRemoverItemDoCarrinho removerItemDoCarrinho(CarrinhoGateway gateway, UsuarioAutenticadoGateway auth, ProdutoGateway produtoGateway) {
        return new RemoverItemDoCarrinho(gateway, auth, produtoGateway);
    }

    @Bean
    public ILimparCarrinho limparCarrinho(CarrinhoGateway gateway, UsuarioAutenticadoGateway auth) {
        return new LimparCarrinho(gateway, auth);
    }

    @Bean
    public IGetCarrinhoById getCarrinhoById(CarrinhoGateway gateway, CarrinhoMapperApl mapper, UsuarioAutenticadoGateway auth) {
        return new GetCarrinhoById(gateway, mapper, auth);
    }

    @Bean
    public IAddItemAoCarrinho addItemAoCarrinho(CarrinhoGateway gateway, ProdutoGateway produtoGateway, CarrinhoMapperApl mapper, UsuarioAutenticadoGateway auth) {
        return new AddItemAoCarrinho(gateway, produtoGateway, mapper, auth);
    }
}
