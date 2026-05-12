<div align="center">

# E-Commerce Backend API

### A production-grade, cloud-ready e-commerce platform built with enterprise Java patterns

[![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL_16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis_7-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ_3-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)
[![Elasticsearch](https://img.shields.io/badge/Elasticsearch_8-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)](https://www.elastic.co/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Stripe](https://img.shields.io/badge/Stripe-635BFF?style=for-the-badge&logo=stripe&logoColor=white)](https://stripe.com/)

</div>

---

## Sobre o Projeto

Este projeto é uma **API Backend completa para E-Commerce**, construída do zero com foco em **qualidade de código, escalabilidade e padrões enterprise**. Foi projetada para simular os desafios reais de sistemas de alta demanda, integrando tecnologias e padrões arquiteturais que o mercado exige.

O diferencial não está apenas nas tecnologias — está nas **decisões de design**: desde como garantir que um cliente nunca seja cobrado duas vezes por falha de rede, até como separar a escrita da leitura para escalar de forma independente.

---

## Arquitetura

O projeto é estruturado como um **Monolito Modular** (via Spring Modulith), combinando a organização de um microserviço com a simplicidade operacional de um monólito. Internamente, cada módulo segue a **Clean Architecture + DDD**, e o fluxo de dados usa o padrão **CQRS**.

```
┌─────────────────────────────────────────────────────────────┐
│                     MÓDULOS (Spring Modulith)                │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │   Produtos   │  │   Pedidos    │  │    Pagamentos    │  │
│  │   (CQRS)     │  │  (Outbox)    │  │    (Stripe)      │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │  Carrinhos   │  │   Usuários   │  │  Notificações    │  │
│  │   (Redis)    │  │    (JWT)     │  │  (RabbitMQ/SMTP) │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
└─────────────────────────────────────────────────────────────┘
         │ API Layer (REST Controllers + Swagger)
         │ Application Layer (Use Cases / Commands / Queries)
         │ Domain Layer (Entities + Business Rules)
         │ Infrastructure Layer (JPA, Redis, Stripe, RabbitMQ...)
```

### Por que Monolito Modular?

A separação em módulos explícitos via **Spring Modulith** impõe fronteiras arquiteturais verificadas em teste, impedindo dependências circulares entre domínios — o melhor dos dois mundos entre monólito e microsserviços.

---

## Stack Tecnológica

| Categoria | Tecnologia | Propósito |
|---|---|---|
| **Framework** | Spring Boot 3.4 | Core da aplicação |
| **Arquitetura** | Spring Modulith 1.3 | Modular Monolith com fronteiras verificáveis |
| **Banco de Dados** | PostgreSQL 16 + Flyway | Persistência principal + migrações versionadas |
| **Cache** | Redis 7 | Cache de carrinhos e sessões |
| **Busca** | Elasticsearch 8 | Leitura de produtos (CQRS) |
| **Mensageria** | RabbitMQ 3 | Comunicação assíncrona entre módulos |
| **Segurança** | Spring Security + JJWT | Autenticação JWT multi-perfil |
| **Pagamentos** | Stripe Java SDK | Split Payments (Marketplace) |
| **Resiliência** | Resilience4j | Circuit Breaker + Rate Limiter |
| **Observabilidade** | Prometheus + Grafana + Zipkin | Métricas + Dashboards + Distributed Tracing |
| **Documentação** | SpringDoc OpenAPI 2.8 | Swagger UI interativo |
| **Containerização** | Docker + Docker Compose | Ambiente completo reproduzível |

---

## Padrões & Decisões de Engenharia

### CQRS + Elasticsearch no Módulo de Produtos

A leitura e a escrita de produtos usam modelos separados. Ao criar ou atualizar um produto, o evento é publicado para o Elasticsearch via RabbitMQ. As consultas (listagens, buscas) vão direto ao Elasticsearch, desacoplando a carga de leitura do banco relacional.

```
POST /produtos → PostgreSQL (Write Model)
                      ↓ Evento via RabbitMQ
               Elasticsearch (Read Model)
                      ↑
GET /produtos  → Elasticsearch
```

**Benefício**: escala de leitura independente da escrita; buscas full-text e filtros complexos sem impactar o banco transacional.

---

### Outbox Pattern — Confiabilidade na Entrega de Eventos

Publicar um evento direto no RabbitMQ dentro de uma transação de negócio cria um problema: e se a transação confirmar mas a mensagem falhar? O **Outbox Pattern** resolve isso.

```
Transação de Banco:
  ├── Salva entidade (ex: Pedido)
  └── Salva evento na tabela outbox (mesmo TX)

Worker assíncrono:
  └── Lê outbox → Publica no RabbitMQ → Marca como publicado
```

**Resultado**: garantia de que nenhum evento é perdido, mesmo com falhas de infraestrutura entre a aplicação e o broker.

---

### Idempotência de Requisições

Endpoints críticos (criação de pedido, processamento de pagamento) exigem um `Idempotency-Key` no header. Um `HandlerInterceptor` verifica se a chave já foi processada:

- **Primeira chamada**: executa a lógica de negócio e persiste a resposta.
- **Chamadas duplicadas**: retorna a resposta salva sem reexecutar — sem cobranças duplas, sem pedidos duplicados.

---

### Split Payments — Stripe Marketplace

Cada pedido pode ter produtos de múltiplos vendedores. O fluxo usa a API de Transferências do Stripe:

1. Cria um `PaymentIntent` com o valor total + `transfer_group` identificador do pedido.
2. Após confirmação do pagamento, distribui transferências individuais para cada conta Stripe dos vendedores.
3. A plataforma retém a taxa sobre cada transação.

---

### Resiliência com Resilience4j

| Padrão | Onde | Comportamento |
|---|---|---|
| **Circuit Breaker** | Chamadas ao Stripe | Se a API do Stripe oscilar, o circuito abre e retorna fallback, protegendo o sistema de cascata de falhas |
| **Rate Limiter** | `POST /auth/login` | Limita tentativas por minuto, mitigando ataques de força bruta |

---

### Gestão de Estado de Pedidos com Rollback Seguro

```
[CRIADO] → Estoque pré-reservado
    ↓ pagamento confirmado
 [PAGO]
    ↓ pagamento cancelado/falha
[CANCELADO] → Estoque restaurado automaticamente
```

O rollback de estoque é acionado de forma assíncrona via evento RabbitMQ, garantindo que nenhum item fique "preso" em reserva por falha de pagamento.

---

### Notificações Assíncronas

O módulo de notificações ouve eventos de domínio (pedido criado, pagamento confirmado) via RabbitMQ e dispara e-mails transacionais via SMTP — completamente desacoplado do fluxo principal.

---

## Observabilidade

O projeto inclui uma stack completa de observabilidade, subida com um único comando:

```bash
docker compose -f docker-compose.observability.yml up -d
```

| Ferramenta | URL | Propósito |
|---|---|---|
| **Prometheus** | `http://localhost:9090` | Coleta de métricas da aplicação |
| **Grafana** | `http://localhost:3000` | Dashboards e alertas (admin/admin) |
| **Zipkin** | `http://localhost:9411` | Distributed Tracing entre módulos |
| **Kibana** | `http://localhost:5601` | Visualização de índices Elasticsearch |
| **RabbitMQ UI** | `http://localhost:15672` | Gerenciamento de filas e mensagens |
| **Actuator** | `http://localhost:8080/actuator` | Health checks e métricas runtime |

---

## Como Executar

### Pré-requisitos

- **Docker** e **Docker Compose** instalados
- **Conta Stripe** com chaves de API (modo teste)
- **Java 17** (apenas se executar fora do Docker)

### 1. Clonar o repositório

```bash
git clone https://github.com/guilhermemonte21/Ecommerce.git
cd Ecommerce
```

### 2. Configurar variáveis de ambiente

```bash
cp .env.example .env
```

Edite o `.env` e preencha:

```env
# Banco de Dados
SPRING_DATASOURCE_USERNAME=seu_usuario
SPRING_DATASOURCE_PASSWORD=sua_senha

# JWT
JWT_SECRET=uma_chave_secreta_forte_de_256_bits

# Stripe
STRIPE_SECRET_KEY=sk_test_...
STRIPE_WEBHOOK_SECRET=whsec_...

# RabbitMQ
RABBITMQ_USER=guest
RABBITMQ_PASS=guest

# Email (SMTP)
MAIL_USERNAME=seu@email.com
MAIL_PASSWORD=sua_senha_de_app
```

### 3. Subir toda a infraestrutura

```bash
# Infraestrutura principal (PostgreSQL, Redis, RabbitMQ, Elasticsearch, Kibana)
docker compose up -d

# Stack de observabilidade (Prometheus, Grafana, Zipkin) — opcional
docker compose -f docker-compose.observability.yml up -d
```

### 4. Executar a aplicação

```bash
# Com Maven wrapper
./mvnw spring-boot:run

# Ou via Docker (todas as dependências incluídas)
docker compose up --build
```

A API estará disponível em **`http://localhost:8080`**

A documentação Swagger estará em **`http://localhost:8080/swagger-ui.html`**

---

## Endpoints da API

A API segue padrões RESTful com versionamento via URL (`/api/v1/`).

### Autenticação
| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/auth/login` | Login e geração de JWT (**Rate Limited**) | Pública |

### Usuários
| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/api/v1/usuarios` | Cadastro de comprador | Pública |
| `POST` | `/api/v1/usuarios/vendedores` | Cadastro de vendedor (exige `stripeAccountId`) | Pública |
| `GET` | `/api/v1/usuarios/{id}` | Perfil do usuário | JWT |
| `PATCH` | `/api/v1/usuarios/status` | Ativar/desativar conta | ADMIN |

### Produtos
| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `GET` | `/api/v1/produtos` | Listagem paginada via **Elasticsearch** | Pública |
| `GET` | `/api/v1/produtos/{id}` | Detalhes do produto | Pública |
| `POST` | `/api/v1/produtos` | Cadastrar produto (**Idempotente**) | VENDEDOR |
| `PUT` | `/api/v1/produtos/{id}` | Atualizar produto | VENDEDOR |
| `PATCH` | `/api/v1/produtos/{id}/estoque` | Atualizar estoque | VENDEDOR |
| `DELETE` | `/api/v1/produtos/{id}` | Remover produto | VENDEDOR |

### Carrinho
| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/api/v1/carrinhos` | Criar carrinho (**Idempotente**, persiste no Redis) | CLIENTE |
| `GET` | `/api/v1/carrinhos/{id}` | Ver itens do carrinho | CLIENTE |
| `POST` | `/api/v1/carrinhos/{id}/itens` | Adicionar produto | CLIENTE |
| `DELETE` | `/api/v1/carrinhos/{id}/itens/{idProd}` | Remover item | CLIENTE |
| `DELETE` | `/api/v1/carrinhos/{id}/itens` | Limpar carrinho | CLIENTE |

### Pedidos
| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/api/v1/pedidos/{idCarrinho}` | Criar pedido (**Idempotente**, reserva estoque) | CLIENTE |
| `GET` | `/api/v1/pedidos/{id}` | Detalhes do pedido | CLIENTE |
| `GET` | `/api/v1/pedidos/comprador/{id}` | Histórico de pedidos | CLIENTE |

### Pagamentos
| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/api/v1/pagamentos/{idPedido}` | Processar pagamento via Stripe (**Idempotente**) | CLIENTE |
| `DELETE` | `/api/v1/pagamentos/{idPedido}` | Cancelar e estornar (restaura estoque) | CLIENTE |

> **Autenticação**: endpoints protegidos exigem o header `Authorization: Bearer {token}`

---

## Migrações de Banco de Dados

O schema é gerenciado pelo **Flyway**, garantindo que o banco esteja sempre na versão correta ao iniciar a aplicação. As migrações ficam em `src/main/resources/db/migration/` e são executadas automaticamente.

---

## Estrutura do Projeto

```
src/main/java/com/github/guilhermemonte21/ecommerce/
├── auth/               # Módulo de autenticação (JWT, Spring Security)
├── carrinho/           # Módulo de carrinho (Redis)
├── notificacao/        # Módulo de notificações (RabbitMQ + SMTP)
├── pagamento/          # Módulo de pagamentos (Stripe Marketplace)
├── pedido/             # Módulo de pedidos (Outbox Pattern)
├── produto/            # Módulo de produtos (CQRS + Elasticsearch)
│   ├── application/
│   │   ├── command/    # Handlers de escrita (PostgreSQL)
│   │   └── query/      # Handlers de leitura (Elasticsearch)
│   ├── domain/
│   └── infra/
└── usuario/            # Módulo de usuários (perfis: CLIENTE, VENDEDOR, ADMIN)
```

---

## Autor

**Guilherme Monte**

[![GitHub](https://img.shields.io/badge/GitHub-guilhermemonte21-181717?style=flat-square&logo=github)](https://github.com/guilhermemonte21)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Guilherme_Monte-0A66C2?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/guilherme-pereira-monte)
[![Email](https://img.shields.io/badge/Email-guilhermemontefilho2112gmail.com-EA4335?style=flat-square&logo=gmail)](mailto:guilhermemontefilho2112@gmail.com)

---

<div align="center">
  <sub>Desenvolvido com foco em qualidade, escalabilidade e boas práticas de engenharia de software.</sub>
</div>
