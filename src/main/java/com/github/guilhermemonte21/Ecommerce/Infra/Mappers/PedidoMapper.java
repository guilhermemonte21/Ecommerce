package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PedidoMapper {
        private final UsuarioMapper usuarioMapper;


    public PedidoMapper(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    public Pedidos toDomain(PedidosEntity entity){
        Pedidos newPedido = new Pedidos();
        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setComprador(usuarioMapper.toDomain(entity.getComprador()));
        return newPedido;
    }

    public PedidosEntity toEntity(Pedidos entity){
        PedidosEntity newPedido = new PedidosEntity();

        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setComprador(usuarioMapper.toEntity(entity.getComprador()));
        return newPedido;
    }
}
