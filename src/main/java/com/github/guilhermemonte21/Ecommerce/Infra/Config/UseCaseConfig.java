package com.github.guilhermemonte21.Ecommerce.Infra.Config;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.*;
import com.github.guilhermemonte21.Ecommerce.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho.*;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.CriarCarrinho.*;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById.*;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.LimparCarrinho.*;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.RemoverItemDoCarrinho.*;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento.IPagamento;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento.Pagamento;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus.ChangePedidoStatus;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus.IChangePedidoStatus;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido.CriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido.ICriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido.GetItensByPedido;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido.IGetItensByPedido;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidoById.GetPedidoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidoById.IGetPedidoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidosByComprador.IGetPedidosByComprador;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidosByComprador.GetPedidosByComprador;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque.AtualizarEstoque;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque.IAtualizarEstoque;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarProduto.AtualizarProduto;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarProduto.IAtualizarProduto;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.BuscarTodosOsProdutos.BuscarTodosOsProdutos;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.BuscarTodosOsProdutos.IBuscarTodosOsProdutos;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.DeletarProduto.DeletarProduto;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.DeletarProduto.IDeletarProduto;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.GetProdutoById.GetProdutoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.GetProdutoById.IGetProdutoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.RegistrarProduto.IRegistrarProduto;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.RegistrarProduto.RegistrarProduto;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateSellerAcount.CreateSellerAcount;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateSellerAcount.ICreateSellerAcount;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser.CreateUser;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser.ICreateUser;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.DesativarConta.IMudarAtividadeDaConta;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.DesativarConta.MudarAtividadeDaConta;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.GetUserById.GetUserById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.GetUserById.IGetUserById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.Login;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UseCaseConfig {

    @Bean
    public ILogin login(UsuarioGateway gateway, PasswordEncoder encoder) {
        return new Login(gateway, encoder);
    }

    @Bean
    public IGetUserById getUserById(UsuarioGateway gateway, UsuarioMapperApl mapper) {
        return new GetUserById(gateway, mapper);
    }

    @Bean
    public IMudarAtividadeDaConta mudarAtividadeDaConta(UsuarioGateway gateway,
                                                        UsuarioAutenticadoGateway authGateway) {
        return new MudarAtividadeDaConta(gateway, authGateway);
    }

    @Bean
    public ICreateUser createUser(UsuarioGateway gateway, PasswordEncoder encoder, UsuarioMapperApl mapper) {
        return new CreateUser(gateway, encoder, mapper);
    }

    @Bean
    public ICreateSellerAcount createSellerAcount(UsuarioGateway gateway, UsuarioMapperApl mapper,
                                                   UsuarioAutenticadoGateway authGateway) {
        return new CreateSellerAcount(gateway, mapper, authGateway);
    }

    @Bean
    public IRegistrarProduto registrarProduto(ProdutoGateway gateway, ProdutoMapperApl mapper,
                                               UsuarioAutenticadoGateway auth) {
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
    public IAtualizarProduto atualizarProduto(ProdutoGateway gateway, ProdutoMapperApl mapper,
                                               UsuarioAutenticadoGateway auth) {
        return new AtualizarProduto(gateway, mapper, auth);
    }

    @Bean
    public IPagamento pagamento(PedidoGateway pedidoGateway, PagamentoGateway pagamentoGateway,
                                EventPublisher eventPublisher) {
        return new Pagamento(pedidoGateway, pagamentoGateway, eventPublisher);
    }

    @Bean
    public IChangePedidoStatus changePedidoStatus(PedidoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        return new ChangePedidoStatus(gateway, authGateway);
    }

    @Bean
    public ICriarPedido criarPedido(PedidoGateway pedidoGateway, CarrinhoGateway carrinhoGateway,
                                     ProdutoGateway produtoGateway, PedidoMapperApl mapper,
                                     UsuarioAutenticadoGateway auth, EventPublisher eventPublisher) {
        return new CriarPedido(pedidoGateway, carrinhoGateway, produtoGateway, mapper, auth, eventPublisher);
    }

    @Bean
    public IGetItensByPedido getItensByPedido(PedidoGateway pedidoGateway, UsuarioAutenticadoGateway auth,
                                               PedidoDoVendedorMapperApl mapperApl) {
        return new GetItensByPedido(pedidoGateway, auth, mapperApl);
    }

    @Bean
    public IGetPedidoById getPedidoById(PedidoGateway gateway, PedidoMapperApl mapperApl,
                                         UsuarioAutenticadoGateway authGateway) {
        return new GetPedidoById(gateway, mapperApl, authGateway);
    }

    @Bean
    public IGetPedidosByComprador getPedidosByComprador(PedidoGateway gateway,
                                                         UsuarioAutenticadoGateway authGateway,
                                                         PedidoMapperApl mapperApl) {
        return new GetPedidosByComprador(gateway, authGateway, mapperApl);
    }

    @Bean
    public ICriarCarrinho criarCarrinho(CarrinhoGateway gateway, CarrinhoMapperApl mapper,
                                         UsuarioAutenticadoGateway auth) {
        return new CriarCarrinho(gateway, mapper, auth);
    }

    @Bean
    public IRemoverItemDoCarrinho removerItemDoCarrinho(CarrinhoGateway gateway, UsuarioAutenticadoGateway auth) {
        return new RemoverItemDoCarrinho(gateway, auth);
    }

    @Bean
    public ILimparCarrinho limparCarrinho(CarrinhoGateway gateway, UsuarioAutenticadoGateway auth) {
        return new LimparCarrinho(gateway, auth);
    }

    @Bean
    public IGetCarrinhoById getCarrinhoById(CarrinhoGateway gateway, CarrinhoMapperApl mapper,
                                             UsuarioAutenticadoGateway auth) {
        return new GetCarrinhoById(gateway, mapper, auth);
    }

    @Bean
    public IAddItemAoCarrinho addItemAoCarrinho(CarrinhoGateway gateway, ProdutoGateway produtoGateway,
                                                 CarrinhoMapperApl mapper, UsuarioAutenticadoGateway auth) {
        return new AddItemAoCarrinho(gateway, produtoGateway, mapper, auth);
    }

    @Bean
    public IdempotencyGateway idempotencyGateway(com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaIdempotencyRepository repository,
                                                 com.github.guilhermemonte21.Ecommerce.Infra.Mappers.IdempotencyRecordMapper mapper) {
        return new com.github.guilhermemonte21.Ecommerce.Infra.Gateway.IdempotencyGatewayImpl(repository, mapper);
    }
}
