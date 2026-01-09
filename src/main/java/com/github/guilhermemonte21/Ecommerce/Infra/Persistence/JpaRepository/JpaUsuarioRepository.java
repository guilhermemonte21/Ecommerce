package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository;

import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUsuarioRepository extends JpaRepository<UsuariosEntity, UUID> {
}
