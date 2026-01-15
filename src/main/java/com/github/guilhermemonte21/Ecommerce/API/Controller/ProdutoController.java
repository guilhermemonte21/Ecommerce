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
@RequestMapping("/Produto")
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
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid CreateProdutoRequest produtos){
        ProdutoResponse prod = registrarProduto.Create(produtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(prod);
    }

    @PreAuthorize("isAuthenticated() ")
    @PutMapping("/{id}")
    public ResponseEntity<Long> AtualizarEstoque( @PathVariable("id") UUID IdProduto,@RequestParam Long Quantity){
        Long EstoqueAtualizado = atualizarEstoque.AtualizarEstoque( IdProduto, Quantity);
        return ResponseEntity.ok().body(EstoqueAtualizado);
    }
    @GetMapping("FindAll")
    public ResponseEntity<List<ProdutoResponse>> findAll(){
        List<ProdutoResponse> list = buscarTodosOsProdutos.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("Atualizar/{IdProduto}")
    public ResponseEntity<ProdutoResponse>Atualizar(@PathVariable("IdProduto") UUID IdProduto, @RequestBody CreateProdutoRequest request){
        ProdutoResponse response = atualizarProduto.Atualizar(IdProduto, request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("FindById/{IdProduto}")
    public ResponseEntity<ProdutoResponse> findById(@PathVariable("IdProduto") UUID IdProduto){
        ProdutoResponse response = getProdutoById.GetProdutoById(IdProduto);
        return ResponseEntity.ok(response);
    }
}
