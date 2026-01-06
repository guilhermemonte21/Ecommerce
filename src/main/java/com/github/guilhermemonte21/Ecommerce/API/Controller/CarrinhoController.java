package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho.IAddItemAoCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.CriarCarrinho.ICriarCarrinho;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById.IGetCarrinhoById;
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

    public CarrinhoController(IAddItemAoCarrinho add, ICriarCarrinho create, IGetCarrinhoById getById, IRemoverItemDoCarrinho remove) {
        this.add = add;
        this.create = create;
        this.getById = getById;
        this.remove = remove;
    }

    @PostMapping("/Create")
    public ResponseEntity<Carrinho> create(@RequestBody @Valid CreateCarrinhoRequest carrinho){
        Carrinho newCarrinho = create.Criar(carrinho);
        System.out.println(carrinho.getProdutosIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(newCarrinho);
    }

    @PostMapping("/Add")
    public ResponseEntity<Carrinho> addItem(UUID Id, UUID IdProduto){
        Carrinho newCarrinho = add.AdicionarAoCarrinho(Id, IdProduto);

        return ResponseEntity.ok(newCarrinho);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Carrinho>> getById(@PathVariable("id") UUID Id){
        Optional<Carrinho> carrinhobyId = getById.FindCarrinhoById(Id);
        if (!carrinhobyId.isEmpty()){
           return ResponseEntity.status(HttpStatus.FOUND).body(carrinhobyId);
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

}
