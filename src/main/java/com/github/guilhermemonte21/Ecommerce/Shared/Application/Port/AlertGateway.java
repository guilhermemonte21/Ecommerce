package com.github.guilhermemonte21.Ecommerce.Shared.Application.Port;

public interface AlertGateway {
    void enviarAlertaCritico(String titulo, String mensagem);
}
