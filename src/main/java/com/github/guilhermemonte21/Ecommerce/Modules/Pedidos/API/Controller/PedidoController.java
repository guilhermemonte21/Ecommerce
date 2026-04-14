package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoDoVendedorResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.CriarPedido.ICriarPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetItensByPedido.IGetItensByPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.ChangePedidoStatus.IChangePedidoStatus;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetPedidoById.IGetPedidoById;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetPedidosByComprador.IGetPedidosByComprador;
import com.github.guilhermemonte21.Ecommerce.Shared.API.Idempotency.Idempotent;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);

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
    @PostMapping
    @RateLimiter(name = "checkoutRateLimiter", fallbackMethod = "fallbackRateLimit")
    @Bulkhead(name = "checkoutBulkhead", fallbackMethod = "fallbackBulkhead")
    public ResponseEntity<PedidoResponse> criarPedido(@RequestBody String endereco) {
        PedidoResponse newPedido = criarPedido.criarPedido(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPedido);
    }

    public ResponseEntity<PedidoResponse> fallbackRateLimit(String endereco, RequestNotPermitted e) {
        log.warn("Rate limit atingido no checkout: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    public ResponseEntity<PedidoResponse> fallbackBulkhead(String endereco, BulkheadFullException e) {
        log.warn("Bulkhead cheio no checkout: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
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

    @Operation(summary = "Buscar pedidos do comprador autenticado")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/meus-pedidos")
    public ResponseEntity<List<PedidoResponse>> getMeusPedidos() {
        List<PedidoResponse> response = getPedidosByComprador.getPedidosByComprador();
        return ResponseEntity.ok(response);
    }
}
