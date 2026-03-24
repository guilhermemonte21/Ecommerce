package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento;

import java.util.UUID;

public interface IPagamento {
    Boolean pagar(UUID IdPedido);
}
