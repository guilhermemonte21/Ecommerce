package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque.IAtualizarEstoque;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarProduto.IAtualizarProduto;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.BuscarTodosOsProdutos.IBuscarTodosOsProdutos;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.GetProdutoById.IGetProdutoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.RegistrarProduto.IRegistrarProduto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final IRegistrarProduto registrarProduto;
    private final IAtualizarEstoque atualizarEstoque;
    private final IGetProdutoById getProdutoById;
    private final IBuscarTodosOsProdutos buscarTodosOsProdutos;
    private final IAtualizarProduto atualizarProduto;

    public ProdutoController(IRegistrarProduto registrarProduto, IAtualizarEstoque atualizarEstoque, IGetProdutoById getProdutoById, IBuscarTodosOsProdutos buscarTodosOsProdutos, IAtualizarProduto atualizarProduto) {
        this.registrarProduto = registrarProduto;
        this.atualizarEstoque = atualizarEstoque;
        this.getProdutoById = getProdutoById;
        this.buscarTodosOsProdutos = buscarTodosOsProdutos;
        this.atualizarProduto = atualizarProduto;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid CreateProdutoRequest produtos) {
        ProdutoResponse prod = registrarProduto.Create(produtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(prod);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> findAll() {
        List<ProdutoResponse> list = buscarTodosOsProdutos.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponse> findById(@PathVariable("idProduto") UUID idProduto) {
        ProdutoResponse response = getProdutoById.GetProdutoById(idProduto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable("idProduto") UUID idProduto, @RequestBody CreateProdutoRequest request) {
        ProdutoResponse response = atualizarProduto.Atualizar(idProduto, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{idProduto}/estoque")
    public ResponseEntity<Long> atualizarEstoque(@PathVariable("idProduto") UUID idProduto, @RequestParam Long quantity) {
        Long estoqueAtualizado = atualizarEstoque.AtualizarEstoque(idProduto, quantity);
        return ResponseEntity.ok(estoqueAtualizado);
    }
}
