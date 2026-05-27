ALTER TABLE avaliacao_sintoma
    DROP PRIMARY KEY,
    ADD COLUMN id INT AUTO_INCREMENT PRIMARY KEY FIRST,
    ADD CONSTRAINT uq_avaliacao_sintoma UNIQUE (id_avaliacao, id_sintoma);
