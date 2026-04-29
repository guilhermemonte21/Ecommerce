package com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoAlteradoEvent implements DomainEvent {
    private UUID id;
    private String nomeProduto;
    private UUID vendedorId;
    private String descricao;
    private BigDecimal preco;
    private Long estoque;
    private String tipoAlteracao;
    private OffsetDateTime occurredOn;

    @Override
    public String eventType() {
        return RabbitMQConfig.RK_PRODUTO_ALTERADO;
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
