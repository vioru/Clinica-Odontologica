INSERT INTO ODONTOLOGOS(MATRICULA, NOMBRE, APELLIDO)
VALUES
('mat-001', 'Nick', 'Riviera'),
('mat-002','Gregory','House'),
('mat-003','Julius','Hibbert'),
('mat-004','John','Zoidberg'),
('mat-005','Fulano','Mengueche'),
('mat-006','Aquiles','Mata'),
('mat-007','Albert','Jotas'),
('mat-008','Douglas','Powers');

INSERT INTO DOMICILIOS(CALLE, NUMERO, LOCALIDAD, PROVINCIA)
VALUES
('Av Siempre Viva', 742, 'Springfield', 'Massachusetts'),
('Calle Wallaby', 42, 'Sydney', 'Nueva Gales del Sur'),
('Av juarez',54, 'Paez','Lomas'),
('Romero',85, 'Lomas','Lima');


INSERT INTO PACIENTES (NOMBRE, APELLIDO, DNI, FECHA_INGRESO, DOMICILIO_ID)
VALUES
('Juan', 'Perez', 35464856,'2023-05-24', 1),
('Juana', 'Lopez', 3548546, '2023-05-24', 2),
('Luisa','Perez', 35464856, '2024-09-04', 3),
('Carla','Florez', 12345678, '2024-09-04', 4);

INSERT INTO TURNOS (PACIENTE_ID, ODONTOLOGO_ID, FECHA_HORA)
VALUES
(1, 1, '2023-06-15'),
(2, 2, '2023-06-18');
