package com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Infra.Config;

import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Application.Gateway.EmailGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Application.UseCase.EnviarEmailNotificacao;
import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Infra.Event.Consumer.NotificacaoPagamentoConsumer;
import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Infra.Event.Consumer.NotificacaoPedidoCanceladoConsumer;
import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Infra.Event.Consumer.NotificacaoPedidoCriadoConsumer;
import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Infra.Gateway.AlertGatewayAdapter;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.AlertGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;

@Configuration
public class NotificacaoModuleConfig {

    @Bean
    public EnviarEmailNotificacao enviarEmailNotificacao(EmailGateway emailGateway, TemplateEngine templateEngine) {
        return new EnviarEmailNotificacao(emailGateway, templateEngine);
    }

    @Bean
    public NotificacaoPedidoCriadoConsumer notificacaoPedidoCriadoConsumer(
            EnviarEmailNotificacao enviarEmailNotificacao,
            UsuarioGateway usuarioGateway,
            PedidoGateway pedidoGateway) {
        return new NotificacaoPedidoCriadoConsumer(enviarEmailNotificacao, usuarioGateway, pedidoGateway);
    }

    @Bean
    public NotificacaoPagamentoConsumer notificacaoPagamentoConsumer(
            EnviarEmailNotificacao enviarEmailNotificacao,
            UsuarioGateway usuarioGateway,
            PedidoGateway pedidoGateway) {
        return new NotificacaoPagamentoConsumer(enviarEmailNotificacao, usuarioGateway, pedidoGateway);
    }
    @Bean
    public NotificacaoPedidoCanceladoConsumer notificacaoPedidoCanceladoConsumer(
            EnviarEmailNotificacao enviarEmailNotificacao,
            UsuarioGateway usuarioGateway,
            PedidoGateway pedidoGateway) {
        return new NotificacaoPedidoCanceladoConsumer(enviarEmailNotificacao, usuarioGateway, pedidoGateway);
    }

    @Bean
    public AlertGateway alertGateway(EnviarEmailNotificacao enviarEmailNotificacao) {
        return new AlertGatewayAdapter(enviarEmailNotificacao);
    }
}
