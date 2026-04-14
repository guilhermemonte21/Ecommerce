package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.DesativarConta;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import org.springframework.transaction.annotation.Transactional;

public class MudarAtividadeDaConta implements IMudarAtividadeDaConta {
    private final UsuarioGateway gateway;
    private final UsuarioAutenticadoGateway authGateway;

    public MudarAtividadeDaConta(UsuarioGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.authGateway = authGateway;
    }

    @Override
    @Transactional
    public Boolean mudarAtividade() {
        UsuarioAutenticado user = authGateway.get();
        Usuarios usuario = gateway.findByEmail(user.getUser().getEmail())
                .orElseThrow(() -> new com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioNotFoundException(user.getUser().getId()));
        usuario.setAtivo(!usuario.getAtivo());
        gateway.salvar(usuario);
        return usuario.getAtivo();
    }
}
