-- V1__baseline.sql

-- Usuarios
CREATE TABLE usuarios (
    id_usuario UUID PRIMARY KEY,
    nome_usuario VARCHAR(255),
    email_usuario VARCHAR(255),
    cpf_usuario VARCHAR(255) UNIQUE,
    senha_usuario VARCHAR(255),
    ativo BOOLEAN,
    tipo_usuario VARCHAR(50),
    stripe_account_id VARCHAR(255)
);
CREATE INDEX idx_usuario_email ON usuarios(email_usuario);

-- Produtos
CREATE TABLE produtos (
    id_produto UUID PRIMARY KEY,
    nome_produto VARCHAR(255),
    vendedor_id UUID NOT NULL,
    descricao_produto TEXT,
    preco_produto DECIMAL(19, 2),
    estoque_produto BIGINT,
    version BIGINT
);
CREATE INDEX idx_produto_nome ON produtos(nome_produto);

-- Carrinho
CREATE TABLE carrinho (
    id_carrinho UUID PRIMARY KEY,
    comprador_id UUID NOT NULL UNIQUE,
    valor_total DECIMAL(19, 2),
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Carrinho Itens
CREATE TABLE carrinho_itens (
    id UUID PRIMARY KEY,
    carrinho_id UUID,
    produto_id UUID NOT NULL,
    nome_produto_snapshot VARCHAR(255),
    preco_snapshot DECIMAL(19, 2),
    quantidade BIGINT,
    CONSTRAINT fk_carrinho FOREIGN KEY (carrinho_id) REFERENCES carrinho(id_carrinho)
);

-- Pedidos
CREATE TABLE pedidos (
    id_pedido UUID PRIMARY KEY,
    comprador_id UUID NOT NULL,
    preco_pedido DECIMAL(19, 2),
    Endereco_entrega VARCHAR(255),
    status_pedido VARCHAR(50),
    criado_em TIMESTAMP WITH TIME ZONE
);

-- Pedidos do Vendedor (Itens do Pedido)
CREATE TABLE pedidosDoVendedor (
    ProdutoVendedorId UUID PRIMARY KEY,
    vendedor_id UUID NOT NULL,
    id_Pedido UUID,
    produto_id UUID NOT NULL,
    nome_produto_snapshot VARCHAR(255),
    preco_snapshot DECIMAL(19, 2),
    quantidade BIGINT,
    valorDoPedido DECIMAL(19, 2),
    stripe_account_id VARCHAR(255),
    StatusDoPedido VARCHAR(50),
    CONSTRAINT fk_pedido FOREIGN KEY (id_Pedido) REFERENCES pedidos(id_pedido)
);

-- Outbox Events
CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    event_type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    occurred_on TIMESTAMP WITH TIME ZONE NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    processed_at TIMESTAMP WITH TIME ZONE,
    error_message TEXT,
    trace_id VARCHAR(255),
    span_id VARCHAR(255)
);
CREATE INDEX idx_outbox_processed_occurred ON outbox_events(processed, occurred_on);

-- Idempotency Records
CREATE TABLE idempotency_records (
    idempotency_key VARCHAR(100) PRIMARY KEY,
    http_method VARCHAR(10) NOT NULL,
    request_path VARCHAR(255) NOT NULL,
    response_status INTEGER NOT NULL,
    response_body TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
