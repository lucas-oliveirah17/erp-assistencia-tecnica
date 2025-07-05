CREATE DATABASE IF NOT EXISTS projeto_apo;
-- DROP DATABASE projeto_apo;

USE projeto_apo;

-- ==========================================================================================
-- TABELA tb_funcionario
-- Armazena os dados dos funcionários da empresa.
-- ==========================================================================================
CREATE TABLE IF NOT EXISTS tb_funcionario (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE, -- CPF no formato "000.000.000-00"
    funcao ENUM(
        'tecnico', 'atendente', 'gerente', 'rh', 'diretor'
    ) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    ativo BOOLEAN DEFAULT TRUE
);

-- ==========================================================================================
-- TABELA tb_usuario
-- Armazena os dados do sistema de login da assistência técnica.
-- ==========================================================================================
CREATE TABLE IF NOT EXISTS tb_usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_funcionario INT NOT NULL UNIQUE,
    usuario VARCHAR(50) NOT NULL UNIQUE, -- Nome de usuário para login
    senha VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,   -- Deve ser igual ao email do funcionário
    privilegios ENUM('administrador', 'usuario') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_funcionario) REFERENCES tb_funcionario(id_funcionario) ON DELETE CASCADE
);

DELIMITER $$

-- Trigger: Antes de inserir na tabela tb_usuario, verifica se o email informado
-- é igual ao email cadastrado para o funcionário correspondente na tb_funcionario.
CREATE TRIGGER trg_usu_email_check BEFORE INSERT ON tb_usuario
FOR EACH ROW
BEGIN
    DECLARE func_email VARCHAR(255);
    SELECT email INTO func_email FROM tb_funcionario WHERE id_funcionario = NEW.id_funcionario;
    
    IF func_email IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Funcionário não encontrado';
    ELSEIF func_email <> NEW.email THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email do usuário deve ser igual ao do funcionário';
    END IF;
END$$

-- Trigger: Antes de atualizar a tabela tb_usuario, verifica se o email informado
-- permanece igual ao email do funcionário na tb_funcionario.
CREATE TRIGGER trg_usu_email_check_update BEFORE UPDATE ON tb_usuario
FOR EACH ROW
BEGIN
    DECLARE func_email VARCHAR(255);
    SELECT email INTO func_email FROM tb_funcionario WHERE id_funcionario = NEW.id_funcionario;

    IF func_email IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Funcionário não encontrado';
    ELSEIF func_email <> NEW.email THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email do usuário deve ser igual ao do funcionário';
    END IF;
END$$

-- Trigger: Após atualização do email na tabela tb_funcionario,
-- atualiza automaticamente o email correspondente na tabela tb_usuario.
CREATE TRIGGER trg_funcionario_email_update AFTER UPDATE ON tb_funcionario
FOR EACH ROW
BEGIN
    -- Só executa se o email realmente mudou
    IF OLD.email <> NEW.email THEN
        UPDATE tb_usuario
        SET email = NEW.email
        WHERE id_funcionario = NEW.id_funcionario;
    END IF;
END$$

DELIMITER ;
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
    complemento VARCHAR(255),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    uf ENUM(
        'AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT',
        'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO',
        'RR', 'SC', 'SP', 'SE', 'TO'
    ) NOT NULL,
    cep VARCHAR(9),
    ativo BOOLEAN DEFAULT TRUE
);
-- DROP TABLE tb_cliente;
-- SELECT * FROM tb_cliente;
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
    capacidade INT,
    ativo BOOLEAN DEFAULT TRUE
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
    categoria ENUM(
		'televisao', 'monitor', 'home_theater', 'celular', 'tablet', 'aparelho_som', 
		'notebook', 'computador'
	) NOT NULL,
    dimensao VARCHAR(50),
    peso DECIMAL(10, 2),
    proprietario INT NOT NULL,
    id_estoque_aparelho INT,
    garantia TEXT,
    estado ENUM('em_manutencao', 'em_estoque', 'em_entrega') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (proprietario) REFERENCES tb_cliente(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_estoque_aparelho) REFERENCES tb_estoque_aparelho(id_estoque_aparelho) ON DELETE CASCADE
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
    tipo_servico ENUM('manutencao', 'orcamento', 'reparo'),
    valor_servico DECIMAL(10, 2),
    tipo_pagamento ENUM('avista', 'parcelado'),
    forma_pagamento ENUM('cartao_credito', 'debito', 'pix', 'boleto'),
    prazo DATE,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('finalizado', 'em_manutencao', 'aguardando_resposta') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_cliente) REFERENCES tb_cliente(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_aparelho) REFERENCES tb_aparelho(id_aparelho) ON DELETE CASCADE,
    FOREIGN KEY (tecnico_responsavel) REFERENCES tb_funcionario(id_funcionario) ON DELETE CASCADE,
    FOREIGN KEY (atendente) REFERENCES tb_funcionario(id_funcionario) ON DELETE CASCADE
);
-- DROP TABLE tb_ordem_servico;
-- SELECT * FROM tb_ordem_servico;

-- ==========================================================================================
-- INSERÇÕES PARA DADOS INICIAIS DO BANCO DE DADOS
-- ==========================================================================================
INSERT INTO tb_funcionario (nome, cpf, funcao, telefone, email) VALUES
('Dummy Admin', '000.000.000-00', 'diretor', '(00)00000-0000', 'admin@apo.com'),
('Dummy User', '000.000.000-01', 'diretor', '(00)00000-0000', 'user@apo.com'),

('Cleber Oliveira', '567.567.567-56', 'diretor', '(51)95678-9012', 'cleber@apo.com'),
('Lucas Oliveira', '901.901.901-90', 'gerente', '(48)99012-3456', 'lucas@apo.com'),
('Guilherme Dionizo', '012.012.012-01', 'gerente', '(11)90123-4567', 'guilherme@apo.com'),
('Tatiane Freitas', '890.890.890-89', 'rh', '(85)98901-2345', 'tatiane@apo.com'),
('Carlos Mendes', '123.123.123-12', 'tecnico', '(11)91234-5678', 'carlos@apo.com'),
('Fernanda Lima', '234.234.234-23', 'atendente', '(21)92345-6789', 'fernanda@apo.com'),
('Joana Silva', '345.345.345-34', 'tecnico', '(31)93456-7890', 'joana@apo.com'),
('Paulo Henrique', '456.456.456-45', 'tecnico', '(41)94567-8901', 'paulo@apo.com'),
('Vanessa Rocha', '678.678.678-67', 'atendente', '(71)96789-0123', 'vanessa@apo.com'),
('Bruno Soares', '789.789.789-78', 'tecnico', '(62)97890-1234', 'bruno@apo.com');

-- Usuários (com privilégio correto e dois de teste "admin" e "user")
INSERT INTO tb_usuario (id_funcionario, usuario, senha, email, privilegios) VALUES
(1, 'admin', '123', 'admin@apo.com', 'administrador'), -- usuário admin para testes
(2, 'user', '123', 'user@apo.com', 'usuario'),         -- usuário user para testes

(3, 'clebero', '123', 'cleber@apo.com', 'administrador'),  -- diretor admin
(4, 'lucaso', '123', 'lucas@apo.com', 'administrador'),    -- gerente admin
(5, 'guilhermed', '123', 'guilherme@apo.com', 'administrador'), -- gerente admin
(6, 'tatianef', '123', 'tatiane@apo.com', 'administrador'), -- rh admin
(7, 'carlosm', '123', 'carlos@apo.com', 'usuario'),
(8, 'fernandal', '123', 'fernanda@apo.com', 'usuario'),
(9, 'joanas', '123', 'joana@apo.com', 'usuario'),
(10, 'pauloh', '123', 'paulo@apo.com', 'usuario'),
(11, 'vanessar', '123', 'vanessa@apo.com', 'usuario'),
(12, 'brunos', '123', 'bruno@apo.com', 'usuario');

INSERT INTO tb_cliente (nome, cpf_cnpj, tipo_cliente, telefone, email, endereco, numero, complemento, bairro, cidade, uf, cep) VALUES
('Lucas Almeida', '123.456.789-01', 'pf', '(11)91234-5678', 'lucas@email.com', 'Rua das Flores', '123', '', 'Centro', 'São Paulo', 'SP', '01001-000'),
('Empresa XYZ', '12.345.678/0001-00', 'pj', '(11)99876-5432', 'contato@xyz.com.br', 'Av. Paulista', '1000', '10º Andar', 'Bela Vista', 'São Paulo', 'SP', '01310-000'),
('Maria Souza', '987.654.321-00', 'pf', '(21)91234-5678', 'maria@email.com', 'Rua das Acácias', '321', '', 'Copacabana', 'Rio de Janeiro', 'RJ', '22050-002'),
('João Silva', '456.789.123-45', 'pf', '(31)98888-7777', 'joao@email.com', 'Av. Afonso Pena', '200', '', 'Centro', 'Belo Horizonte', 'MG', '30130-003'),
('TechCorp Ltda', '11.111.111/0001-11', 'pj', '(48)99999-9999', 'suporte@techcorp.com', 'Rua da Inovação', '45', 'Bloco B', 'Estreito', 'Florianópolis', 'SC', '88075-000'),
('Cláudia Ramos', '111.222.333-44', 'pf', '(85)98888-0000', 'claudia@email.com', 'Rua das Palmeiras', '85', '', 'Aldeota', 'Fortaleza', 'CE', '60150-001'),
('Construtora Alfa', '55.444.333/0001-55', 'pj', '(62)91234-1111', 'contato@alfa.com', 'Av. T-63', '740', '', 'Bueno', 'Goiânia', 'GO', '74230-010'),
('Rafael Torres', '222.333.444-55', 'pf', '(51)93456-7890', 'rafael@email.com', 'Rua Central', '55', '', 'Centro', 'Porto Alegre', 'RS', '90010-320'),
('Isabela Mendes', '333.444.555-66', 'pf', '(41)97654-3210', 'isa@email.com', 'Av. República', '10', 'Apto 102', 'Água Verde', 'Curitiba', 'PR', '80240-140'),
('Bruna Lima', '444.555.666-77', 'pf', '(71)99999-8888', 'bruna@email.com', 'Rua das Laranjeiras', '22', '', 'Barra', 'Salvador', 'BA', '40140-000');

INSERT INTO tb_cliente (nome, cpf_cnpj, tipo_cliente, telefone, email, endereco, numero, complemento, bairro, cidade, uf, cep) VALUES
('Ana Carolina', '987.321.654-00', 'pf', '(11)93456-7890', 'ana.carolina@email.com', 'Rua do Sol', '88', '', 'Vila Mariana', 'São Paulo', 'SP', '04112-000'),
('Conserta Tudo Ltda', '77.555.333/0001-77', 'pj', '(21)97890-1234', 'contato@consertatudoltda.com', 'Rua das Flores', '500', 'Sala 101', 'Centro', 'Rio de Janeiro', 'RJ', '20031-050'),
('Felipe Mendes', '741.852.963-21', 'pf', '(31)98765-4321', 'felipe.mendes@email.com', 'Av. dos Andradas', '1234', '', 'Lourdes', 'Belo Horizonte', 'MG', '30140-120'),
('GreenTech Soluções', '88.444.222/0001-88', 'pj', '(48)99123-4567', 'contato@greentech.com.br', 'Rua das Palmeiras', '300', '', 'Trindade', 'Florianópolis', 'SC', '88035-210'),
('Juliana Ribeiro', '159.753.486-00', 'pf', '(85)99999-8888', 'juliana.ribeiro@email.com', 'Rua do Comércio', '67', '', 'Meireles', 'Fortaleza', 'CE', '60125-000'),
('Metalúrgica RS', '44.333.222/0001-44', 'pj', '(51)92345-6789', 'contato@metalurgicars.com', 'Rua das Indústrias', '110', 'Galpão 4', 'Distrito Industrial', 'Porto Alegre', 'RS', '91740-000'),
('Patrícia Santos', '963.852.741-99', 'pf', '(41)98765-4321', 'patricia.santos@email.com', 'Av. Paraná', '777', 'Ap 1502', 'Centro', 'Curitiba', 'PR', '80060-000'),
('Construtora Beta', '99.888.777/0001-99', 'pj', '(62)91234-5678', 'contato@betaconstrucoes.com', 'Av. Brasil', '1200', '', 'Setor Oeste', 'Goiânia', 'GO', '74085-210'),
('Ricardo Alves', '852.741.963-11', 'pf', '(21)98765-1111', 'ricardo.alves@email.com', 'Rua das Laranjeiras', '88', 'Casa', 'Botafogo', 'Rio de Janeiro', 'RJ', '22240-004'),
('Supermercado Central Sul', '55.666.444/0001-55', 'pj', '(31)99876-5432', 'contato@supercentralsul.com', 'Rua do Comércio', '900', '', 'Santa Efigênia', 'Belo Horizonte', 'MG', '30210-110'),
('Vanessa Costa', '147.258.369-00', 'pf', '(27)99999-7777', 'vanessa.costa@email.com', 'Rua XV de Novembro', '230', '', 'Jardim Camburi', 'Vitória', 'ES', '29055-440'),
('Vidraçaria Luz', '22.111.333/0001-22', 'pj', '(41)91111-2222', 'contato@vidracarialuz.com', 'Av. Água Verde', '145', '', 'Água Verde', 'Curitiba', 'PR', '80240-000'),
('Wesley Ferreira', '753.159.852-00', 'pf', '(85)98888-4444', 'wesley.ferreira@email.com', 'Rua da Paz', '520', '', 'Aldeota', 'Fortaleza', 'CE', '60150-120'),
('XTech Informática', '88.777.666/0001-88', 'pj', '(48)98888-5555', 'contato@xtech.com.br', 'Rua das Orquídeas', '77', 'Sala 204', 'Trindade', 'Florianópolis', 'SC', '88036-210'),
('Yara Lima', '369.258.147-00', 'pf', '(51)91234-7777', 'yara.lima@email.com', 'Rua da Liberdade', '101', '', 'Moinhos de Vento', 'Porto Alegre', 'RS', '90540-000'),
('Zap Comércio', '66.555.444/0001-66', 'pj', '(11)99999-3333', 'contato@zapcomercio.com', 'Av. das Nações', '505', 'Loja 3', 'Itaim Bibi', 'São Paulo', 'SP', '04530-010'),
('André Gomes', '258.147.369-00', 'pf', '(62)93333-8888', 'andre.gomes@email.com', 'Rua 5', '90', 'Casa', 'Setor Bueno', 'Goiânia', 'GO', '74223-020'),
('Bella Flores', '99.666.555-44', 'pj', '(71)99988-1111', 'contato@bellaflores.com', 'Rua Chile', '88', '', 'Campo Grande', 'Salvador', 'BA', '40285-030'),
('Carlos Moreira', '357.951.456-00', 'pf', '(21)92345-6789', 'carlos.moreira@email.com', 'Rua Visconde de Pirajá', '456', '', 'Ipanema', 'Rio de Janeiro', 'RJ', '22410-002'),
('Delícias da Casa', '33.222.111/0001-33', 'pj', '(31)91111-3333', 'contato@deliciasdacasa.com', 'Av. Afonso Pena', '1230', '', 'Centro', 'Belo Horizonte', 'MG', '30130-080');

INSERT INTO tb_fornecedor (razao_social, cnpj, telefone, site, tipo_fornecedor, garantia, nome_fantasia) VALUES
('Eletrônica Silva LTDA', '01.234.567/0001-01', '(11)4002-8922', 'http://eletronicasilva.com.br', 'eletrônicos', '12 meses', 'Silva Eletrônicos'),
('TecParts Distribuidora', '02.345.678/0001-02', '(21)3003-1234', 'http://tecparts.com.br', 'componentes', '6 meses', 'TecParts'),
('FastCom Peças', '03.456.789/0001-03', '(31)3222-4455', NULL, 'cabos e conectores', '3 meses', 'FastCom'),
('GigaTech Brasil', '04.567.890/0001-04', '(41)4444-5555', 'https://gigatech.com', 'hardware', '12 meses', 'GigaTech'),
('AudioPlus', '05.678.901/0001-05', '(51)9999-8888', NULL, 'áudio', '2 anos', 'AudioPlus'),
('TelaFácil SA', '06.789.012/0001-06', '(71)7777-6666', 'http://telafacil.com', 'displays', '6 meses', 'TelaFácil'),
('MobileCenter Ltda', '07.890.123/0001-07', '(62)3333-4444', NULL, 'smartphones', '1 ano', 'MobileCenter'),
('NotebookPRO', '08.901.234/0001-08', '(85)2222-1111', 'https://notebookpro.com.br', 'notebooks', '18 meses', 'NotebookPRO'),
('DataWorld', '09.012.345/0001-09', '(91)5555-6666', NULL, 'infraestrutura', '12 meses', 'DataWorld'),
('EletroMania', '10.123.456/0001-10', '(11)8888-7777', 'http://eletronet.com', 'geral', '6 meses', 'EletroMania');

INSERT INTO tb_estoque_aparelho (localizacao, categoria, dimensao, capacidade) VALUES
('Prateleira A1', 'smartphones', '30x30x30', 10),
('Prateleira B2', 'notebooks', '50x40x10', 8),
('Gaveta 1', 'tablets', '25x20x5', 15),
('Sala Técnica', 'diversos', 'grande', 5),
('Armário 2', 'acessórios', '10x10x5', 50),
('Estoque Alto', 'televisores', '120x80x20', 2),
('Prateleira C3', 'monitores', '60x40x15', 6),
('Gaveta 2', 'home_theater', '50x30x20', 4),
('Depósito', 'diversos', 'variável', 20),
('Armazém Externo', 'computadores', 'grande', 10);

INSERT INTO tb_aparelho (modelo, numero_serie, categoria, dimensao, peso, proprietario, id_estoque_aparelho, garantia, estado) VALUES
('Galaxy S21', 'SN123456789', 'celular', '15x7x0.8', 0.18, 1, 1, '1 ano pela Samsung', 'em_estoque'),
('Smart TV LG 55"', 'LG987654321', 'televisao', '122x71x5', 13.5, 3, 6, '2 anos', 'em_manutencao'),
('Notebook Dell i7', 'DL5566X9', 'notebook', '35x24x2', 2.1, 4, 2, '1 ano', 'em_estoque'),
('Tablet iPad Air', 'IPAD1234', 'tablet', '24x17x0.6', 0.5, 5, 3, '1 ano AppleCare', 'em_entrega'),
('Monitor Samsung 24"', 'SAMS24HD', 'monitor', '55x33x6', 3.2, 6, 7, '6 meses', 'em_estoque'),
('Home Theater Sony', 'HTSONY200', 'home_theater', '80x40x10', 7.0, 2, 8, '2 anos', 'em_manutencao'),
('Computador AMD Ryzen', 'CPTRYZEN5000', 'computador', '45x20x45', 10.0, 7, 10, '1 ano', 'em_estoque'),
('Celular Xiaomi Redmi', 'XMIRED123', 'celular', '15x7x0.9', 0.19, 9, 1, '1 ano', 'em_entrega'),
('Tablet Galaxy Tab A', 'GTABAXX22', 'tablet', '25x16x0.8', 0.48, 10, 3, '1 ano', 'em_estoque'),
('Notebook Lenovo', 'LN123X556', 'notebook', '34x23x2', 1.8, 8, 2, '1 ano', 'em_manutencao');

INSERT INTO tb_ordem_servico (id_cliente, id_aparelho, tecnico_responsavel, atendente, descricao_ocorrencia, comentario_tecnico, tipo_servico, valor_servico, tipo_pagamento, forma_pagamento, prazo, estado) VALUES
(1, 1, 1, 2, 'Tela quebrada', 'Substituição da tela', 'reparo', 550.00, 'avista', 'pix', '2025-07-10', 'em_manutencao'),
(3, 2, 4, 6, 'Imagem falhando', 'Placa principal danificada', 'manutencao', 850.00, 'parcelado', 'cartao_credito', '2025-07-15', 'aguardando_resposta'),
(4, 3, 7, 2, 'Travamentos frequentes', 'Limpeza e reinstalação do SO', 'manutencao', 320.00, 'avista', 'debito', '2025-07-09', 'finalizado'),
(5, 4, 1, 6, 'Vidro trincado', 'Substituído com sucesso', 'reparo', 400.00, 'avista', 'pix', '2025-07-05', 'finalizado'),
(6, 5, 10, 2, 'Não liga', 'Fonte com defeito', 'orcamento', 0.00, 'avista', 'boleto', '2025-07-08', 'aguardando_resposta'),
(2, 6, 1, 2, 'Sem áudio', 'Problema no amplificador', 'manutencao', 700.00, 'parcelado', 'cartao_credito', '2025-07-12', 'em_manutencao'),
(7, 7, 4, 6, 'Upgrade solicitado', 'Troca de SSD', 'reparo', 250.00, 'avista', 'pix', '2025-07-04', 'finalizado'),
(9, 8, 1, 2, 'Tela rachada', NULL, 'orcamento', 0.00, 'avista', 'pix', '2025-07-14', 'aguardando_resposta'),
(10, 9, 10, 6, 'Sem som', 'Alto-falante danificado', 'manutencao', 180.00, 'avista', 'debito', '2025-07-07', 'finalizado'),
(8, 10, 7, 6, 'Sistema travando', 'Atualização de BIOS e SO', 'reparo', 350.00, 'avista', 'pix', '2025-07-11', 'em_manutencao');

-- SELECT user, host FROM mysql.user;
-- SELECT VERSION();


