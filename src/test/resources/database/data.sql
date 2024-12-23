INSERT INTO user_table(id, email, name, phone) VALUES(1, 'user@domain.com', 'username', '01012345678');
INSERT INTO user_table(id, email, name, phone) VALUES(2, 'another-user@domain.com', 'another', '01023456789');

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
