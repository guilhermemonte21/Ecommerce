package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.CreateSellerAcount;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.UsuarioResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Mappers.UsuarioMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class CreateSellerAcount implements ICreateSellerAcount {

    private static final Logger log = LoggerFactory.getLogger(CreateSellerAcount.class);

    private final UsuarioGateway gateway;
    private final UsuarioMapperApl mapperApl;
    private final UsuarioAutenticadoGateway authGateway;

    public CreateSellerAcount(UsuarioGateway gateway, UsuarioMapperApl mapperApl,
                              UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
        this.authGateway = authGateway;
    }

    @Override
    @Transactional
    public UsuarioResponse create(String stripeAccountId) {
        UsuarioAutenticado auth = authGateway.get();
        Usuarios user = gateway.findByEmail(auth.getUser().getEmail());

        user.setTipoUsuario("Vendedor");
        user.setStripeAccountId(stripeAccountId);

        Usuarios salvo = gateway.salvar(user);
        log.info("Conta de vendedor criada: id={}, email={}", salvo.getId(), salvo.getEmail());
        return mapperApl.toResponse(salvo);
    }
}
