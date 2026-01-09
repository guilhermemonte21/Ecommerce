package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository;

import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidoDoVendedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPedidoDoVendedorRepo extends JpaRepository<PedidoDoVendedorEntity, UUID> {
}
