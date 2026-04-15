package com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Infra.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Application.UseCase.EnviarEmailNotificacao;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.AlertGateway;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AlertGatewayAdapter implements AlertGateway {

    private final EnviarEmailNotificacao enviarEmailNotificacao;
    private final String adminEmail = "gmonte003@gmail.com";

    public AlertGatewayAdapter(EnviarEmailNotificacao enviarEmailNotificacao) {
        this.enviarEmailNotificacao = enviarEmailNotificacao;
    }

    @Override
    public void enviarAlertaCritico(String titulo, String mensagem) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("titulo", titulo);
        variables.put("payload", mensagem);

        enviarEmailNotificacao.enviar(
                adminEmail,
                titulo,
                "alerta-dlq",
                variables
        );
    }
}
