INSERT INTO product(store_id, name, category, price, stock, base_stock, is_deleted, created_at, updated_at) VALUES
                                                                                                                  (1, '상품 이름 1', 'MAIN_MENU', 1000, 20, 20, 'N', '2024-12-01 8:00:00', '2024-12-01 8:00:00'),
                                                                                                                  (1, '상품 이름 2', 'SUB_MENU', 500, 20, 40, 'N', '2024-12-01 8:10:00', '2024-12-01 8:10:00'),
                                                                                                                  (1, '상품 이름 3', 'SUB_MENU', 700, 20, 20, 'Y', '2024-12-01 8:20:00', '2024-12-01 8:20:00'),
                                                                                                                  (1, '상품 이름 4', 'DRINK', 300, 30, 50, 'N', '2024-12-01 8:30:00', '2024-12-01 8:30:00'),
                                                                                                                  (2, '어느 상품 이름', 'MAIN_MENU', 3000, 10, 10, 'N', '2024-12-01 8:40:00', '2024-12-01 8:40:00');

INSERT INTO trade(store_id, trade_device_id, trade_amount, created_at, is_completed) VALUES
                                                                                           (1, 1, 3600, '2024-12-01 10:20:30', 'N'),
                                                                                           (1, 2, 3900, '2024-12-01 10:25:30', 'N');

INSERT INTO order_table(trade_id, order_amount, created_at) VALUES
                                                                  (1, 2600, '2024-12-01 10:20:30'),
                                                                  (2, 3900, '2024-12-01 10:25:30'),
                                                                  (1, 1000, '2024-12-01 10:30:30');

INSERT INTO order_detail(order_id, product_id, count, total_amount, created_at) VALUES
                                                                                      (1, 2, 4, 2000, '2024-12-01 10:20:30'),
                                                                                      (1, 4, 2, 600, '2024-12-01 10:20:30'),
                                                                                      (2, 2, 6, 3000, '2024-12-01 10:25:30'),
                                                                                      (2, 4, 3, 900, '2024-12-01 10:25:30'),
                                                                                      (3, 2, 2, 1000, '2024-12-01 10:30:30');
