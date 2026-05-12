package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.API.Controller;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarEstoque.IAtualizarEstoque;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarProduto.IAtualizarProduto;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.DeletarProduto.IDeletarProduto;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.RegistrarProduto.IRegistrarProduto;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.BuscarTodosOsProdutos.IBuscarTodosOsProdutos;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.GetProdutoById.IGetProdutoById;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
@AutoConfigureMockMvc
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IRegistrarProduto registrarProduto;
    @MockBean
    private IAtualizarEstoque atualizarEstoque;
    @MockBean
    private IGetProdutoById getProdutoById;
    @MockBean
    private IBuscarTodosOsProdutos buscarTodosOsProdutos;
    @MockBean
    private IAtualizarProduto atualizarProduto;
    @MockBean
    private IDeletarProduto deletarProduto;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private IdempotencyInterceptor idempotencyInterceptor;

    @org.junit.jupiter.api.BeforeEach
    void setup() throws Exception {
        when(idempotencyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("Deve retornar 201 ao criar um produto válido")
    @WithMockUser
    void deveRetornar201AoCriarProdutoValido() throws Exception {
        UUID id = UUID.randomUUID();
        CreateProdutoRequest request = new CreateProdutoRequest("Produto Teste", "Descrição", BigDecimal.TEN, 10L);
        ProdutoResponse response = new ProdutoResponse(id, "Produto Teste", "Vendedor", BigDecimal.TEN, "Descrição", 10L);

        when(registrarProduto.create(any(CreateProdutoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/produtos")
                .with(csrf())
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.IdProduto").value(id.toString()))
                .andExpect(jsonPath("$.NomeProduto").value("Produto Teste"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar produto por ID")
    @WithMockUser
    void deveRetornar200AoBuscarPorId() throws Exception {
        UUID id = UUID.randomUUID();
        ProdutoResponse response = new ProdutoResponse(id, "Produto Teste", "Vendedor", BigDecimal.TEN, "Descrição", 10L);

        when(getProdutoById.getProdutoById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.IdProduto").value(id.toString()));
    }

    @Test
    @DisplayName("Deve retornar 204 ao deletar produto")
    @WithMockUser
    void deveRetornar204AoDeletar() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(deletarProduto).deletar(id);

        mockMvc.perform(delete("/api/v1/produtos/{id}", id)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(deletarProduto).deletar(id);
    }
}
