package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido.ICriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido.IGetItensByPedido;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Pedido")
public class PedidoController {
    private final ICriarPedido criarPedido;
    private final IGetItensByPedido getItensByPedido;

    public PedidoController(ICriarPedido criarPedido, IGetItensByPedido getItensByPedido) {
        this.criarPedido = criarPedido;
        this.getItensByPedido = getItensByPedido;
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("CriarPedido/{idCarrinho}")
    public ResponseEntity<PedidoResponse> CriarPedido(@PathVariable("idCarrinho") UUID IdCarrinho){
        PedidoResponse newPedido = criarPedido.CriarPedido(IdCarrinho);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPedido);
    }
    @PreAuthorize("isAuthenticated() ")
    @GetMapping("/{IdPedido}/itens")
    public ResponseEntity<List<PedidoDoVendedor>> GetItensDoPedido(@PathVariable UUID IdPedido){
        List<PedidoDoVendedor> get = getItensByPedido.get(IdPedido);
        return ResponseEntity.status(HttpStatus.OK).body(get);
    }
}
