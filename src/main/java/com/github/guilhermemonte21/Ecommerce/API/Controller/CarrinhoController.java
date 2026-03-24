package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoDTO.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho.IAddItemAoCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.CriarCarrinho.ICriarCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById.IGetCarrinhoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.LimparCarrinho.ILimparCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.RemoverItemDoCarrinho.IRemoverItemDoCarrinho;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carrinhos")
public class CarrinhoController {
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<CarrinhoResponse> create(@RequestBody @Valid CreateCarrinhoRequest carrinhoRequest) {
        CarrinhoResponse newCarrinho = create.Criar(carrinhoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCarrinho);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{idCarrinho}/itens")
    public ResponseEntity<CarrinhoResponse> addItem(@PathVariable("idCarrinho") UUID idCarrinho,
            @RequestParam UUID idProduto, @RequestBody Long quantity) {
        CarrinhoResponse newCarrinho = add.AdicionarAoCarrinho(idCarrinho, idProduto, quantity);
        return ResponseEntity.ok(newCarrinho);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<CarrinhoResponse> getById(@PathVariable("id") UUID id) {
        CarrinhoResponse carrinhoResponse = getById.FindCarrinhoById(id);
        return ResponseEntity.ok(carrinhoResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{idCarrinho}/itens/{idProduto}")
    public ResponseEntity<Void> deleteItem(@PathVariable("idCarrinho") UUID idCarrinho,
            @PathVariable("idProduto") UUID idProduto) {
        remove.RemoverItem(idCarrinho, idProduto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{idCarrinho}/itens")
    public ResponseEntity<Void> clearCart(@PathVariable("idCarrinho") UUID idCarrinho) {
        limparCarrinho.LimparCarrinho(idCarrinho);
        return ResponseEntity.noContent().build();
    }
}

