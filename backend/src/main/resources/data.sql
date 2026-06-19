INSERT INTO usuarios (cedula, password, nombre, email, rol, activo)
VALUES
    ('1000000001', '$2a$10$1Upa.FO1OIncX3ku9AWQCOgcIkP4d8U1rtP.6QMFXEugdC1T5U7/6', 'Administrador Principal', 'admin@compo.com', 'ADMIN', true),
    ('1000000002', '$2a$10$Ftvr0S6P.cktSJOTQL.xEezf69G0Q7dLHS91QK.lhdI45/Ubyknlu', 'Usuario Estándar', 'user@compo.com', 'USER', true)
ON CONFLICT (cedula) DO UPDATE SET password = EXCLUDED.password;

-- Compositores
INSERT INTO compositores (nombre, apellido, nacionalidad, fecha_nacimiento, activo)
VALUES
    ('Ludwig van', 'Beethoven', 'Alemán', '1770-12-17', true),
    ('Wolfgang Amadeus', 'Mozart', 'Austriaco', '1756-01-27', true),
    ('Johann Sebastian', 'Bach', 'Alemán', '1685-03-21', true)
ON CONFLICT DO NOTHING;

-- Directores
INSERT INTO directores (nombre, apellido, nacionalidad, fecha_nacimiento, activo)
VALUES
    ('Herbert von', 'Karajan', 'Austriaco', '1908-04-05', true),
    ('Carlos', 'Kleiber', 'Alemán', '1930-07-03', true)
ON CONFLICT DO NOTHING;

-- Interpretes
INSERT INTO interpretes (nombre, apellido, tipo_voz_o_instrumento, nacionalidad, fecha_nacimiento, activo)
VALUES
    ('Plácido', 'Domingo', 'Tenor', 'Español', '1941-01-21', true),
    ('Martha', 'Argerich', 'Piano', 'Argentina', '1941-06-05', true),
    ('Itzhak', 'Perlman', 'Violín', 'Israelí', '1945-08-31', true)
ON CONFLICT DO NOTHING;

-- Obras
INSERT INTO obras (titulo, descripcion, genero, fecha_creacion, activo)
VALUES
    ('Sinfonía N° 9 en Re menor', 'La famosa novena sinfonía de Beethoven, con el himno Oda a la Alegría en el cuarto movimiento.', 'Sinfonía', '1824-05-07', true),
    ('Requiem en Re menor K. 626', 'El réquiem inacabado de Mozart, completado por Franz Xaver Süssmayr.', 'Música Sacra', '1791-12-05', true)
ON CONFLICT DO NOTHING;

-- Relación Obra - Compositor (obras_compositores)
-- Sinfonía N°9 → Beethoven
INSERT INTO obras_compositores (obra_id, compositor_id)
SELECT o.id, c.id
FROM obras o, compositores c
WHERE o.titulo = 'Sinfonía N° 9 en Re menor' AND c.apellido = 'Beethoven'
ON CONFLICT DO NOTHING;

-- Requiem → Mozart y Bach (obra con dos compositores como ejemplo)
INSERT INTO obras_compositores (obra_id, compositor_id)
SELECT o.id, c.id
FROM obras o, compositores c
WHERE o.titulo = 'Requiem en Re menor K. 626' AND c.apellido = 'Mozart'
ON CONFLICT DO NOTHING;

INSERT INTO obras_compositores (obra_id, compositor_id)
SELECT o.id, c.id
FROM obras o, compositores c
WHERE o.titulo = 'Requiem en Re menor K. 626' AND c.apellido = 'Bach'
ON CONFLICT DO NOTHING;

-- Interpretaciones
INSERT INTO interpretaciones (obra_id, interprete_id, director_id, fecha_interpretacion, lugar, observaciones, activo)
SELECT o.id, i.id, d.id, '2023-10-15', 'Teatro Colón, Buenos Aires', 'Interpretación magistral en el bicentenario del estreno.', true
FROM obras o, interpretes i, directores d
WHERE o.titulo = 'Sinfonía N° 9 en Re menor'
  AND i.apellido = 'Perlman'
  AND d.apellido = 'Karajan'
ON CONFLICT DO NOTHING;

INSERT INTO interpretaciones (obra_id, interprete_id, director_id, fecha_interpretacion, lugar, observaciones, activo)
SELECT o.id, i.id, d.id, '2023-11-20', 'Auditorio Nacional, Madrid', 'Primera visita de la soprano al auditorio madrileño.', true
FROM obras o, interpretes i, directores d
WHERE o.titulo = 'Requiem en Re menor K. 626'
  AND i.apellido = 'Domingo'
  AND d.apellido = 'Kleiber'
ON CONFLICT DO NOTHING;

INSERT INTO interpretaciones (obra_id, interprete_id, director_id, fecha_interpretacion, lugar, observaciones, activo)
SELECT o.id, i.id, d.id, '2024-03-08', 'Filarmónica de Berlín', 'Concierto especial por el día internacional de la mujer.', true
FROM obras o, interpretes i, directores d
WHERE o.titulo = 'Sinfonía N° 9 en Re menor'
  AND i.apellido = 'Argerich'
  AND d.apellido = 'Karajan'
ON CONFLICT DO NOTHING;
