package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido.CriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido.ICriarPedido;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/Pedido")
public class PedidoController {
    private final ICriarPedido criarPedido;


    public PedidoController(ICriarPedido criarPedido) {
        this.criarPedido = criarPedido;
    }

    @PostMapping("CriarPedido/{idCarrinho}")
    public ResponseEntity<Pedidos> CriarPedido(@PathVariable("idCarrinho") UUID IdCarrinho){
        Pedidos newPedido = criarPedido.CriarPedido(IdCarrinho);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(newPedido);
    }
}
