package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho.IAddItemAoCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.CriarCarrinho.ICriarCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById.IGetCarrinhoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.LimparCarrinho.ILimparCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.RemoverItemDoCarrinho.IRemoverItemDoCarrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/Carrinho")
public class CarrinhoController {
    private final IAddItemAoCarrinho add;
    private final ICriarCarrinho create;
    private final IGetCarrinhoById getById;
    private final IRemoverItemDoCarrinho remove;
    private final ILimparCarrinho limparCarrinho;

    public CarrinhoController(IAddItemAoCarrinho add, ICriarCarrinho create, IGetCarrinhoById getById, IRemoverItemDoCarrinho remove, ILimparCarrinho limparCarrinho) {
        this.add = add;
        this.create = create;
        this.getById = getById;
        this.remove = remove;
        this.limparCarrinho = limparCarrinho;
    }

    @PostMapping("/Create")
    public ResponseEntity<Carrinho> create(@RequestBody @Valid CreateCarrinhoRequest carrinho){
        Carrinho newCarrinho = create.Criar(carrinho);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCarrinho);
    }

    @PostMapping("/Add/{idCarrinho}")
    public ResponseEntity<Carrinho> addItem(@PathVariable("idCarrinho") UUID Id,  UUID IdProduto,@RequestBody Long quantity){
        Carrinho newCarrinho = add.AdicionarAoCarrinho(Id, IdProduto, quantity);

        return ResponseEntity.ok(newCarrinho);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Carrinho>> getById(@PathVariable("id") UUID Id){
        Optional<Carrinho> carrinhobyId = getById.FindCarrinhoById(Id);
        if (!carrinhobyId.isEmpty()){
           return ResponseEntity.status(HttpStatus.OK).body(carrinhobyId);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}/{Id}")
    public ResponseEntity<Void> DeleteItem(@PathVariable("id") UUID Id,@PathVariable("Id") UUID id){
        remove.RemoverItem(Id, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{IdCarrinho}")
    public ResponseEntity<Void> LimparCarrinho(@PathVariable("IdCarrinho") UUID IdCarrinho){
        limparCarrinho.LimparCarrinho(IdCarrinho);
        return ResponseEntity.noContent().build();
    }

}
