package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.API.Idempotency.Idempotent;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoDoVendedorResponse;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido.ICriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido.IGetItensByPedido;
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

    public PedidoController(ICriarPedido criarPedido, IGetItensByPedido getItensByPedido) {
        this.criarPedido = criarPedido;
        this.getItensByPedido = getItensByPedido;
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
}
