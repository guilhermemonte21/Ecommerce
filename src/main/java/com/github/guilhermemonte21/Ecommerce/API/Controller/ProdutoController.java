package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque.IAtualizarEstoque;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.GetProdutoById.IGetProdutoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.RegistrarProduto.IRegistrarProduto;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/Produto")
public class ProdutoController {
    private final IRegistrarProduto registrarProduto;
    private final IAtualizarEstoque atualizarEstoque;
    private final IGetProdutoById getProdutoById;

    public ProdutoController(IRegistrarProduto registrarProduto, IAtualizarEstoque atualizarEstoque, IGetProdutoById getProdutoById) {
        this.registrarProduto = registrarProduto;
        this.atualizarEstoque = atualizarEstoque;
        this.getProdutoById = getProdutoById;
    }

    @PostMapping
    public ResponseEntity<Produtos> criar(@RequestBody CreateProdutoRequest produtos){
        Produtos prod = registrarProduto.Create(produtos);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(prod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> AtualizarEstoque(@RequestHeader("userId") UUID IdUser, @PathVariable("id") UUID IdProduto,@RequestParam Long Quantity){
        Long EstoqueAtualizado = atualizarEstoque.AtualizarEstoque(IdUser, IdProduto, Quantity);
        return ResponseEntity.ok().body(EstoqueAtualizado);
    }
}
