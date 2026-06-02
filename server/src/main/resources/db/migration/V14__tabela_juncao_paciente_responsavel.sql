CREATE TABLE paciente_responsavel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_responsavel INT NOT NULL,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (id_responsavel) REFERENCES responsaveis(id_responsavel)
);

