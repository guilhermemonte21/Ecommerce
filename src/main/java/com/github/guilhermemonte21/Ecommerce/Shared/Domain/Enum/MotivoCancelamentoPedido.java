package com.github.guilhermemonte21.Ecommerce.Shared.Domain.Enum;

public enum MotivoCancelamentoPedido {
    FALHA_PAGAMENTO("Falha no processo de pagamento"),
    CANCELADO_PELO_USUARIO("Cancelado pelo usuário"),
    TIMEOUT_PAGAMENTO("Timeout: Pagamento não realizado em tempo hábil");

    private final String descricao;

    MotivoCancelamentoPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
