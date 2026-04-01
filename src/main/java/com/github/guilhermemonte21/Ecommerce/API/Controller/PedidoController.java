package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.API.Idempotency.Idempotent;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoDoVendedorResponse;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido.ICriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido.IGetItensByPedido;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus.IChangePedidoStatus;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidoById.IGetPedidoById;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidosByComprador.IGetPedidosByComprador;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
public class PedidoController {

    private final ICriarPedido criarPedido;
    private final IGetItensByPedido getItensByPedido;
    private final IChangePedidoStatus changePedidoStatus;
    private final IGetPedidoById getPedidoById;
    private final IGetPedidosByComprador getPedidosByComprador;

    public PedidoController(ICriarPedido criarPedido, IGetItensByPedido getItensByPedido,
            IChangePedidoStatus changePedidoStatus, IGetPedidoById getPedidoById,
            IGetPedidosByComprador getPedidosByComprador) {
        this.criarPedido = criarPedido;
        this.getItensByPedido = getItensByPedido;
        this.changePedidoStatus = changePedidoStatus;
        this.getPedidoById = getPedidoById;
        this.getPedidosByComprador = getPedidosByComprador;
    }

    @Operation(summary = "Criar pedido a partir do carrinho")
    @PreAuthorize("isAuthenticated()")
    @Idempotent
    @Parameter(name = "Idempotency-Key", in = ParameterIn.HEADER, required = true, description = "Chave única da requisição (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    @PostMapping("/{idCarrinho}")
    public ResponseEntity<PedidoResponse> criarPedido(@RequestBody String endereco) {

        PedidoResponse newPedido = criarPedido.criarPedido(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPedido);
    }

    @Operation(summary = "Buscar itens de um pedido")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{idPedido}/itens")
    public ResponseEntity<List<PedidoDoVendedorResponse>> getItensDoPedido(
            @PathVariable("idPedido") UUID idPedido) {
        List<PedidoDoVendedorResponse> itens = getItensByPedido.get(idPedido);
        return ResponseEntity.ok(itens);
    }

    @Operation(summary = "Mudar status do pedido")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{idPedido}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable("idPedido") UUID idPedido) {
        changePedidoStatus.ChangePedidosStatus(idPedido);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Buscar pedido por ID")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoResponse> getPedidoById(@PathVariable("idPedido") UUID idPedido) {
        PedidoResponse response = getPedidoById.pedidoById(idPedido);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar pedidos do comprador")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/comprador/{idComprador}")
    public ResponseEntity<List<Pedidos>> getPedidosByComprador(@PathVariable("idComprador") UUID idComprador) {
        List<Pedidos> response = getPedidosByComprador.getPedidosByComprador(idComprador);
        return ResponseEntity.ok(response);
    }
}
