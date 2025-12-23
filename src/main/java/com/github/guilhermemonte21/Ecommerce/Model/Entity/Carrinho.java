package com.github.guilhermemonte21.Ecommerce.Model.Entity;

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
@Table(name = "Carrinho")
@Getter
@Setter
@NoArgsConstructor
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "IdCarrinho")
    private UUID Id;

    @Column(name = "Comprador")
    @OneToOne
    private Usuarios Comprador;

    @Column(name = "ProdutosCarrinho")
    private List<Produtos> Itens = new ArrayList<>();

    @Column(name = "ValorTotal")
    private BigDecimal ValorTotal;

    @Column(name = "AtualizadoEm")
    private OffsetDateTime AtualizadoEm;
}
