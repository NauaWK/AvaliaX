-- Tabela de Usuários
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ROLE_ADMIN', 'ROLE_USER') NOT NULL
);

-- Tabela de Pacientes
CREATE TABLE pacientes (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sexo CHAR(1) NOT NULL,
    idade INT,
    responsavel VARCHAR(100)
);

-- Tabela de Sintomas
CREATE TABLE sintomas (
    id_sintoma INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    peso_masculino DECIMAL(5,2),
    peso_feminino DECIMAL(5,2)
);

-- Tabela de Avaliações
CREATE TABLE avaliacoes (
    id_avaliacao INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_usuario INT NOT NULL,
    data_avaliacao DATE NOT NULL,
    score DECIMAL(4,2),
    recomendacao VARCHAR(100),
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- Tabela intermediária Avaliação_Sintoma
CREATE TABLE avaliacao_sintoma (
    id_avaliacao INT NOT NULL,
    id_sintoma INT NOT NULL,
    presente BOOLEAN NOT NULL,
    PRIMARY KEY (id_avaliacao, id_sintoma),
    FOREIGN KEY (id_avaliacao) REFERENCES avaliacoes(id_avaliacao),
    FOREIGN KEY (id_sintoma) REFERENCES sintomas(id_sintoma)
);
