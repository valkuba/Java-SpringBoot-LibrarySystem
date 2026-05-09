INSERT INTO lendings
(member_id, book_copy_id, loan_date, due_date, return_date, status)
VALUES
    (1, 1, '2026-01-05', '2026-01-19', '2026-01-18', 'RETURNED'),
    (2, 3, '2026-01-10', '2026-01-24', NULL, 'LOANED'),
    (1, 2, '2026-02-01', '2026-02-15', '2026-02-20', 'RETURNED'),
    (3, 1, '2026-02-10', '2026-02-24', NULL, 'LOANED'),
    (2, 1, '2026-03-01', '2026-03-15', NULL, 'LOANED'),
    (4, 1, '2026-03-10', '2026-03-24', NULL, 'LOANED'),
    (5, 2, '2026-03-20', '2026-04-03', NULL, 'LOANED');


INSERT INTO reservations
(member_id, book_id, reservation_date, when_available_date, status)
VALUES
    (1, 2, '2026-01-20', '2026-02-01', 'ACTIVE'),
    (3, 1, '2026-02-05', '2026-02-20', 'ACTIVE'),
    (2, 3, '2026-03-01', NULL, 'PENDING');


INSERT INTO fines
(member_id, loan_id, date, amount, status)
VALUES
    (1, 3, '2026-02-20', 25.0, 'UNPAID'),
    (2, 1, '2026-01-18', 0.0, 'PAID');
