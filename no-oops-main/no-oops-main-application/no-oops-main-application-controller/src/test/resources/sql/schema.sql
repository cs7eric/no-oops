-- Create the books table with full audit fields for testing
CREATE TABLE IF NOT EXISTS books (
    -- Primary Key
    id BIGSERIAL PRIMARY KEY,

    -- Business Fields
    name VARCHAR(255) NOT NULL,
    cover TEXT,
    author VARCHAR(100),
    isbn VARCHAR(20) UNIQUE,
    description TEXT,

    -- Audit Fields (User tracking)
    -- In big factories, this is usually the User ID (BIGINT) or username (VARCHAR)
    created_by VARCHAR(64) NOT NULL,
    updated_by VARCHAR(64),

    -- Audit Fields (Time tracking)
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Soft Delete Field
    -- Using SMALLINT (0/1) or BOOLEAN.
    -- 0: active, 1: deleted (easier for indexing in some DBs, but BOOLEAN is fine for PG)
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Version for optimistic locking
    version BIGINT
);

-- Indexing for performance and soft delete
-- We usually add 'is_deleted' to indexes to speed up common queries
CREATE INDEX IF NOT EXISTS idx_books_name_active ON books(name) WHERE is_deleted = FALSE;
CREATE INDEX IF NOT EXISTS idx_books_author ON books(author);