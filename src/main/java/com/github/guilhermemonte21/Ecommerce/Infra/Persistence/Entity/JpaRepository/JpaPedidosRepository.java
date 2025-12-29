package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository;

import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPedidosRepository extends JpaRepository<PedidosEntity, UUID> {
}
