package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Shared.API.Idempotency.Idempotent;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarEstoque.IAtualizarEstoque;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarProduto.IAtualizarProduto;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.BuscarTodosOsProdutos.IBuscarTodosOsProdutos;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.DeletarProduto.IDeletarProduto;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.GetProdutoById.IGetProdutoById;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.RegistrarProduto.IRegistrarProduto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produtos", description = "Gerenciamento de produtos")
public class ProdutoController {

    private static final Logger log = LoggerFactory.getLogger(ProdutoController.class);

    private final IRegistrarProduto registrarProduto;
    private final IAtualizarEstoque atualizarEstoque;
    private final IGetProdutoById getProdutoById;
    private final IBuscarTodosOsProdutos buscarTodosOsProdutos;
    private final IAtualizarProduto atualizarProduto;
    private final IDeletarProduto deletarProduto;

    public ProdutoController(IRegistrarProduto registrarProduto, IAtualizarEstoque atualizarEstoque,
            IGetProdutoById getProdutoById, IBuscarTodosOsProdutos buscarTodosOsProdutos,
            IAtualizarProduto atualizarProduto, IDeletarProduto deletarProduto) {
        this.registrarProduto = registrarProduto;
        this.atualizarEstoque = atualizarEstoque;
        this.getProdutoById = getProdutoById;
        this.buscarTodosOsProdutos = buscarTodosOsProdutos;
        this.atualizarProduto = atualizarProduto;
        this.deletarProduto = deletarProduto;
    }

    @Operation(summary = "Registrar novo produto")
    @PreAuthorize("isAuthenticated()")
    @Idempotent
    @Parameter(name = "Idempotency-Key", in = ParameterIn.HEADER, required = true, description = "Chave única da requisição (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid CreateProdutoRequest produtos) {
        log.info("Registrando novo produto: {}", produtos.nomeProduto());
        ProdutoResponse prod = registrarProduto.create(produtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(prod);
    }

    @Operation(summary = "Listar todos os produtos", description = "Listagem paginada de produtos")
    @GetMapping
    public ResponseEntity<Page<ProdutoResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nomeProduto") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ProdutoResponse> result = buscarTodosOsProdutos.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Buscar produto por ID")
    @GetMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponse> findById(@PathVariable("idProduto") UUID idProduto) {
        ProdutoResponse response = getProdutoById.getProdutoById(idProduto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar produto")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable("idProduto") UUID idProduto,
            @RequestBody @Valid CreateProdutoRequest request) {
        ProdutoResponse response = atualizarProduto.atualizar(idProduto, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar estoque do produto")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{idProduto}/estoque")
    public ResponseEntity<Long> atualizarEstoque(@PathVariable("idProduto") UUID idProduto,
            @RequestParam Long quantity) {
        Long estoqueAtualizado = atualizarEstoque.atualizarEstoque(idProduto, quantity);
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @Operation(summary = "Deletar produto")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{idProduto}")
    public ResponseEntity<Void> deletar(@PathVariable("idProduto") UUID idProduto) {
        deletarProduto.deletar(idProduto);
        return ResponseEntity.noContent().build();
    }
}
