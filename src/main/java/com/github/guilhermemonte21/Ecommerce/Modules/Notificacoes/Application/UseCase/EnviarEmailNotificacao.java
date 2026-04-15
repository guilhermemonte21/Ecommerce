package com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Application.UseCase;

import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Application.Gateway.EmailGateway;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EnviarEmailNotificacao {

    private final EmailGateway emailGateway;
    private final TemplateEngine templateEngine;

    public EnviarEmailNotificacao(EmailGateway emailGateway, TemplateEngine templateEngine) {
        this.emailGateway = emailGateway;
        this.templateEngine = templateEngine;
    }

    public void enviar(String to, String subject, String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);

        String htmlContent = templateEngine.process("emails/" + templateName, context);

        emailGateway.enviarEmail(to, subject, htmlContent, true);
    }
}
