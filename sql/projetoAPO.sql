-- Recomenda-se a criação de um banco de dados específico para o sistema.
CREATE DATABASE IF NOT EXISTS projeto_apo;
-- DROP DATABASE projeto_apo;

USE projeto_apo;

-- ==========================================================================================
-- TABELA tb_usuario
-- Armazena os dados do sistema de login da assistência técnica.
-- ==========================================================================================
CREATE TABLE IF NOT EXISTS tb_usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    privilegios ENUM('administrador', 'usuario') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);
-- DROP TABLE tb_usuario;
-- SELECT * FROM tb_usuario;
INSERT INTO tb_usuario(email, senha, privilegios) VALUES('admin', '123', 'administrador');
INSERT INTO tb_usuario(email, senha, privilegios) VALUES('user', '123', 'usuario');

-- ==========================================================================================
-- TABELA tb_cliente
-- Armazena os dados dos clientes da assistência técnica.
-- ==========================================================================================
CREATE TABLE IF NOT EXISTS tb_cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(18) NOT NULL UNIQUE,
    tipo_cliente ENUM('pf', 'pj') NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    endereco VARCHAR(255),
    numero VARCHAR(10),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    uf CHAR(2),
    cep VARCHAR(9),
    complemento VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE
);
-- DROP TABLE tb_cliente;
-- SELECT * FROM tb_cliente;

-- ==========================================================================================
-- TABELA tb_funcionario
-- Armazena os dados dos funcionários da empresa.
-- ==========================================================================================
CREATE TABLE IF NOT EXISTS tb_funcionario (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNIQUE,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    funcao VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES tb_usuario(id_usuario)
);
-- DROP TABLE tb_funcionario;
-- SELECT * FROM tb_funcionario;

-- ==========================================================================================
-- TABELA tb_fornecedor
-- Armazena os dados dos fornecedores de peças e outros materiais.
-- ==========================================================================================
CREATE TABLE IF NOT EXISTS tb_fornecedor (
    id_fornecedor INT AUTO_INCREMENT PRIMARY KEY,
    razao_social VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    site VARCHAR(255),
    tipo_fornecedor VARCHAR(100),
    garantia TEXT,
    nome_fantasia VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE
);
-- DROP TABLE tb_fornecedor;
-- SELECT * FROM tb_fornecedor;

-- ======================================================================================
-- TABELA tb_estoque_aparelho
-- Define os locais de estoque para os aparelhos.
-- ==========================================================================================
CREATE TABLE IF NOT EXISTS tb_estoque_aparelho (
    id_estoque_aparelho INT AUTO_INCREMENT PRIMARY KEY,
    localizacao VARCHAR(255) NOT NULL,
    categoria VARCHAR(100),
    dimensao VARCHAR(100),
    capacidade INT
);
-- DROP TABLE tb_estoque_aparelho;
-- SELECT * FROM tb_estoque_aparelho;

-- ==========================================================================================
-- TABELA tb_aparelho
-- Armazena informações sobre os aparelhos dos clientes.
-- ==========================================================================================
CREATE TABLE IF NOT EXISTS tb_aparelho (
    id_aparelho INT AUTO_INCREMENT PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    numero_serie VARCHAR(100) NOT NULL UNIQUE,
    categoria VARCHAR(100),
    dimensao VARCHAR(50),
    peso DECIMAL(10, 2),
    proprietario INT NOT NULL,
    id_estoque_aparelho INT,
    garantia TEXT,
    estado ENUM('em_manutencao', 'em_estoque', 'em_entrega') NOT NULL,
    FOREIGN KEY (proprietario) REFERENCES tb_cliente(id_cliente),
    FOREIGN KEY (id_estoque_aparelho) REFERENCES tb_estoque_aparelho(id_estoque_aparelho)
);
-- DROP TABLE tb_aparelho;
-- SELECT * FROM tb_aparelho;

-- ==========================================================================================
-- TABELA tb_ordem_servico
-- Registra todas as ordens de serviço.
-- ==========================================================================================
CREATE TABLE IF NOT EXISTS tb_ordem_servico (
    id_ordem_servico INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_aparelho INT NOT NULL,
    tecnico_responsavel INT NOT NULL,
    atendente INT NOT NULL,
    descricao_ocorrencia TEXT NOT NULL,
    comentario_tecnico TEXT,
    tipo_servico VARCHAR(255),
    valor_servico DECIMAL(10, 2),
    tipo_pagamento ENUM('avista', 'parcelado'),
    forma_pagamento ENUM('cartao_credito', 'debito', 'pix', 'boleto'),
    prazo DATE,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('finalizado', 'em_manutencao', 'aguardando_resposta') NOT NULL,
    endereco_entrega INT,
    FOREIGN KEY (id_cliente) REFERENCES tb_cliente(id_cliente),
    FOREIGN KEY (id_aparelho) REFERENCES tb_aparelho(id_aparelho),
    FOREIGN KEY (tecnico_responsavel) REFERENCES tb_funcionario(id_funcionario),
    FOREIGN KEY (atendente) REFERENCES tb_funcionario(id_funcionario)
);
-- DROP TABLE tb_ordem_servico;
-- SELECT * FROM tb_ordem_servico;

-- ==========================================================================================
-- INSERÇÕES PARA DADOS INICIAIS DO BANCO DE DADOS
-- ==========================================================================================
INSERT INTO tb_cliente (nome, cpf_cnpj, tipo_cliente, telefone, email, endereco, numero, bairro, cidade, uf, cep, complemento) VALUES
('Ana Souza', '123.456.789-00', 'pf', '11987654321', 'ana@email.com', 'Rua das Flores', '101', 'Centro', 'São Paulo', 'SP', '01000-000', ''),
('João Lima', '987.654.321-00', 'pf', '11999998888', 'joao@email.com', 'Av. Brasil', '45', 'Jardins', 'São Paulo', 'SP', '01400-000', ''),
('TechCorp Ltda', '12.345.678/0001-99', 'pj', '1133224455', 'contato@techcorp.com', 'Rua Industrial', '300', 'Zona Industrial', 'Campinas', 'SP', '13000-000', 'Bloco A'),
('Carlos Mendes', '123.123.123-99', 'pf', '1188888888', 'carlos@email.com', 'Av. Paulista', '900', 'Bela Vista', 'São Paulo', 'SP', '01310-000', ''),
('Roberta Dias', '321.321.321-77', 'pf', '11912345678', 'roberta@email.com', 'Rua Nova', '200', 'Centro', 'Santos', 'SP', '11000-000', ''),
('Empresa X', '11.111.111/0001-00', 'pj', '1144556677', 'empresaX@dominio.com', 'Rua das Empresas', '10', 'Distrito', 'Osasco', 'SP', '06000-000', ''),
('Mariana Castro', '456.789.123-11', 'pf', '11987654322', 'mariana@email.com', 'Rua A', '111', 'Bairro B', 'Ribeirão Preto', 'SP', '14000-000', ''),
('André Silva', '789.123.456-00', 'pf', '1177776666', 'andre@email.com', 'Rua C', '222', 'Bairro D', 'Campinas', 'SP', '13050-000', ''),
('Loja Peças Ltda', '99.888.777/0001-11', 'pj', '1166554433', 'pecas@loja.com', 'Av. Central', '500', 'Comercial', 'Guarulhos', 'SP', '07100-000', ''),
('Beatriz Gomes', '111.222.333-44', 'pf', '11911223344', 'beatriz@email.com', 'Rua D', '303', 'Bairro E', 'São Paulo', 'SP', '01500-000', '');

INSERT INTO tb_funcionario (nome, cpf, funcao, telefone, email) VALUES
('Lucas Oliveira', '123.456.789-01', 'Técnico', '11999990000', 'lucas@empresa.com'),
('Juliana Alves', '987.654.321-02', 'Atendente', '11988887777', 'juliana@empresa.com'),
('Pedro Lima', '321.654.987-03', 'Técnico', '11977776666', 'pedro@empresa.com'),
('Fernanda Costa', '654.321.987-04', 'Atendente', '11966665555', 'fernanda@empresa.com'),
('Carlos Eduardo', '789.456.123-05', 'Gerente', '11955554444', 'carlos@empresa.com'),
('Amanda Nunes', '963.852.741-06', 'Financeiro', '11944443333', 'amanda@empresa.com'),
('Rafael Dias', '852.963.741-07', 'Técnico', '11933332222', 'rafael@empresa.com'),
('Sofia Martins', '741.852.963-08', 'RH', '11922221111', 'sofia@empresa.com'),
('Bruno Silva', '159.357.486-09', 'Suporte', '11911110000', 'bruno@empresa.com'),
('Larissa Lopes', '357.159.486-10', 'Atendente', '11900009999', 'larissa@empresa.com');

INSERT INTO tb_fornecedor (razao_social, cnpj, telefone, site, tipo_fornecedor, garantia, nome_fantasia) VALUES
('Tecnoparts Ltda', '11.222.333/0001-11', '1133334444', 'http://tecnoparts.com', 'Eletrônicos', '12 meses', 'Tecnoparts'),
('Eletronic World', '22.333.444/0001-22', '1144445555', 'http://eletronicworld.com', 'Componentes', '24 meses', 'E-World'),
('Distribuidora Mega', '33.444.555/0001-33', '1155556666', 'http://megadistrib.com', 'Acessórios', '6 meses', 'MegaDist'),
('Cabo Fácil', '44.555.666/0001-44', '1166667777', 'http://cabofacil.com', 'Cabos', 'Indeterminado', 'CaboFácil'),
('FonteTech', '55.666.777/0001-55', '1177778888', 'http://fontetech.com', 'Fontes', '18 meses', 'FonteTech'),
('Cooler Brasil', '66.777.888/0001-66', '1188889999', 'http://coolerbr.com', 'Resfriamento', '12 meses', 'CoolerBR'),
('Chip&Co', '77.888.999/0001-77', '1199990000', 'http://chipco.com', 'Chips', '12 meses', 'ChipCo'),
('Placas Alpha', '88.999.000/0001-88', '1122334455', 'http://placasalpha.com', 'Placas Mãe', '24 meses', 'Alpha'),
('HardStore', '99.000.111/0001-99', '1133445566', 'http://hardstore.com', 'HDs e SSDs', '12 meses', 'HardStore'),
('Visão Digital', '00.111.222/0001-00', '1144556677', 'http://visaodigital.com', 'Monitores', '24 meses', 'VisãoDigital');

INSERT INTO tb_estoque_aparelho (localizacao, categoria, dimensao, capacidade) VALUES
('Estoque A1', 'Notebook', '40x30x10', 50),
('Estoque A2', 'Smartphone', '15x7x1', 200),
('Estoque B1', 'Desktop', '60x60x30', 30),
('Estoque C1', 'TV', '120x80x20', 10),
('Estoque D1', 'Tablet', '25x20x2', 100),
('Estoque A3', 'Impressoras', '50x40x30', 20),
('Estoque B2', 'Fontes', '30x20x15', 150),
('Estoque C2', 'Cabos', '10x10x10', 500),
('Estoque D2', 'Coolers', '15x15x10', 80),
('Estoque B3', 'Monitores', '50x30x10', 40);

INSERT INTO tb_aparelho (modelo, numero_serie, categoria, dimensao, peso, proprietario, id_estoque_aparelho, garantia, estado) VALUES
('Dell Inspiron 15', 'SN001', 'Notebook', '38x25x2', 2.5, 1, 1, 'Garantia de 1 ano', 'em_manutencao'),
('iPhone 12', 'SN002', 'Smartphone', '15x7x1', 0.3, 2, 2, 'Garantia Apple', 'em_estoque'),
('TV Samsung 50"', 'SN003', 'TV', '112x70x7', 8.0, 3, 4, 'Garantia de 2 anos', 'em_entrega'),
('Lenovo ThinkPad', 'SN004', 'Notebook', '36x24x2', 2.3, 4, 1, 'Garantia de 1 ano', 'em_estoque'),
('Canon Pixma', 'SN005', 'Impressora', '45x30x15', 4.0, 5, 6, 'Garantia Canon', 'em_manutencao'),
('Samsung Galaxy S22', 'SN006', 'Smartphone', '15x7x1', 0.4, 6, 2, 'Garantia de fábrica', 'em_estoque'),
('iMac 27"', 'SN007', 'Desktop', '65x45x20', 10.0, 7, 3, 'Garantia Apple', 'em_entrega'),
('Xiaomi Mi 11', 'SN008', 'Smartphone', '16x7x1', 0.35, 8, 2, 'Garantia de 1 ano', 'em_estoque'),
('HP Envy', 'SN009', 'Notebook', '37x24x2', 2.4, 9, 1, 'Garantia HP', 'em_estoque'),
('Acer Aspire 5', 'SN010', 'Notebook', '39x26x2', 2.6, 10, 1, 'Garantia de 1 ano', 'em_manutencao');

INSERT INTO tb_ordem_servico (id_cliente, id_aparelho, tecnico_responsavel, atendente, descricao_ocorrencia, comentario_tecnico, tipo_servico, valor_servico, tipo_pagamento, forma_pagamento, prazo, estado, endereco_entrega) VALUES
(1, 1, 1, 2, 'Notebook não liga', 'Problema na fonte', 'Reparo de fonte', 250.00, 'avista', 'pix', '2025-07-05', 'em_manutencao', 1),
(2, 2, 3, 4, 'Tela quebrada', 'Substituição pendente', 'Troca de tela', 600.00, 'parcelado', 'cartao_credito', '2025-07-10', 'aguardando_resposta', 2),
(3, 3, 1, 2, 'Sem imagem', 'TV funcionando após ajuste', 'Ajuste de firmware', 300.00, 'avista', 'boleto', '2025-07-01', 'finalizado', 3),
(4, 4, 3, 4, 'Teclado não funciona', 'Troca concluída', 'Substituição de teclado', 150.00, 'avista', 'debito', '2025-07-03', 'finalizado', 4),
(5, 5, 1, 2, 'Erro de impressão', 'Problema na cabeça de impressão', 'Manutenção geral', 200.00, 'parcelado', 'cartao_credito', '2025-07-08', 'em_manutencao', 5),
(6, 6, 3, 4, 'Tela não acende', '', 'Diagnóstico de display', 0.00, 'avista', 'pix', '2025-07-10', 'aguardando_resposta', 6),
(7, 7, 1, 2, 'Problemas de desempenho', 'Limpeza de sistema', 'Formatação e otimização', 180.00, 'avista', 'pix', '2025-07-06', 'finalizado', 7),
(8, 8, 3, 4, 'Bateria descarregando rápido', 'Substituição agendada', 'Troca de bateria', 280.00, 'parcelado', 'cartao_credito', '2025-07-12', 'em_manutencao', 8),
(9, 9, 1, 2, 'Não inicializa', 'HD com problemas', 'Troca de HD', 350.00, 'avista', 'debito', '2025-07-07', 'finalizado', 9),
(10, 10, 3, 4, 'Tela azul constante', 'Reinstalação do sistema', 'Reinstalação', 120.00, 'avista', 'pix', '2025-07-04', 'finalizado', 10);

-- SELECT user, host FROM mysql.user;
