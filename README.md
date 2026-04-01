# 🛒 Ecommerce - Backend API

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42335b?style=for-the-badge&logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-008CDD?style=for-the-badge&logo=stripe&logoColor=white)

Uma API robusta e escalável para E-commerce construída com **Spring Boot 3**, seguindo os princípios da **Clean Architecture** e **DDD (Domain-Driven Design)**. O sistema suporta pagamentos via Stripe com divisão de valores entre vendedores (Split Payments), caching distribuído com Redis e resiliência com Resilience4j.

---

## 🚀 Funcionalidades Principais

- **📦 Gestão de Produtos**: CRUD completo com paginação e busca.
- **🛒 Carrinho de Compras**: Persistência otimizada de itens no carrinho (Caching via Redis).
- **👤 Autenticação & Autorização**: JWT com suporte a diferentes perfis (CLIENTE, VENDEDOR, ADMIN).
- **💰 Pagamentos Integrados**: Integração completa com **Stripe API** para processamento de cartões e split de pagamentos entre vendedores e plataforma.
- **📋 Gestão de Pedidos**: Fluxo completo de pedidos (Pendente, Pago, Cancelado) com rollback de estoque.
- **🛡️ Alta Disponibilidade & Resiliência**: Implementação de Circuit Breaker e Rate Limiter utilizando **Resilience4j**.
- **📊 Monitoramento**: Exposição de métricas de saúde e performance via **Spring Boot Actuator**.
- **📜 Documentação Interativa**: API totalmente documentada com **Swagger/OpenAPI**.

---

## 🏗️ Arquitetura do Projeto

O projeto é estruturado seguindo a **Clean Architecture**, garantindo independência de frameworks, testabilidade e separação clara de responsabilidades:

1. **Domain**: Contém as entidades de negócio, enums e regras fundamentais.
2. **Application (Use Cases)**: Orquestra o fluxo de dados e implementa as regras de negócio específicas da aplicação.
3. **API (Interface Adapters)**: Camada de entrada (Controllers) que expõe os endpoints REST.
4. **Infra (Frameworks & Drivers)**: Detalhes de implementação como persistência (JPA), segurança (JWT), configurações de terceiros (Stripe, Redis) e mappers.

---

## 🛠️ Tecnologias Utilizadas

- **Framework**: Spring Boot 3.3.4
- **Linguagem**: Java 17
- **Banco de Dados**: PostgreSQL
- **Cache**: Redis
- **Segurança**: Spring Security & JJWT
- **Documentação**: SpringDoc OpenAPI (Swagger UI)
- **Mensageria & Resiliência**: Resilience4j (Circuit Breaker, Rate Limiter)
- **Pagamentos**: Stripe Java SDK
- **Containerização**: Docker & Docker Compose
- **Ferramentas**: Lombok, MapStruct (Mappers), Maven

---

## ⚙️ Como Executar

### Pré-requisitos
- **Java 17** ou superior
- **Docker & Docker Compose**
- **Conta no Stripe** (para chaves de API em modo teste)

### Passo a passo

1. **Clonar o Repositório**
   ```bash
   git clone https://github.com/guilhermemonte21/Ecommerce.git
   cd Ecommerce
   ```

2. **Configurar as Variáveis de Ambiente**
   Crie um arquivo `.env` na raiz do projeto (use o `.env.example` como base):
   ```bash
   cp .env.example .env
   ```
   *Preencha o `JWT_SECRET`, as credenciais do Banco de Dados e as chaves do Stripe no arquivo `.env`.*

3. **Subir a Infraestrutura (Docker)**
   Utilize o Docker Compose para subir o PostgreSQL e o Redis:
   ```bash
   docker-compose up -d
   ```

4. **Executar a Aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```
   *A API estará disponível em `http://localhost:8080`.*

---

## 📋 Endpoints da API

A API segue padrões RESTful. Abaixo estão os principais recursos disponíveis:

### 🔐 Autenticação (`/auth`)
- `POST /auth/login` - Autentica o usuário e retorna o Token JWT (**Rate Limited**).

### 👥 Usuários (`/api/v1/usuarios`)
- `POST /api/v1/usuarios` - Cria um novo comprador.
- `GET /api/v1/usuarios/{id}` - Busca dados do perfil.
- `PATCH /api/v1/usuarios/status` - Ativa/Desativa a conta.
- `POST /api/v1/usuarios/vendedores` - Cria uma conta de vendedor (Requer `stripeAccountId`).

### 📦 Produtos (`/api/v1/produtos`)
- `GET /api/v1/produtos` - Listagem paginada (Cache em Redis).
- `POST /api/v1/produtos` - Cadastra novo produto (**Idempotente**).
- `GET /api/v1/produtos/{id}` - Detalhes do produto.
- `PUT /api/v1/produtos/{id}` - Atualização completa.
- `PATCH /api/v1/produtos/{id}/estoque` - Atualiza quantidade em estoque.
- `DELETE /api/v1/produtos/{id}` - Remove o produto.

### 🛒 Carrinho (`/api/v1/carrinhos`)
- `POST /api/v1/carrinhos` - Inicia novo carrinho (**Idempotente**).
- `GET /api/v1/carrinhos/{id}` - Recupera itens do carrinho.
- `POST /api/v1/carrinhos/{id}/itens` - Adiciona produto ao carrinho.
- `DELETE /api/v1/carrinhos/{id}/itens/{idProd}` - Remove item específico.
- `DELETE /api/v1/carrinhos/{id}/itens` - Limpa o carrinho.

### 📋 Pedidos (`/api/v1/pedidos`)
- `POST /api/v1/pedidos/{idCarrinho}` - Finaliza carrinho e gera pedido (**Idempotente**).
- `GET /api/v1/pedidos/{id}` - Detalhes do pedido.
- `GET /api/v1/pedidos/comprador/{id}` - Histórico de pedidos do usuário.

### 💰 Pagamentos (`/api/v1/pagamentos`)
- `POST /api/v1/pagamentos/{idPedido}` - Processa pagamento via Stripe (**Idempotente**).
- `DELETE /api/v1/pagamentos/{idPedido}` - Cancela/Estorna e reverte estoque.

---

## 🧠 Lógicas Complexas & Engenharia

Este projeto implementa padrões avançados de engenharia de software para garantir escalabilidade e confiabilidade:

### 1. Split Payments (Stripe Marketplace)
Diferente de um checkout simples, esta API gerencia múltiplos vendedores em um único pedido.
- **PaymentIntent + Transfer Group**: Criamos uma intenção de pagamento agregando o valor total. Usamos o `transfer_group` para vincular cobranças e transferências.
- **Transferências Individuais**: Após a confirmação do pagamento, o sistema orquestra transferências automáticas para as contas Stripe dos respectivos vendedores, garantindo que cada um receba seu valor proporcional de forma desacoplada.

### 2. Idempotência de Requisições
Para evitar cobranças duplicadas ou pedidos repetidos por instabilidade na rede:
- **Idempotency-Key**: Endpoints críticos exigem uma chave única no Header.
- **Interceptor de Requisição**: Um `HandlerInterceptor` verifica se a chave já foi processada. Se sim, ele retorna a resposta salva no banco sem reexecutar a lógica de negócio.
- **Segurança Transacional**: Se uma queda ocorrer durante o processamento, a chave garante que o cliente nunca pague duas vezes pelo mesmo item.

### 3. Resiliência com Resilience4j
O sistema é projetado para falhar "graciosamente":
- **Circuit Breaker (Stripe)**: Se a API da Stripe oscilar, o circuito se abre para evitar sobrecarga e responde com uma mensagem amigável de fallback.
- **Rate Limiter (Login)**: Proteção contra ataques de força bruta no endpoint de `/auth/login`, limitando o número de tentativas por minuto.

### 4. Gestão de Estado de Pedidos & Rollback
Implementamos um lifecycle robusto de pedidos:
- **Estado Pendente**: O estoque é pré-reservado no ato da criação do pedido.
- **Rollback de Estoque**: Caso o pagamento seja cancelado ou falhe, um mecanismo automatizado devolve os itens ao estoque original para evitar inconsistências de inventário.

### 5. Clean Architecture & Desacoplamento
O projeto é rigorosamente dividido em camadas:
- **Independência de Frameworks**: A lógica de negócio reside no centro (`Domain` e `Application`) e não conhece detalhes como JPA ou Stripe SDK.
- **Gateways & Adaptadores**: Toda comunicação externa (Banco, Pagamentos) é feita via interfaces (Gateways), permitindo trocar de provedor de pagamento ou database com impacto mínimo no código fonte.

---

## 📜 Documentação Interativa

## 🔐 Configurações de Segurança

O projeto utiliza **JWT (JSON Web Token)** para autenticação. Para acessar rotas protegidas, inclua o token no Header da requisição:
`Authorization: Bearer {SEU_TOKEN_AQUI}`

---

## 📄 Licença

Este projeto é de uso livre para fins educacionais e profissionais. 
Sinta-se à vontade para entrar em contato se tiver alguma dúvida.

---

Feito por [Guilherme Monte](https://github.com/guilhermemonte21)
