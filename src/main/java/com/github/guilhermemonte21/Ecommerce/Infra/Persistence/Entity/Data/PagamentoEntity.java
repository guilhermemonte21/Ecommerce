package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data;

import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Enum.StatusPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
    @Table(name = "pagamentos")
    @Getter
    @Setter
    @NoArgsConstructor
    public class PagamentoEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Column(name = "id_pagamento")
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "pedido_id", nullable = false)
        private PedidosEntity pedido;

        @Column(name = "gateway_payment_id", nullable = false)
        private String gatewayPaymentId;

        @Enumerated(EnumType.STRING)
        @Column(name = "metodo_pagamento", nullable = false)
        private MetodoPagamento metodoPagamento;


        @Column(name = "valor_total", nullable = false)
        private BigDecimal valorTotal;


        @Enumerated(EnumType.STRING)
        @Column(name = "status_pagamento", nullable = false)
        private StatusPagamento status;

        @Column(name = "criado_em", nullable = false)
        private OffsetDateTime criadoEm = OffsetDateTime.now();


        @Column(name = "atualizado_em")
        private OffsetDateTime atualizadoEm;
    }

