package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "produtos")
@Setting(shards = 1, replicas = 0)
public class ProductDocument {

    @Id
    private UUID id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "portuguese", searchAnalyzer = "portuguese"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword),
                    @InnerField(suffix = "suggest", type = FieldType.Text)
            }
    )
    private String nomeProduto;

    @Field(type = FieldType.Keyword)
    private UUID vendedorId;

    @Field(type = FieldType.Text)
    private String vendedorNome;

    @Field(type = FieldType.Text, analyzer = "portuguese")
    private String descricao;

    @Field(type = FieldType.Double)
    private BigDecimal preco;

    @Field(type = FieldType.Long)
    private Long estoque;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSZ")
    private java.time.Instant createdAt;
}
