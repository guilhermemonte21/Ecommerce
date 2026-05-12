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

    @Query(value = """
    SELECT * FROM pedidos
    WHERE status_pedido = :status
    AND criado_em < :threshold
    FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<PedidosEntity> findByStatusAndCriadoEmBefore(@Param("status") String status, @Param("threshold") OffsetDateTime threshold);
}
