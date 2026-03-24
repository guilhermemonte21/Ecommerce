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
@RequestMapping("/pedidos")
public class PedidoController {
    private final ICriarPedido criarPedido;
    private final IGetItensByPedido getItensByPedido;

    public PedidoController(ICriarPedido criarPedido, IGetItensByPedido getItensByPedido) {
        this.criarPedido = criarPedido;
        this.getItensByPedido = getItensByPedido;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{idCarrinho}")
    public ResponseEntity<PedidoResponse> criarPedido(@PathVariable("idCarrinho") UUID idCarrinho, @RequestBody String endereco) {
        PedidoResponse newPedido = criarPedido.CriarPedido(idCarrinho, endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPedido);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{idPedido}/itens")
    public ResponseEntity<List<PedidoDoVendedor>> getItensDoPedido(@PathVariable("idPedido") UUID idPedido) {
        List<PedidoDoVendedor> itens = getItensByPedido.get(idPedido);
        return ResponseEntity.ok(itens);
    }
}
