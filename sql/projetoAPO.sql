-- Recomenda-se a criação de um banco de dados específico para o sistema.
CREATE DATABASE ProjetoAPO;
-- DROP DATABASE ProjetoAPO;

USE ProjetoAPO;

CREATE TABLE Usuario (
    iduser INT AUTO_INCREMENT PRIMARY KEY,
    emailuser VARCHAR(255) NOT NULL,
    senhauser VARCHAR(255) NOT NULL UNIQUE,
    privilegiosuser ENUM('admin', 'usuario') NOT NULL,
    userativo BOOLEAN DEFAULT TRUE
);

-- ==========================================================================================
-- TABELA Cliente
-- Armazena os dados dos clientes da assistência técnica.
-- ==========================================================================================
CREATE TABLE tb_cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(18) NOT NULL UNIQUE,
    tipo_cliente ENUM('PF', 'PJ') NOT NULL,
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

-- ==========================================================================================
-- TABELA Funcionario
-- Armazena os dados dos funcionários da empresa.
-- ==========================================================================================
CREATE TABLE Funcionario (
    idfuncionario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    funcao VARCHAR(100) NOT NULL,
    telefonefuncionario VARCHAR(20) NOT NULL,
    emailfuncionario VARCHAR(255) NOT NULL UNIQUE,
    funcionarioativo BOOLEAN DEFAULT TRUE
);

-- ==========================================================================================
-- TABELA Fornecedor
-- Armazena os dados dos fornecedores de peças e outros materiais.
-- ==========================================================================================
CREATE TABLE Fornecedor (
    idfornecedor INT AUTO_INCREMENT PRIMARY KEY,
    razaosocial VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    telefonefornecedor VARCHAR(20) NOT NULL,
    sitefornecedor VARCHAR(255),
    tipofornecedor VARCHAR(100),
    garantia TEXT,
    nomefantasia VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE
);

-- ======================================================================================
-- TABELA Estoque_Aparelho
-- Define os locais de estoque para os aparelhos.
-- ==========================================================================================
CREATE TABLE Estoque_Aparelho (
    idestoque INT AUTO_INCREMENT PRIMARY KEY,
    localizacao VARCHAR(255) NOT NULL,
    categoriaestoque VARCHAR(100),
    dimensao VARCHAR(100),
    capacidade INT
);

-- ==========================================================================================
-- TABELA Aparelho
-- Armazena informações sobre os aparelhos dos clientes.
-- ==========================================================================================
CREATE TABLE Aparelho (
    idaparelho INT AUTO_INCREMENT PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    numeroserie VARCHAR(100) NOT NULL UNIQUE,
    categoria VARCHAR(100),
    dimensao VARCHAR(50),
    peso DECIMAL(10, 2),
    proprietario INT NOT NULL,
    lugarestoque INT,
    garantia TEXT,
    estado ENUM('Em Manutenção', 'Em Estoque', 'Em Entrega') NOT NULL,
    FOREIGN KEY (proprietario) REFERENCES tb_cliente(id_cliente),
    FOREIGN KEY (lugarestoque) REFERENCES Estoque_Aparelho(idestoque)
);

-- ==========================================================================================
-- TABELA Ordem_de_Servico
-- Registra todas as ordens de serviço.
-- ==========================================================================================
CREATE TABLE Ordem_de_Servico (
    idordemservico INT AUTO_INCREMENT PRIMARY KEY,
    idcliente INT NOT NULL,
    idaparelho INT NOT NULL,
    tecnicoresponsavel INT NOT NULL,
    atendente INT NOT NULL,
    descricaoocorrencia TEXT NOT NULL,
    comentariotecnico TEXT,
    tiposervico VARCHAR(255),
    valorservico DECIMAL(10, 2),
    tipopagamento ENUM('a vista', 'Parcelado'),
    formapagamento ENUM('Cartão Crédito', 'Débito', 'Pix', 'Boleto'),
    prazo DATE,
    datacriacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('Finalizado', 'Em Manutenção', 'Aguardando Resposta Cliente') NOT NULL,
    enderecoentrega INT,
    FOREIGN KEY (idcliente) REFERENCES Cliente(idcliente),
    FOREIGN KEY (idaparelho) REFERENCES Aparelho(idaparelho),
    FOREIGN KEY (tecnicoresponsavel) REFERENCES Funcionario(idfuncionario),
    FOREIGN KEY (atendente) REFERENCES Funcionario(idfuncionario)
);

-- SELECT user, host FROM mysql.user;
