import { useState, useEffect } from "react";
import axios from "axios";
import BookCard from "../components/BookCard";

export default function HomePage() {
    const [query, setQuery] = useState("");
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const BASE_URL = "http://localhost:8080";

    // Fetch books at first homepage load
    const fetchDefaultBooks = async () => {
        setLoading(true);
        try {
            const res = await axios.get(`${BASE_URL}/books/default`, {
                params: { page, size: 16 }
            });
            setBooks(res.data.content);
            setTotalPages(res.data.totalPages);
        } catch (err) {
            console.error(err);
        }
        setLoading(false);
    };

    // Fetch books (used by search + pagination)
    const fetchBooks = async (searchQuery) => {
        setLoading(true);
        try {
            const res = await axios.get(`${BASE_URL}/books/search`, {
                params: { q: searchQuery, page, size: 16 },
            });
            setBooks(res.data.content);
            setTotalPages(res.data.totalPages);
        } catch (err) {
            console.error("Error fetching books:", err);
        }
        setLoading(false);
    };

    // Run default search on first load
    useEffect(() => {
        fetchDefaultBooks(); // default keyword
    }, []);

    // Fetch new page when page changes
    useEffect(() => {
        if (query !== "") fetchBooks(query);
        else fetchDefaultBooks();
    }, [page]);

    const handleSearch = () => {
        setPage(0);           // reset page on new search
        fetchBooks(query);
    };

    return (
        <div style={{ padding: "40px", fontFamily: "Arial" }}>
            <h1>Book Search</h1>

            {/* Search Bar */}
            <div style={{ marginBottom: "20px", display: "flex", gap: "10px" }}>
                <input
                    type="text"
                    placeholder="Search for books..."
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    style={{
                        padding: "12px",
                        width: "300px",
                        borderRadius: "6px",
                        border: "1px solid #ccc",
                    }}
                />
                <button
                    onClick={handleSearch}
                    style={{
                        padding: "12px 20px",
                        background: "#0078ff",
                        color: "white",
                        border: "none",
                        borderRadius: "6px",
                        cursor: "pointer",
                    }}
                >
                    Search
                </button>
            </div>

            {/* Loading Indicator */}
            {loading && <p>Loading...</p>}

            {/* Book Grid */}
            <div
                style={{
                    display: "flex",
                    flexWrap: "wrap",
                    gap: "20px",
                    marginTop: "20px",
                }}
            >
                {books.map((book) => (
                    <BookCard key={book.id} book={book} />
                ))}
            </div>

            {/* Pagination */}
            {totalPages > 1 && (
                <div style={{ marginTop: "20px" }}>
                    <button disabled={page === 0} onClick={() => setPage(page - 1)}>
                        Previous
                    </button>

                    <span style={{ margin: "0 15px" }}>
            Page {page + 1} of {totalPages}
          </span>

                    <button
                        disabled={page + 1 === totalPages}
                        onClick={() => setPage(page + 1)}
                    >
                        Next
                    </button>
                </div>
            )}
        </div>
    );
}