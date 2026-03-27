
-- Add full-text search vector column
ALTER TABLE books
    ADD COLUMN search_vector tsvector;

-- Populate initial search vector
UPDATE books
SET search_vector = to_tsvector('english', title || ' ' || author);

-- Create GIN index for fast full-text search
CREATE INDEX idx_books_search
    ON books USING GIN (search_vector);

-- Create trigger to keep search_vector updated on INSERT/UPDATE
CREATE TRIGGER books_search_vector_trigger
    BEFORE INSERT OR UPDATE ON books
                         FOR EACH ROW EXECUTE FUNCTION
                         tsvector_update_trigger(search_vector, 'pg_catalog.english', title, author);
