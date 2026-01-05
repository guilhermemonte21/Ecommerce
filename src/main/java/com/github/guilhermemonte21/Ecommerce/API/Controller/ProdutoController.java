package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.GetProdutoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.RegistrarProduto;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/Produto")
public class ProdutoController {
    private final RegistrarProduto registrarProduto;
    private final AtualizarEstoque atualizarEstoque;
    private final GetProdutoById getProdutoById;

    public ProdutoController(RegistrarProduto registrarProduto, AtualizarEstoque atualizarEstoque, GetProdutoById getProdutoById) {
        this.registrarProduto = registrarProduto;
        this.atualizarEstoque = atualizarEstoque;
        this.getProdutoById = getProdutoById;
    }

    @PostMapping
    public ResponseEntity<Produtos> criar(@RequestBody CreateProdutoRequest produtos){
        Produtos prod = registrarProduto.Create(produtos);
        return ResponseEntity.ok().body(prod);
    }

    @PutMapping
    public ResponseEntity<Long> AtualizarEstoque(@RequestHeader("userId") UUID IdUser, @PathVariable UUID IdProduto,@RequestParam Long Quantity){
        Long EstoqueAtualizado = atualizarEstoque.AtualizarEstoque(IdUser, IdProduto, Quantity);
        return ResponseEntity.ok().body(EstoqueAtualizado);
    }
}
