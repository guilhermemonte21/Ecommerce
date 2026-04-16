package com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Application.UseCase.Pagamento.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.*;

@Configuration
public class PagamentoModuleConfig {

    @Bean
    IPagamento pagamento(PedidoGateway pedidoGateway, PagamentoGateway pagamentoGateway,
            EventPublisher eventPublisher, UsuarioAutenticadoGateway authGateway) {
        return new Pagamento(pedidoGateway, pagamentoGateway, eventPublisher, authGateway);
    }
}
