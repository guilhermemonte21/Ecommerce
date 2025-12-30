package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.RegistrarProduto;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Produto")
public class ProdutoController {
    private final RegistrarProduto registrarProduto;

    public ProdutoController(RegistrarProduto registrarProduto) {
        this.registrarProduto = registrarProduto;
    }

    @PostMapping
    public ResponseEntity<Produtos> criar(@RequestBody CreateProdutoRequest produtos){
        Produtos prod = registrarProduto.Create(produtos);
        return ResponseEntity.ok().body(prod);
    }
}
