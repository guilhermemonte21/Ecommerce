package com.github.guilhermemonte21.Ecommerce.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento.Pagamento;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/pagamento")
@AllArgsConstructor
@RestController
public class PagamentoController {
    private final Pagamento pay;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{idPedido}")
    public ResponseEntity<Void> CriarPedido(@PathVariable("idPedido") UUID IdPedido){
        pay.pagar(IdPedido);
        return ResponseEntity.ok().build();
    }

}
