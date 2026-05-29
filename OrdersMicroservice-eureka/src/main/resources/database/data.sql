-- Datos de prueba para órdenes
INSERT INTO orders (order_number, customer_id, status, created_at, updated_at, billing_name, billing_nif, billing_address, billing_city, billing_zip, billing_country, subtotal, discount, tax_rate, tax_amount, total, notes)
VALUES
('ORD-2024-001', 1, 'PENDING', datetime('now'), datetime('now'), 'Juan García López', '12345678A', 'Calle Principal 123', 'Madrid', '28001', 'España', 49.98, 0.00, 0.21, 10.50, 60.48, 'Primera orden del cliente'),
('ORD-2024-002', 2, 'CONFIRMED', datetime('now', '-2 days'), datetime('now', '-1 day'), 'María Rodríguez', '87654321B', 'Avenida Central 456', 'Barcelona', '08002', 'España', 99.97, 9.99, 0.21, 18.98, 108.96, 'Orden confirmada'),
('ORD-2024-003', 1, 'SHIPPED', datetime('now', '-5 days'), datetime('now', '-3 days'), 'Juan García López', '12345678A', 'Calle Principal 123', 'Madrid', '28001', 'España', 29.99, 0.00, 0.21, 6.30, 36.29, 'Orden enviada'),
('ORD-2024-004', 3, 'DELIVERED', datetime('now', '-10 days'), datetime('now', '-5 days'), 'Pedro Sánchez', '11111111C', 'Plaza Mayor 789', 'Valencia', '46001', 'España', 79.95, 0.00, 0.21, 16.79, 96.74, 'Orden entregada');

-- Datos de líneas de órdenes
INSERT INTO order_lines (order_id, book_id, quantity, unit_price, discount_pct, line_total)
VALUES
(1, 1, 1, 24.99, 0.00, 24.99),
(1, 2, 1, 24.99, 0.00, 24.99),
(2, 3, 1, 34.99, 0.00, 34.99),
(2, 4, 1, 34.99, 0.00, 34.99),
(2, 5, 1, 29.99, 10.00, 26.99),
(3, 1, 1, 29.99, 0.00, 29.99),
(4, 2, 1, 24.99, 0.00, 24.99),
(4, 3, 1, 19.99, 0.00, 19.99),
(4, 6, 1, 34.97, 0.00, 34.97);

-- Datos de log de estado
INSERT INTO order_status_log (order_id, from_status, to_status, changed_at, changed_by)
VALUES
(1, 'NONE', 'PENDING', datetime('now'), 'SYSTEM'),
(2, 'NONE', 'PENDING', datetime('now', '-2 days'), 'SYSTEM'),
(2, 'PENDING', 'CONFIRMED', datetime('now', '-1 day'), 'SYSTEM'),
(3, 'NONE', 'PENDING', datetime('now', '-5 days'), 'SYSTEM'),
(3, 'PENDING', 'CONFIRMED', datetime('now', '-4 days'), 'SYSTEM'),
(3, 'CONFIRMED', 'SHIPPED', datetime('now', '-3 days'), 'SYSTEM'),
(4, 'NONE', 'PENDING', datetime('now', '-10 days'), 'SYSTEM'),
(4, 'PENDING', 'CONFIRMED', datetime('now', '-9 days'), 'SYSTEM'),
(4, 'CONFIRMED', 'SHIPPED', datetime('now', '-7 days'), 'SYSTEM'),
(4, 'SHIPPED', 'DELIVERED', datetime('now', '-5 days'), 'SYSTEM');

