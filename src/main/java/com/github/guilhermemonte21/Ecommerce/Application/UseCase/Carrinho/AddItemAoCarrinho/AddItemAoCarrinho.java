package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.CarrinhoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AddItemAoCarrinho implements IAddItemAoCarrinho{
    private final CarrinhoGateway gateway;
    private final ProdutoGateway Produtogateway;
    private final CarrinhoMapperApl mapperApl;
    private final UsuarioAutenticadoGateway AuthGateway;

    public AddItemAoCarrinho(CarrinhoGateway gateway, ProdutoGateway produtogateway, CarrinhoMapperApl mapperApl, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.Produtogateway = produtogateway;
        this.mapperApl = mapperApl;
        this.AuthGateway = authGateway;
    }

    @Override
    public CarrinhoResponse AdicionarAoCarrinho(UUID idCarrinho, UUID IdProduto, Long quantidade){
        UsuarioAutenticado user = AuthGateway.get();
        Produtos produto = Produtogateway.GetById(IdProduto).orElseThrow(() -> new ProdutoNotFoundException(IdProduto));
        if(quantidade > produto.getEstoque()){
            throw new IllegalArgumentException("Estoque Insuficiente");
        }
        if(produto.getEstoque()<= 0){
            throw new RuntimeException("Produto sem Estoque suficiente");
        }
        if (user.getUser().getAtivo() == false){
            throw new UsuarioInativoException();
        }

        Carrinho carrinhoComItem =  gateway.add(idCarrinho, produto, quantidade);
        if (!user.getUser().getId().equals(carrinhoComItem.getComprador().getId())){
            throw new RuntimeException("Acesso negado");
        }
        carrinhoComItem.atualizarValorTotal();
        carrinhoComItem.AtualizadoAgora();
        Carrinho cart = gateway.save(carrinhoComItem);
        return mapperApl.DomainToResponse(cart);
    }
}
