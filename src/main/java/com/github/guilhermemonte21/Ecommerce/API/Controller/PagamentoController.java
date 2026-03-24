package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento.IPagamento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/pagamentos")
@AllArgsConstructor
@RestController
@Tag(name = "Pagamentos", description = "Processamento de pagamentos")
public class PagamentoController {

    private static final Logger log = LoggerFactory.getLogger(PagamentoController.class);

    private final IPagamento pagamento;

    @Operation(summary = "Confirmar pagamento de um pedido")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{idPedido}")
    public ResponseEntity<Void> pagar(@PathVariable("idPedido") UUID idPedido) {
        log.info("Processando pagamento para pedido: {}", idPedido);
        pagamento.pagar(idPedido);
        return ResponseEntity.ok().build();
    }
}
