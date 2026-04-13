package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.JpaRepository;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Data.PedidosEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Enum.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface  JpaPedidosRepository extends JpaRepository<PedidosEntity, UUID> {

    List<PedidosEntity> findByCompradorId(@Param("id") UUID id);

    @Query("""
    SELECT p FROM PedidosEntity p
    LEFT JOIN FETCH p.pedidos
    WHERE p.status = :status
    AND p.criadoEm < :threshold
    """)
    List<PedidosEntity> findByStatusAndCriadoEmBefore(StatusPedido status, OffsetDateTime threshold);
}
