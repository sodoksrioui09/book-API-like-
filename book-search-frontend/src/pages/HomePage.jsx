import axios from "axios";
import { useState, useEffect } from "react";
import BookCard from "../components/BookCard";

export default function HomePage() {
    const [query, setQuery] = useState("");
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const [totalBooks, setTotalBooks] = useState(0);
    const [enriched, setEnriched] = useState(0);
    const [raw, setRaw] = useState(0);
    const [uniqueAuthors, setUniqueAuthors] = useState(0);

    const [activeQuery, setActiveQuery] = useState("");

    const BASE_URL = "http://localhost:8080";

    const fetchDefaultBooks = async (pageNumber = page) => {
        setLoading(true);
        try {
            const res = await axios.get(`${BASE_URL}/books/default`, {
                params: { page: pageNumber, size: 15 }
            });
            setBooks(res.data.content);
            setTotalPages(res.data.totalPages);
        } catch (err) {
            console.error(err);
        }
        setLoading(false);
    };

    const fetchStats = async () => {
        try {
            const booksRes = await axios.get(`${BASE_URL}/stats/raw`);
            setTotalBooks(booksRes.data.count);
            setRaw(booksRes.data.raw);

            const enrichedRes = await axios.get(`${BASE_URL}/stats/enriched`);
            setEnriched(enrichedRes.data.enriched);

            const authorsRes = await axios.get(`${BASE_URL}/stats/authors`);
            setUniqueAuthors(authorsRes.data.count);
        } catch (err) {
            console.error("Stats error:", err);
        }
    };

    const fetchUnifiedSearch = async (searchQuery, pageNumber = 0) => {
        setLoading(true);
        try {
            const res = await axios.get(`${BASE_URL}/books/searchUnifiede`, {
                params: { q: searchQuery, page: pageNumber, size: 15 },
            });

            setBooks(res.data.content);
            setTotalPages(res.data.totalPages);
        } catch (err) {
            console.error("Unified search error", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchStats();
        fetchDefaultBooks();
    }, []);

    useEffect(() => {
        if (query.trim()) {
            fetchUnifiedSearch(query, page);
        } else {
            fetchDefaultBooks(page);
            setActiveQuery("");
        }
    }, [page]);

    const handleSearch = () => {
        setPage(0);
        setActiveQuery(query);

        if (!query) {
            fetchDefaultBooks();
        } else {
            fetchUnifiedSearch(query, 0);
        }
    };

    return (
        <div
            style={{
                minHeight: "100vh",
                background: "#f9fafb",
                padding: "40px 20px",
                fontFamily: "Inter, system-ui, sans-serif",
            }}
        >
            <div
                style={{
                    maxWidth: "1200px",
                    margin: "0 auto",

                }}
            >
                {/* HEADER */}
                <div style={{ marginBottom: "32px" }}>
                    <h1 style={{
                        fontSize: "36px",
                        fontWeight: "800",
                        marginBottom: "8px",
                        color: "#111827"
                    }}>
                        📚 Book Discovery
                    </h1>

                    <p style={{ color: "#6b7280", fontSize: "16px" }}>
                        Discover books by title, author, or topic
                    </p>
                </div>

                {/* SEARCH */}
                <div
                    style={{
                        display: "flex",
                        gap: "12px",
                        marginBottom: "32px"
                    }}
                >
                    <input
                        type="text"
                        placeholder="Search books, authors, topics..."
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        style={{
                            flex: 1,
                            padding: "16px",
                            borderRadius: "14px",
                            border: "1px solid #e5e7eb",
                            fontSize: "16px",
                            background: "white",
                        }}
                    />

                    <button
                        onClick={handleSearch}
                        style={{
                            padding: "16px 24px",
                            background: "#2563eb",
                            color: "white",
                            border: "none",
                            borderRadius: "14px",
                            fontWeight: "600",
                            cursor: "pointer",
                            boxShadow: "0 4px 12px rgba(37,99,235,0.2)"
                        }}
                    >
                        Search
                    </button>
                </div>

                {/* STATS */}
                {activeQuery === "" && (
                    <div
                        style={{
                            display: "grid",
                            gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))",
                            gap: "20px",
                            marginBottom: "32px"
                        }}
                    >
                        {[
                            { label: "Total Books", value: totalBooks },
                            { label: "Enriched Books", value: enriched },
                            { label: "Raw Books", value: raw },
                            { label: "Authors", value: uniqueAuthors },
                        ].map((stat) => (
                            <div
                                key={stat.label}
                                style={{
                                    background: "white",
                                    borderRadius: "16px",
                                    padding: "22px",
                                    border: "1px solid #e5e7eb",
                                    boxShadow: "0 10px 25px rgba(0,0,0,0.05)"
                                }}
                            >
                                <div style={{
                                    fontSize: "13px",
                                    color: "#6b7280",
                                    marginBottom: "6px"
                                }}>
                                    {stat.label}
                                </div>

                                <div style={{
                                    fontSize: "28px",
                                    fontWeight: "700"
                                }}>
                                    {stat.value}
                                </div>
                            </div>
                        ))}
                    </div>
                )}

                {/* LOADING */}
                {loading && (
                    <div style={{ textAlign: "center", marginTop: "40px" }}>
                        <p>Loading...</p>
                    </div>
                )}

                {/* GRID */}
                <div
                    style={{
                        display: "grid",
                        gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))",
                        gap: "20px",
                        marginTop: "20px",
                    }}
                >
                    {books.map((book) => (
                        <BookCard key={book.id} book={book} />
                    ))}
                </div>

                {/* PAGINATION */}
                {totalPages > 1 && (
                    <div
                        style={{
                            marginTop: "40px",
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            gap: "16px"
                        }}
                    >
                        <button
                            disabled={page === 0}
                            onClick={() => setPage(page - 1)}
                            style={{
                                padding: "10px 16px",
                                borderRadius: "10px",
                                border: "1px solid #e5e7eb",
                                background: "white",
                                cursor: "pointer"
                            }}
                        >
                            Previous
                        </button>

                        <span style={{ fontWeight: "500" }}>
                            Page {page + 1} of {totalPages}
                        </span>

                        <button
                            disabled={page + 1 === totalPages}
                            onClick={() => setPage(page + 1)}
                            style={{
                                padding: "10px 16px",
                                borderRadius: "10px",
                                border: "1px solid #e5e7eb",
                                background: "white",
                                cursor: "pointer"
                            }}
                        >
                            Next
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}