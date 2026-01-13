package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data;

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

    @OneToOne
    @JoinColumn(name = "comprador_id", nullable = false, unique = true)
    private UsuariosEntity comprador;


    @ManyToMany
    @JoinTable(
            name = "carrinho_produtos",
            joinColumns = @JoinColumn(name = "carrinho_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<ProdutosEntity> itens = new ArrayList<>();

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;



}

