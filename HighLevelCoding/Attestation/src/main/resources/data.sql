INSERT INTO books (title, author, isbn, published_year, total_copies) VALUES
    ('Мастер и Маргарита',         'Михаил Булгаков',    '978-5-17-090640-0', 1967, 3),
    ('Война и мир',                'Лев Толстой',        '978-5-04-116618-9', 1869, 2),
    ('Преступление и наказание',   'Фёдор Достоевский',  '978-5-17-119748-5', 1866, 3),
    ('Отцы и дети',                'Иван Тургенев',      '978-5-08-006938-1', 1862, 2),
    ('Евгений Онегин',             'Александр Пушкин',   '978-5-17-099080-5', 1833, 4),
    ('Герой нашего времени',       'Михаил Лермонтов',   '978-5-17-099082-9', 1840, 2),
    ('Мёртвые души',               'Николай Гоголь',     '978-5-17-099083-6', 1842, 2),
    ('Анна Каренина',              'Лев Толстой',        '978-5-04-116619-6', 1878, 2),
    ('Братья Карамазовы',          'Фёдор Достоевский',  '978-5-17-119749-2', 1880, 2),
    ('Идиот',                      'Фёдор Достоевский',  '978-5-17-119750-8', 1869, 1),
    ('Чистый код',                 'Роберт Мартин',      '978-5-496-00099-0', 2008, 2),
    ('Паттерны проектирования',    'Банда четырёх',      '978-5-496-00095-2', 1994, 2),
    ('Совершенный код',            'Стив Макконнелл',    '978-5-7502-0064-1', 2004, 1),
    ('Алгоритмы. Построение и анализ', 'Кормен и др.',   '978-5-8459-2016-4', 2009, 2),
    ('Автостопом по Галактике',    'Дуглас Адамс',       '978-5-17-099084-3', 1979, 3);

INSERT INTO readers (full_name, email, phone) VALUES
    ('Иванов Иван Иванович',       'ivanov@mail.ru',     '+7-900-111-22-33'),
    ('Петрова Мария Сергеевна',    'petrova@gmail.com',  '+7-900-222-33-44'),
    ('Сидоров Алексей Николаевич', 'sidorov@yandex.ru',  '+7-900-333-44-55'),
    ('Козлова Анна Дмитриевна',    'kozlova@mail.ru',    NULL),
    ('Новиков Дмитрий Павлович',   'novikov@gmail.com',  '+7-900-555-66-77'),
    ('Морозова Елена Викторовна',  'morozova@yandex.ru', '+7-900-666-77-88'),
    ('Волков Сергей Андреевич',    'volkov@mail.ru',     NULL),
    ('Лебедева Ольга Ивановна',    'lebedeva@gmail.com', '+7-900-888-99-00');

INSERT INTO loans (book_id, reader_id, borrowed_at, due_date, returned_at) VALUES
    (1, 1, NOW() - INTERVAL '30 days', NOW() - INTERVAL '16 days', NOW() - INTERVAL '18 days'),
    (2, 2, NOW() - INTERVAL '25 days', NOW() - INTERVAL '11 days', NOW() - INTERVAL '12 days'),
    (3, 3, NOW() - INTERVAL '20 days', NOW() - INTERVAL '6 days',  NOW() - INTERVAL '7 days'),
    (1, 4, NOW() - INTERVAL '15 days', NOW() - INTERVAL '1 day',   NOW() - INTERVAL '2 days'),
    (5, 1, NOW() - INTERVAL '40 days', NOW() - INTERVAL '26 days', NOW() - INTERVAL '28 days'),
    (11,2, NOW() - INTERVAL '10 days', NOW() - INTERVAL '3 days',  NOW() - INTERVAL '1 day'),
    (1, 2, NOW() - INTERVAL '5 days',  NOW() + INTERVAL '9 days',  NULL),
    (3, 5, NOW() - INTERVAL '3 days',  NOW() + INTERVAL '11 days', NULL),
    (11,6, NOW() - INTERVAL '7 days',  NOW() + INTERVAL '7 days',  NULL),
    (12,7, NOW() - INTERVAL '2 days',  NOW() + INTERVAL '12 days', NULL),
    (2, 8, NOW() - INTERVAL '18 days', NOW() - INTERVAL '4 days',  NULL);
