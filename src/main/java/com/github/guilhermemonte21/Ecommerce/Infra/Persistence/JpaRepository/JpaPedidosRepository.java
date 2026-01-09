package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository;

import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface  JpaPedidosRepository extends JpaRepository<PedidosEntity, UUID> {

    @Query("SELECT p FROM PedidosEntity p WHERE p.comprador.id = :id")
    List<PedidosEntity> getPedidosByComprador(UUID Id);
}
