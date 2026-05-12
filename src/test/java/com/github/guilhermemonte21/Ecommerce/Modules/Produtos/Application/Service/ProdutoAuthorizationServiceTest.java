package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Service;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Enum.TipoUsuario;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioInativoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoAuthorizationServiceTest {

    @Mock
    private UsuarioAutenticadoGateway authGateway;

    @InjectMocks
    private ProdutoAuthorizationService authorizationService;

    @Test
    @DisplayName("Deve validar proprietário com sucesso")
    void deveValidarProprietarioComSucesso() {
        UUID userId = UUID.randomUUID();
        Usuarios usuario = new Usuarios();
        usuario.setId(userId);
        usuario.setAtivo(true);
        UsuarioAutenticado authUser = new UsuarioAutenticado(usuario);

        when(authGateway.get()).thenReturn(authUser);

        assertDoesNotThrow(() -> authorizationService.validarProprietario(userId));
    }

    @Test
    @DisplayName("Deve lançar AcessoNegadoException se não for o proprietário")
    void deveLancarAcessoNegadoSeNaoForProprietario() {
        UUID userId = UUID.randomUUID();
        UUID outroId = UUID.randomUUID();
        Usuarios usuario = new Usuarios();
        usuario.setId(userId);
        usuario.setAtivo(true);
        UsuarioAutenticado authUser = new UsuarioAutenticado(usuario);

        when(authGateway.get()).thenReturn(authUser);

        assertThrows(AcessoNegadoException.class, () -> authorizationService.validarProprietario(outroId));
    }

    @Test
    @DisplayName("Deve lançar UsuarioInativoException se o usuário estiver inativo")
    void deveLancarUsuarioInativoSeUsuarioInativo() {
        UUID userId = UUID.randomUUID();
        Usuarios usuario = new Usuarios();
        usuario.setId(userId);
        usuario.setAtivo(false);
        UsuarioAutenticado authUser = new UsuarioAutenticado(usuario);

        when(authGateway.get()).thenReturn(authUser);

        assertThrows(UsuarioInativoException.class, () -> authorizationService.validarProprietario(userId));
    }

    @Test
    @DisplayName("Deve validar vendedor ativo com sucesso")
    void deveValidarVendedorAtivoComSucesso() {
        Usuarios usuario = new Usuarios();
        usuario.setId(UUID.randomUUID());
        usuario.setAtivo(true);
        usuario.setTipoUsuario(TipoUsuario.VENDEDOR.name());
        UsuarioAutenticado authUser = new UsuarioAutenticado(usuario);

        when(authGateway.get()).thenReturn(authUser);

        assertDoesNotThrow(() -> authorizationService.validarVendedorAtivo());
    }

    @Test
    @DisplayName("Deve lançar AcessoNegadoException se não for um vendedor")
    void deveLancarAcessoNegadoSeNaoForVendedor() {
        Usuarios usuario = new Usuarios();
        usuario.setId(UUID.randomUUID());
        usuario.setAtivo(true);
        usuario.setTipoUsuario(TipoUsuario.COMPRADOR.name());
        UsuarioAutenticado authUser = new UsuarioAutenticado(usuario);

        when(authGateway.get()).thenReturn(authUser);

        assertThrows(AcessoNegadoException.class, () -> authorizationService.validarVendedorAtivo());
    }
}
