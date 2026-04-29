package com.github.guilhermemonte21.Ecommerce.Modules.Produtos;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.RegistrarProduto.IRegistrarProduto;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.BuscarTodosOsProdutos.IBuscarTodosOsProdutos;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Repository.ProductElasticRepository;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
public class ProdutoCqrsIntegrationTest {

    @Autowired
    private IRegistrarProduto registrarProduto;

    @Autowired
    private IBuscarTodosOsProdutos buscarTodosOsProdutos;

    @Autowired
    private ProductElasticRepository productElasticRepository;

    @Autowired
    private UsuarioGateway usuarioGateway;

    @MockBean
    private UsuarioAutenticadoGateway authGateway;

    @MockBean
    private com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher eventPublisher;

    private UUID vendedorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Usuarios usuario = new Usuarios();
        usuario.setId(vendedorId);
        usuario.setAtivo(true);
        usuario.setTipoUsuario("Vendedor");
        usuario.setNome("Vendedor Teste");
        usuario.setEmail("vendedor@teste.com");
        usuario.setSenha("123456");

        usuarioGateway.salvar(usuario);

        UsuarioAutenticado auth = new UsuarioAutenticado(usuario);
        Mockito.when(authGateway.get()).thenReturn(auth);

        productElasticRepository.deleteAll();
    }

    @Test
    void deveRegistrarProdutoESincronizarComElasticsearch() {
        CreateProdutoRequest request = new CreateProdutoRequest(
                "Smartphone XPTO",
                "Um smartphone muito rápido",
                new BigDecimal("1500.00"),
                10L);

        ProdutoResponse response = registrarProduto.create(request);

        assertThat(response.IdProduto()).isNotNull();
        assertThat(response.NomeProduto()).isEqualTo("Smartphone XPTO");

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            Page<ProdutoResponse> elasticResult = buscarTodosOsProdutos.findAll(PageRequest.of(0, 10));
            assertThat(elasticResult.getContent()).anyMatch(p -> p.IdProduto().equals(response.IdProduto()));
        });
    }
}
