# Библиотека — консольное приложение

Консольное приложение управления библиотекой.
## Запуск

1. Создать БД: `createdb library_db`
2. Выполнить схему: `psql -d library_db -f src/main/resources/schema.sql`
3. Загрузить тестовые данные: `psql -d library_db -f src/main/resources/data.sql`
4. Настроить подключение в `src/main/resources/db.properties`
5. Запустить: `./gradlew bootRun`

## Структура пакетов

```
library/
├── domain/          — доменные объекты
│   ├── Book         — книга (id, title, author, isbn, publishedYear, totalCopies)
│   ├── Reader       — читатель (id, fullName, email, phone, registeredAt)
│   └── Loan         — выдача (id, bookId, readerId, borrowedAt, dueDate, returnedAt)
│
├── db/
│   └── DatabaseConnection   — управление JDBC-соединением, читает db.properties
│
├── repository/      — репозитории
│   ├── BookRepository       — интерфейс
│   ├── ReaderRepository     — интерфейс
│   ├── LoanRepository       — интерфейс
│   ├── AbstractJdbcRepository — Template Method: хранит DatabaseConnection
│   └── impl/
│       ├── JdbcBookRepository
│       ├── JdbcReaderRepository
│       └── JdbcLoanRepository
│
├── service/         — бизнес-логика
│   ├── BookService          — интерфейс
│   ├── ReaderService        — интерфейс
│   ├── LoanService          — интерфейс
│   ├── StatisticsService    — интерфейс
│   └── impl/
│       ├── BookServiceImpl       — валидация, поиск через стратегию
│       ├── ReaderServiceImpl     — проверка дубликата email
│       ├── LoanServiceImpl       — транзакции вручную (setAutoCommit/commit/rollback)
│       └── StatisticsServiceImpl — сложные SQL-запросы (JOIN, GROUP BY, CASE WHEN)
│
├── search/          — поиск (используется Стратегия)
│   ├── BookSearchStrategy   — @FunctionalInterface
│   ├── SearchByTitle
│   ├── SearchByAuthor
│   └── SearchByIsbn
│
├── exception/
│   ├── BookNotAvailableException  — нет доступных экземпляров
│   └── EntityNotFoundException    — объект не найден в БД
│
└── Main             — точка входа, Spring Boot DI, консольное меню
```

## База данных

Три таблицы:

### books — книги

| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PK | Идентификатор |
| title | VARCHAR(255) | Название |
| author | VARCHAR(255) | Автор |
| isbn | VARCHAR(20) UNIQUE | ISBN (опционально) |
| published_year | SMALLINT | Год издания |
| total_copies | SMALLINT | Общее количество экземпляров |

### readers — читатели

| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PK | Идентификатор |
| full_name | VARCHAR(255) | ФИО |
| email | VARCHAR(255) UNIQUE | Email |
| phone | VARCHAR(30) | Телефон (опционально) |
| registered_at | TIMESTAMP | Дата регистрации |

### loans — выдачи книг

| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PK | Идентификатор |
| book_id | INT FK → books | Книга |
| reader_id | INT FK → readers | Читатель |
| borrowed_at | TIMESTAMP | Дата выдачи |
| due_date | DATE | Срок возврата (опционально) |
| returned_at | TIMESTAMP | Дата возврата (NULL = на руках) |

### Индексы

| Индекс | Таблица | Поле |
|--------|---------|------|
| idx_books_title | books | title |
| idx_books_author | books | author |
| idx_readers_full_name | readers | full_name |
| idx_loans_book_id | loans | book_id |
| idx_loans_reader_id | loans | reader_id |
| idx_loans_returned_at | loans | returned_at |

## Функции приложения

### Книги
- Добавить книгу
- Список всех книг
- Поиск по названию
- Поиск по автору
- Поиск по ISBN

### Читатели
- Зарегистрировать читателя
- Список всех читателей

### Выдача книг
- Выдать книгу читателю (с проверкой наличия экземпляров)
- Вернуть книгу
- Список книг на руках у читателя

### Статистика
- Топ 10 популярных книг
- Все выданные книги с отметкой о просрочке

## Паттерны проектирования

- **Strategy** — `BookSearchStrategy`: поиск по названию, автору, ISBN без изменения сервиса
- **Template Method** — `AbstractJdbcRepository`: общий доступ к соединению для всех репозиториев