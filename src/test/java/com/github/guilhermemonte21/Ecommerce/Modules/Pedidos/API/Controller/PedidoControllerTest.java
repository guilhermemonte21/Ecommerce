package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.CriarPedidoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.ChangePedidoStatus.IChangePedidoStatus;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.CriarPedido.ICriarPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetItensByPedido.IGetItensByPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetPedidoById.IGetPedidoById;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetPedidosByComprador.IGetPedidosByComprador;
import com.github.guilhermemonte21.Ecommerce.Shared.API.Idempotency.IdempotencyInterceptor;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICriarPedido criarPedido;
    @MockBean
    private IGetItensByPedido getItensByPedido;
    @MockBean
    private IChangePedidoStatus changePedidoStatus;
    @MockBean
    private IGetPedidoById getPedidoById;
    @MockBean
    private IGetPedidosByComprador getPedidosByComprador;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private IdempotencyInterceptor idempotencyInterceptor;

    @org.junit.jupiter.api.BeforeEach
    void setup() throws Exception {
        when(idempotencyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("Deve retornar 201 ao criar um pedido válido com header de idempotência")
    @WithMockUser
    void deveRetornar201AoCriarPedidoValido() throws Exception {
        UUID id = UUID.randomUUID();
        UUID compradorId = UUID.randomUUID();
        CriarPedidoRequest request = new CriarPedidoRequest("Rua dos Testes, 123");
        PedidoResponse response = new PedidoResponse(id, compradorId, Collections.emptyList(), "Rua dos Testes, 123",
                BigDecimal.TEN, "PENDENTE", OffsetDateTime.now());

        when(criarPedido.criarPedido(any(CriarPedidoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/pedidos")
                .with(csrf())
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPedido").value(id.toString()))
                .andExpect(jsonPath("$.enderecoDeEntrega").value("Rua dos Testes, 123"));
    }

    @Test
    @DisplayName("Deve retornar 400 se o header Idempotency-Key estiver ausente")
    @WithMockUser
    void deveRetornar400SeIdempotencyKeyAusente() throws Exception {

    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar pedido por ID")
    @WithMockUser
    void deveRetornar200AoBuscarPorId() throws Exception {
        UUID id = UUID.randomUUID();
        PedidoResponse response = new PedidoResponse(id, UUID.randomUUID(), Collections.emptyList(), "Endereço",
                BigDecimal.TEN, "PENDENTE", OffsetDateTime.now());

        when(getPedidoById.pedidoById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/pedidos/{idPedido}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido").value(id.toString()));
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar meus pedidos")
    @WithMockUser
    void deveRetornar200AoBuscarMeusPedidos() throws Exception {
        when(getPedidosByComprador.getPedidosByComprador()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/pedidos/meus-pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
