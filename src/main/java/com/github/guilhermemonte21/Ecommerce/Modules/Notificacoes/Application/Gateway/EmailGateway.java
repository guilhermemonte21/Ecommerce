package com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Application.Gateway;

public interface EmailGateway {
    void enviarEmail(String to, String subject, String body, boolean isHtml);
}
