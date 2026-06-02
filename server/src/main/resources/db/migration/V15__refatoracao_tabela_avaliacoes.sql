ALTER TABLE avaliacoes
    MODIFY COLUMN id_usuario INT NULL,
    ADD COLUMN origem ENUM('PROFISSIONAL', 'RESPONSAVEL') NOT NULL AFTER id_usuario,
    ADD COLUMN exame_dna ENUM('SIM','NAO','NAO_SEI') NOT NULL,
    ADD COLUMN interesse_exame ENUM('SIM','NAO','NAO_SEI') NULL,
    ADD COLUMN resultado_exame ENUM('MUTACAO_COMPLETA','PRE_MUTACAO','ZONA_GRAY','MOSAICISMO','NEGATIVO_XF','NAO_SEI') NULL,
    ADD COLUMN diagnostico_autismo ENUM('SIM','NAO','NAO_SEI') NOT NULL,
    ADD COLUMN possui_irmaos ENUM('SIM','NAO','NAO_SEI') NOT NULL,
    ADD COLUMN antecedentes_deficiencia ENUM('SIM','NAO','NAO_SEI') NOT NULL,
    ADD COLUMN antecedentes_menopausa ENUM('SIM','NAO','NAO_SEI') NOT NULL,
    ADD COLUMN antecedentes_ataxia ENUM('SIM','NAO','NAO_SEI') NOT NULL;
