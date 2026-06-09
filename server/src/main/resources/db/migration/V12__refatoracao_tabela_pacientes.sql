ALTER TABLE pacientes
    DROP COLUMN responsavel,
    DROP COLUMN idade,
    ADD COLUMN data_nascimento DATE NOT NULL AFTER sexo,
    ADD COLUMN nome_mae VARCHAR(100) NOT NULL AFTER data_nascimento,
    ADD COLUMN nome_pai VARCHAR(100) AFTER nome_mae;
