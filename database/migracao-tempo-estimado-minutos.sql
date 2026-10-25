
ALTER TABLE servicos ADD COLUMN tempo_estimado_minutos INT NULL AFTER nome;

UPDATE servicos
   SET tempo_estimado_minutos = CAST(TRIM(tempo_estimado) AS UNSIGNED)
 WHERE TRIM(tempo_estimado) REGEXP '^[0-9]+$';

UPDATE servicos
   SET tempo_estimado_minutos = CAST(REGEXP_SUBSTR(tempo_estimado, '[0-9]+') AS UNSIGNED)
 WHERE LOWER(REPLACE(tempo_estimado, ' ', '')) REGEXP '^[0-9]+(min|mins|minuto|minutos)$';

UPDATE servicos
   SET tempo_estimado_minutos = CAST(REGEXP_SUBSTR(tempo_estimado, '[0-9]+') AS UNSIGNED) * 60
 WHERE LOWER(REPLACE(tempo_estimado, ' ', '')) REGEXP '^[0-9]+(h|hr|hora|horas)$';

UPDATE servicos
   SET tempo_estimado_minutos = CAST(REGEXP_SUBSTR(tempo_estimado, '[0-9]+', 1, 1) AS UNSIGNED) * 60
                              + CAST(REGEXP_SUBSTR(tempo_estimado, '[0-9]+', 1, 2) AS UNSIGNED)
 WHERE LOWER(REPLACE(tempo_estimado, ' ', '')) REGEXP '^[0-9]+h[0-9]+(min|m)?$';

SELECT codigo, nome, tempo_estimado, tempo_estimado_minutos
  FROM servicos
 WHERE tempo_estimado_minutos IS NULL OR tempo_estimado_minutos <= 0;

ALTER TABLE servicos
    MODIFY tempo_estimado_minutos INT NOT NULL,
    ADD CONSTRAINT chk_servicos_tempo_estimado_minutos CHECK (tempo_estimado_minutos > 0),
    DROP COLUMN tempo_estimado;
