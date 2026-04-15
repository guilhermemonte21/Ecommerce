package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Persistence.Entity.Data;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carrinho")
@Getter
@Setter
@NoArgsConstructor
public class CarrinhoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_carrinho")
    private UUID id;

    @Column(name = "comprador_id", nullable = false, unique = true)
    private UUID compradorId;


    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarrinhoItemEntity> itens = new ArrayList<>();

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;



}

