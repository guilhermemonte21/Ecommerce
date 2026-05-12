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
    private String vendedorNome;
    private OffsetDateTime occurredOn;

    @Override
    public String eventType() {
        return RabbitMQConfig.RK_PRODUTO_ALTERADO;
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }

    public static ProdutoAlteradoEvent criado(com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos produto, String vendedorNome) {
        return ProdutoAlteradoEvent.builder()
                .id(produto.getId())
                .nomeProduto(produto.getNomeProduto())
                .vendedorId(produto.getVendedorId())
                .vendedorNome(vendedorNome)
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                .estoque(produto.getEstoque())
                .tipoAlteracao(com.github.guilhermemonte21.Ecommerce.Shared.Domain.Enum.TipoAlteracaoProduto.CRIADO.name())
                .occurredOn(OffsetDateTime.now())
                .build();
    }

    public static ProdutoAlteradoEvent atualizado(com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos produto, String vendedorNome) {
        return ProdutoAlteradoEvent.builder()
                .id(produto.getId())
                .nomeProduto(produto.getNomeProduto())
                .vendedorId(produto.getVendedorId())
                .vendedorNome(vendedorNome)
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                .estoque(produto.getEstoque())
                .tipoAlteracao(com.github.guilhermemonte21.Ecommerce.Shared.Domain.Enum.TipoAlteracaoProduto.ATUALIZADO.name())
                .occurredOn(OffsetDateTime.now())
                .build();
    }

    public static ProdutoAlteradoEvent deletado(UUID id) {
        return ProdutoAlteradoEvent.builder()
                .id(id)
                .tipoAlteracao(com.github.guilhermemonte21.Ecommerce.Shared.Domain.Enum.TipoAlteracaoProduto.DELETADO.name())
                .occurredOn(OffsetDateTime.now())
                .build();
    }
}
