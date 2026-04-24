package com.github.guilhermemonte21.Ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModuleStructureTest {

    static final ApplicationModules modules = ApplicationModules.of(EcommerceApplication.class);

    @Test
    void documentModuleStructure() {
        modules.forEach(System.out::println);
    }

    /**
     * Valida que não existem dependências cíclicas entre módulos.
     * Violações de fronteira conhecidas e pendentes de refatoração:
     *
     * - Pagamento → Pedidos: Pagamento.java e StripePagamentoGatewayImpl.java importam
     *   PedidoGateway e entidades de Pedidos diretamente.
     *   Solução futura: criar DTO de pagamento em Shared e expor apenas o que o módulo de
     *   pagamento precisa saber sobre um pedido.
     *
     * - Notificacoes → Pedidos/Usuarios: os consumers de notificação buscam dados de pedido
     *   e usuário via gateways de outros módulos para montar o e-mail.
     *   Solução futura: enriquecer os eventos com os dados necessários (email, nome) na origem,
     *   eliminando a necessidade de queries cross-módulo.
     *
     * - CriarPedido → Carrinho/Produtos/Usuarios: o caso de uso de criação de pedido
     *   orquestra múltiplos módulos por design (hub module).
     */
    @Test
    void verifyModuleStructure() {
        modules.verify();
    }
}
