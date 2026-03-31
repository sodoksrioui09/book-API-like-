export default function BookCard({ book }) {
    return (
        <div
            style={{
                width: "200px",
                border: "1px solid #ddd",
                borderRadius: "8px",
                padding: "10px",
                background: "#fff",
                boxShadow: "0 2px 4px rgba(0,0,0,0.1)"
            }}
        >
            {book.coverUrl ? (
                <img
                    src={book.coverUrl.replace("-L.jpg", "-M.jpg")}
                    alt={book.title}
                    style={{
                        width: "100%",
                        height: "280px",
                        objectFit: "cover",
                        borderRadius: "6px",
                        marginBottom: "10px",
                    }}
                />
            ) : (
                <div
                    style={{
                        width: "100%",
                        height: "280px",
                        background: "#f0f0f0",
                        borderRadius: "6px",
                        marginBottom: "10px",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        color: "#666",
                        fontSize: "14px",
                    }}
                >
                    No Image
                </div>
            )}

            <h3 style={{ fontSize: "16px", margin: "4px 0" }}>{book.title}</h3>
            <p style={{ margin: "0", color: "#444" }}>
                <strong>{book.author}</strong>
            </p>
            <p style={{ margin: "0", color: "#777" }}>{book.publish_year}</p>
        </div>
    );
}