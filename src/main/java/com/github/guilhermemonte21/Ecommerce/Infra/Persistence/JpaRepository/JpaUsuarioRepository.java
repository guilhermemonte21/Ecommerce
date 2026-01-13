package com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository;

import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface JpaUsuarioRepository extends JpaRepository<UsuariosEntity, UUID> {
    @Query("SELECT u from UsuariosEntity u Where u.Email = :email")
    UsuariosEntity findByEmail(@Param("email")String Email);
}
