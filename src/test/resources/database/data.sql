INSERT INTO user_table(id, email, name, phone) VALUES
                                                   (1, 'user@domain.com', 'username', '01012345678'),
                                                   (2, 'another-user@domain.com', 'another', '01023456789');

INSERT INTO store(id, owner_user_id, name, pos_grade) VALUES
                                                          (1, 1, '상점 이름', 'BRONZE'),
                                                          (2, 2, '어느 상점 이름', 'BRONZE');

INSERT INTO trade_device(id, store_id) VALUES
                                           (1, 1),
                                           (2, 1),
                                           (3, 1),
                                           (4, 1),
                                           (5, 1),
                                           (6, 1),
                                           (7, 1),
                                           (8, 1),
                                           (9, 1),
                                           (10, 1),
                                           (11, 2),
                                           (12, 2),
                                           (13, 2),
                                           (14, 2),
                                           (15, 2),
                                           (16, 2),
                                           (17, 2),
                                           (18, 2),
                                           (19, 2),
                                           (20, 2);

INSERT INTO product(id, store_id, name, category, price, stock, base_stock, is_deleted) VALUES
                                                                                            (1, 1, '상품 이름 1', 'MAIN_MENU', 1000, 20, 20, 'N'),
                                                                                            (2, 1, '상품 이름 2', 'SUB_MENU', 500, 20, 40, 'N'),
                                                                                            (3, 1, '상품 이름 3', 'SUB_MENU', 700, 20, 20, 'Y'),
                                                                                            (4, 1, '상품 이름 4', 'DRINK', 300, 30, 50, 'N'),
                                                                                            (5, 2, '어느 상품 이름', 'MAIN_MENU', 3000, 10, 10, 'N');

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
