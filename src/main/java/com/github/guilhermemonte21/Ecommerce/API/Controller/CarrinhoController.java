package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoDTO.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho.IAddItemAoCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.CriarCarrinho.ICriarCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById.IGetCarrinhoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.LimparCarrinho.ILimparCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.RemoverItemDoCarrinho.IRemoverItemDoCarrinho;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carrinhos")
@Tag(name = "Carrinhos", description = "Gerenciamento de carrinhos de compras")
public class CarrinhoController {

    private static final Logger log = LoggerFactory.getLogger(CarrinhoController.class);

    private final IAddItemAoCarrinho add;
    private final ICriarCarrinho create;
    private final IGetCarrinhoById getById;
    private final IRemoverItemDoCarrinho remove;
    private final ILimparCarrinho limparCarrinho;

    public CarrinhoController(IAddItemAoCarrinho add, ICriarCarrinho create, IGetCarrinhoById getById,
                              IRemoverItemDoCarrinho remove, ILimparCarrinho limparCarrinho) {
        this.add = add;
        this.create = create;
        this.getById = getById;
        this.remove = remove;
        this.limparCarrinho = limparCarrinho;
    }

    @Operation(summary = "Criar novo carrinho")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<CarrinhoResponse> create(@RequestBody @Valid CreateCarrinhoRequest carrinhoRequest) {
        log.info("Criando novo carrinho");
        CarrinhoResponse newCarrinho = create.criar(carrinhoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCarrinho);
    }

    @Operation(summary = "Adicionar item ao carrinho")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{idCarrinho}/itens")
    public ResponseEntity<CarrinhoResponse> addItem(@PathVariable("idCarrinho") UUID idCarrinho,
                                                     @RequestParam UUID idProduto, @RequestBody Long quantity) {
        CarrinhoResponse newCarrinho = add.adicionarAoCarrinho(idCarrinho, idProduto, quantity);
        return ResponseEntity.ok(newCarrinho);
    }

    @Operation(summary = "Buscar carrinho por ID")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<CarrinhoResponse> getById(@PathVariable("id") UUID id) {
        CarrinhoResponse carrinhoResponse = getById.findCarrinhoById(id);
        return ResponseEntity.ok(carrinhoResponse);
    }

    @Operation(summary = "Remover item do carrinho")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{idCarrinho}/itens/{idProduto}")
    public ResponseEntity<Void> deleteItem(@PathVariable("idCarrinho") UUID idCarrinho,
                                            @PathVariable("idProduto") UUID idProduto) {
        remove.removerItem(idCarrinho, idProduto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Limpar todos os itens do carrinho")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{idCarrinho}/itens")
    public ResponseEntity<Void> clearCart(@PathVariable("idCarrinho") UUID idCarrinho) {
        limparCarrinho.limparCarrinho(idCarrinho);
        return ResponseEntity.noContent().build();
    }
}
