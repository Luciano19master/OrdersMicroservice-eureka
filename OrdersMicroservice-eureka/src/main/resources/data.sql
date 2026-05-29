-- ============================================================
--  ORDERS MICROSERVICE — DML (Datos de ejemplo)
-- ============================================================

INSERT INTO orders (order_number, customer_id, status, billing_name, billing_nif, billing_address, billing_city, billing_zip, billing_country, subtotal, discount, tax_rate, notes)
VALUES
  ('ORD-1001', 1, 'pending', 'Juan Pérez', '12345678A', 'Calle Falsa 123', 'Madrid', '28001', 'ES', 100.00, 5.00, 21, 'Primera orden de ejemplo'),
  ('ORD-1002', 2, 'confirmed', 'Ana Gómez', '87654321B', 'Avenida Siempre Viva 742', 'Barcelona', '08001', 'ES', 200.00, 10.00, 21, 'Segunda orden de ejemplo');

-- 20 órdenes adicionales
INSERT INTO orders (order_number, customer_id, status, billing_name, billing_nif, billing_address, billing_city, billing_zip, billing_country, subtotal, discount, tax_rate, notes) VALUES
  ('ORD-1003', 3, 'shipped', 'Luis Martínez', '11223344C', 'Calle Luna 5', 'Sevilla', '41001', 'ES', 150.00, 0.00, 21, 'Orden enviada'),
  ('ORD-1004', 4, 'delivered', 'Marta Ruiz', '22334455D', 'Calle Sol 8', 'Valencia', '46001', 'ES', 80.00, 2.00, 21, 'Orden entregada'),
  ('ORD-1005', 5, 'pending', 'Carlos López', '33445566E', 'Calle Mar 10', 'Bilbao', '48001', 'ES', 120.00, 0.00, 21, 'Pendiente de pago'),
  ('ORD-1006', 6, 'cancelled', 'Elena Torres', '44556677F', 'Calle Río 12', 'Zaragoza', '50001', 'ES', 60.00, 1.00, 21, 'Cancelada por cliente'),
  ('ORD-1007', 7, 'confirmed', 'Pedro Sánchez', '55667788G', 'Calle Lago 14', 'Málaga', '29001', 'ES', 210.00, 15.00, 21, 'Confirmada'),
  ('ORD-1008', 8, 'pending', 'Lucía Fernández', '66778899H', 'Calle Bosque 16', 'Murcia', '30001', 'ES', 95.00, 0.00, 21, 'Pendiente'),
  ('ORD-1009', 9, 'shipped', 'Javier Castro', '77889900I', 'Calle Nube 18', 'Alicante', '03001', 'ES', 175.00, 5.00, 21, 'Enviado'),
  ('ORD-1010', 10, 'delivered', 'Sara Molina', '88990011J', 'Calle Arena 20', 'Santander', '39001', 'ES', 130.00, 0.00, 21, 'Entregada'),
  ('ORD-1011', 11, 'pending', 'David Romero', '99001122K', 'Calle Brisa 22', 'Valladolid', '47001', 'ES', 110.00, 3.00, 21, 'Pendiente'),
  ('ORD-1012', 12, 'confirmed', 'Patricia Gil', '10111213L', 'Calle Olivo 24', 'Granada', '18001', 'ES', 140.00, 0.00, 21, 'Confirmada'),
  ('ORD-1013', 13, 'shipped', 'Alberto Ruiz', '12131415M', 'Calle Cedro 26', 'Toledo', '45001', 'ES', 160.00, 8.00, 21, 'Enviado'),
  ('ORD-1014', 14, 'delivered', 'Cristina León', '13141516N', 'Calle Sauce 28', 'Salamanca', '37001', 'ES', 70.00, 0.00, 21, 'Entregada'),
  ('ORD-1015', 15, 'pending', 'Miguel Navarro', '14151617O', 'Calle Pino 30', 'Córdoba', '14001', 'ES', 125.00, 2.00, 21, 'Pendiente'),
  ('ORD-1016', 16, 'cancelled', 'Teresa Ramos', '15161718P', 'Calle Roble 32', 'Burgos', '09001', 'ES', 85.00, 0.00, 21, 'Cancelada'),
  ('ORD-1017', 17, 'confirmed', 'Andrés Vega', '16171819Q', 'Calle Fresno 34', 'Logroño', '26001', 'ES', 190.00, 10.00, 21, 'Confirmada'),
  ('ORD-1018', 18, 'pending', 'Rosa Herrera', '17181920R', 'Calle Haya 36', 'León', '24001', 'ES', 105.00, 0.00, 21, 'Pendiente'),
  ('ORD-1019', 19, 'shipped', 'Sergio Ortega', '18192021S', 'Calle Abeto 38', 'Almería', '04001', 'ES', 155.00, 7.00, 21, 'Enviado'),
  ('ORD-1020', 20, 'delivered', 'Isabel Peña', '19202122T', 'Calle Laurel 40', 'Huelva', '21001', 'ES', 135.00, 0.00, 21, 'Entregada'),
  ('ORD-1021', 21, 'pending', 'Raúl Castro', '20212223U', 'Calle Olmo 42', 'Cuenca', '16001', 'ES', 115.00, 4.00, 21, 'Pendiente'),
  ('ORD-1022', 22, 'confirmed', 'Beatriz Soto', '21222324V', 'Calle Encina 44', 'Guadalajara', '19001', 'ES', 145.00, 0.00, 21, 'Confirmada');

INSERT INTO order_lines (order_id, book_id, quantity, unit_price, discount_pct)
VALUES
  (1, 101, 2, 25.00, 0),
  (1, 102, 1, 50.00, 10),
  (2, 103, 4, 40.00, 5);

-- Líneas de orden para las órdenes adicionales
INSERT INTO order_lines (order_id, book_id, quantity, unit_price, discount_pct) VALUES
  (3, 104, 1, 30.00, 0),
  (3, 105, 2, 45.00, 5),
  (4, 106, 1, 80.00, 0),
  (5, 107, 3, 40.00, 10),
  (6, 108, 2, 20.00, 0),
  (7, 109, 1, 210.00, 15),
  (8, 110, 2, 47.50, 0),
  (9, 111, 1, 175.00, 5),
  (10, 112, 2, 65.00, 0),
  (11, 113, 1, 110.00, 3),
  (12, 114, 2, 70.00, 0),
  (13, 115, 1, 152.00, 8),
  (14, 116, 2, 35.00, 0),
  (15, 117, 1, 125.00, 2),
  (16, 118, 2, 42.50, 0),
  (17, 119, 1, 190.00, 10),
  (18, 120, 2, 52.50, 0),
  (19, 121, 1, 155.00, 7),
  (20, 122, 2, 67.50, 0),
  (21, 123, 1, 115.00, 4),
  (22, 124, 2, 72.50, 0);
