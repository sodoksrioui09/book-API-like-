export default function BookCard({ book }) {
    return (
        <div
            style={{
                background: "white",
                borderRadius: "16px",
                border: "1px solid #e5e7eb",
                overflow: "hidden",
                boxShadow: "0 4px 12px rgba(0,0,0,0.04)",
                transition: "transform 0.2s ease, box-shadow 0.2s ease",
                cursor: "pointer",
            }}
            onMouseEnter={(e) => {
                e.currentTarget.style.transform = "translateY(-4px)";
                e.currentTarget.style.boxShadow =
                    "0 12px 24px rgba(0,0,0,0.08)";
            }}
            onMouseLeave={(e) => {
                e.currentTarget.style.transform = "translateY(0)";
                e.currentTarget.style.boxShadow =
                    "0 4px 12px rgba(0,0,0,0.04)";
            }}
        >
            {/* Cover image */}
            {book.coverUrl ? (
                <img
                    src={book.coverUrl.replace("-L.jpg", "-M.jpg")}
                    alt={book.title}
                    loading="lazy"
                    style={{
                        width: "100%",
                        height: "280px",
                        objectFit: "cover",
                        display: "block",
                    }}
                />
            ) : (
                <div
                    style={{
                        width: "100%",
                        height: "280px",
                        background: "#f3f4f6",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        color: "#9ca3af",
                        fontSize: "14px",
                    }}
                >
                    No cover available
                </div>
            )}

            {/* Card content */}
            <div style={{ padding: "14px" }}>
                <h3
                    style={{
                        fontSize: "16px",
                        fontWeight: "600",
                        margin: "0 0 6px",
                        lineHeight: "1.3",
                        color: "#111827",
                    }}
                >
                    {book.title}
                </h3>

                <p
                    style={{
                        margin: "0",
                        fontSize: "14px",
                        fontWeight: "500",
                        color: "#374151",
                    }}
                >
                    {book.author}
                </p>

                {book.publish_year && (
                    <p
                        style={{
                            margin: "4px 0 0",
                            fontSize: "13px",
                            color: "#6b7280",
                        }}
                    >
                        {book.publish_year}
                    </p>
                )}
            </div>
        </div>
    );
}