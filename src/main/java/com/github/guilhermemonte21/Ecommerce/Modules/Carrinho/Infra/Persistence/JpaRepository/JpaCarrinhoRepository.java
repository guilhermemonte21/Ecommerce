package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Persistence.JpaRepository;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Persistence.Entity.Data.CarrinhoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaCarrinhoRepository extends JpaRepository<CarrinhoEntity, UUID> {

    CarrinhoEntity findByCompradorId(UUID id);
}
