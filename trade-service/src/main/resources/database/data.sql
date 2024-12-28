INSERT INTO trade(id, store_id, trade_device_id, trade_amount, created_at, is_completed) VALUES
                                                                                             (1, 1, 1, 3600, '2024-12-01 10:20:30', 'N'),
                                                                                             (2, 1, 2, 3900, '2024-12-01 10:25:30', 'N');

INSERT INTO order_table(id, trade_id, order_amount, created_at) VALUES
                                                                    (1, 1, 2600, '2024-12-01 10:20:30'),
                                                                    (2, 2, 3900, '2024-12-01 10:25:30'),
                                                                    (3, 1, 1000, '2024-12-01 10:30:30');

INSERT INTO order_detail(id, order_id, product_name, product_category, product_price, count, total_amount) VALUES
                                                                                                (1, 1, '상품 이름 2', 'SUB_MENU', 500, 4, 2000),
                                                                                                (2, 1, '상품 이름 4', 'DRINK', 300, 2, 600),
                                                                                                (3, 2, '상품 이름 2', 'SUB_MENU', 500, 6, 3000),
                                                                                                (4, 2, '상품 이름 4', 'DRINK', 300, 3, 900),
                                                                                                (5, 3, '상품 이름 2', 'SUB_MENU', 500, 2, 1000);