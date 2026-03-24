package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento.IPagamento;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/pagamentos")
@AllArgsConstructor
@RestController
public class PagamentoController {
    private final IPagamento pagamento;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{idPedido}")
    public ResponseEntity<Void> pagar(@PathVariable("idPedido") UUID idPedido){
        pagamento.pagar(idPedido);
        return ResponseEntity.ok().build();
    }

}
