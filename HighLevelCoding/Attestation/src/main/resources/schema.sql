CREATE TABLE IF NOT EXISTS books (
    id             SERIAL       PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    author         VARCHAR(255) NOT NULL,
    isbn           VARCHAR(20)  UNIQUE,
    published_year SMALLINT,
    total_copies   SMALLINT     NOT NULL DEFAULT 1,
    CONSTRAINT chk_copies CHECK (total_copies >= 1)
);

CREATE INDEX IF NOT EXISTS idx_books_title  ON books (title);
CREATE INDEX IF NOT EXISTS idx_books_author ON books (author);

CREATE TABLE IF NOT EXISTS readers (
    id            SERIAL       PRIMARY KEY,
    full_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone         VARCHAR(30),
    registered_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_readers_full_name ON readers (full_name);

CREATE TABLE IF NOT EXISTS loans (
    id          SERIAL    PRIMARY KEY,
    book_id     INT       NOT NULL REFERENCES books(id),
    reader_id   INT       NOT NULL REFERENCES readers(id),
    borrowed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    due_date    DATE,
    returned_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_loans_book_id     ON loans (book_id);
CREATE INDEX IF NOT EXISTS idx_loans_reader_id   ON loans (reader_id);
CREATE INDEX IF NOT EXISTS idx_loans_returned_at ON loans (returned_at);
