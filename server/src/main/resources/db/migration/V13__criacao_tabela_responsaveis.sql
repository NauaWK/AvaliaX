CREATE TABLE responsaveis (
    id_responsavel INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    CPF VARCHAR(11) NOT NULL UNIQUE, -- CPF sem máscara
    grau_parentesco VARCHAR(50) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    whatsapp VARCHAR(20),
    telefone1 VARCHAR(20) NOT NULL,
    telefone2 VARCHAR(20),
    email VARCHAR(255)
);
