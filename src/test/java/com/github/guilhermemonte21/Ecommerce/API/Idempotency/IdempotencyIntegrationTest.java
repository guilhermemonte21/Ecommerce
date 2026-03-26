package com.github.guilhermemonte21.Ecommerce.API.Idempotency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido.ICriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;

import java.util.UUID;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IdempotencyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICriarPedido criarPedido;

    @Test
    @DisplayName("Deve retornar 400 quando o Idempotency-Key não é enviado")
    @WithMockUser
    void deveRetornar400QuandoChaveAusente() throws Exception {
        mockMvc.perform(post("/api/v1/pedidos/" + UUID.randomUUID())
                .content("Endereço de Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar o mesmo resultado e chamar o use case apenas uma vez")
    @WithMockUser
    void deveRetornarMesmoResultadoParaChaveDuplicada() throws Exception {
        String key = UUID.randomUUID().toString();
        UUID idCarrinho = UUID.randomUUID();

        PedidoResponse mockResponse = new PedidoResponse(UUID.randomUUID(), "Teste", null, "Endereço", BigDecimal.ZERO,
                OffsetDateTime.now());
        when(criarPedido.criarPedido(any(), any())).thenReturn(mockResponse);

        MvcResult result1 = mockMvc.perform(post("/api/v1/pedidos/" + idCarrinho)
                .header("Idempotency-Key", key)
                .content("Endereço de Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String content1 = result1.getResponse().getContentAsString();

        MvcResult result2 = mockMvc.perform(post("/api/v1/pedidos/" + idCarrinho)
                .header("Idempotency-Key", key)
                .content("Endereço de Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(content1, result2.getResponse().getContentAsString());

        verify(criarPedido, times(1)).criarPedido(any(), any());
    }
}
