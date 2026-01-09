package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddItemAoCarrinho implements IAddItemAoCarrinho{
    private final CarrinhoGateway gateway;
    private final ProdutoGateway Produtogateway;

    public AddItemAoCarrinho(CarrinhoGateway gateway, ProdutoGateway produtogateway) {
        this.gateway = gateway;
        Produtogateway = produtogateway;
    }

    @Override
    public Carrinho AdicionarAoCarrinho(UUID idCarrinho, UUID IdProduto, Long quantidade){
        Produtos produto = Produtogateway.GetById(IdProduto).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        if(quantidade > produto.getEstoque()){
            throw new RuntimeException("Estoque Insuficiente");
        }
        Carrinho carrinhoComItem =  gateway.add(idCarrinho, produto, quantidade);
        carrinhoComItem.atualizarValorTotal();
        carrinhoComItem.AtualizadoAgora();
        gateway.save(carrinhoComItem);
        return carrinhoComItem;
    }
}
