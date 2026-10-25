CREATE TABLE IF NOT EXISTS pecas (
 codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
 nome VARCHAR(150) NOT NULL,
 codigo_barras VARCHAR(100) NOT NULL,
 fornecedor_marca VARCHAR(150) NOT NULL,
 quantidade_estoque INT NOT NULL CHECK (quantidade_estoque >= 0),
 preco_custo DECIMAL(12,2) NOT NULL CHECK (preco_custo >= 0),
 preco_venda DECIMAL(12,2) NOT NULL CHECK (preco_venda >= 0),
 categoria VARCHAR(30) NOT NULL CHECK (categoria IN ('MOTOR','SUSPENSAO','FREIOS','ELETRICA','ACESSORIOS')),
 tamanho VARCHAR(50), cor VARCHAR(50),
 data_cadastro DATETIME(6) NOT NULL,
 data_ultima_atualizacao DATETIME(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS servicos (
 codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
 nome VARCHAR(150) NOT NULL,
 tempo_estimado_minutos INT NOT NULL CHECK (tempo_estimado_minutos > 0),
 custo_tabelado DECIMAL(12,2) NOT NULL CHECK (custo_tabelado >= 0),
 data_criacao DATETIME(6) NOT NULL,
 data_ultima_atualizacao DATETIME(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS pedidos_pecas (
 codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
 status VARCHAR(30) NOT NULL CHECK (status IN ('EM_ORCAMENTO','PENDENTE_PAGAMENTO','PAGO','ENTREGUE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS itens_pedido_peca (
 codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
 pedido_codigo BIGINT NOT NULL,
 peca_codigo BIGINT NOT NULL,
 quantidade INT NOT NULL CHECK (quantidade > 0),
 FOREIGN KEY (pedido_codigo) REFERENCES pedidos_pecas(codigo) ON DELETE CASCADE,
 FOREIGN KEY (peca_codigo) REFERENCES pecas(codigo) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS ordens_servico (
 codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
 descricao VARCHAR(2000) NOT NULL,
 status VARCHAR(30) NOT NULL CHECK (status IN ('ABERTO','PENDENDE_PAGAMENTO','PAGO','EXECUCAO','EXECUTADO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS ordem_servico_pecas (
 ordem_codigo BIGINT NOT NULL, peca_codigo BIGINT NOT NULL,
 PRIMARY KEY (ordem_codigo,peca_codigo),
 FOREIGN KEY (ordem_codigo) REFERENCES ordens_servico(codigo) ON DELETE CASCADE,
 FOREIGN KEY (peca_codigo) REFERENCES pecas(codigo) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS ordem_servico_servicos (
 ordem_codigo BIGINT NOT NULL, servico_codigo BIGINT NOT NULL,
 PRIMARY KEY (ordem_codigo,servico_codigo),
 FOREIGN KEY (ordem_codigo) REFERENCES ordens_servico(codigo) ON DELETE CASCADE,
 FOREIGN KEY (servico_codigo) REFERENCES servicos(codigo) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
