ALTER TABLE pacientes
ADD COLUMN id_usuario INT;

ALTER TABLE pacientes
ADD CONSTRAINT fk_paciente_usuario
FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario);

