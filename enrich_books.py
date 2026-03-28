import requests
import psycopg2
import time
import urllib.parse

# -------------------------------
#  DB CONNECTION SETTINGS
# -------------------------------
DB_HOST = "localhost"           # or your Docker host IP
DB_NAME = "mydb"
DB_USER = "myuser"
DB_PASS = "mypassword"

# -------------------------------
#  CONNECT TO POSTGRESQL
# -------------------------------
conn = psycopg2.connect(
    host=DB_HOST,
    database=DB_NAME,
    user=DB_USER,
    password=DB_PASS
)

cursor = conn.cursor()

# -------------------------------
#  FETCH ALL BOOKS (title + author)
# -------------------------------
cursor.execute("SELECT id, title, author FROM books")
books = cursor.fetchall()

print(f"📚 Found {len(books)} books. Starting metadata enrichment...\n")


# -------------------------------
#  MAIN LOOP: ENRICH EACH BOOK
# -------------------------------
for (book_id, title, author) in books:

    if not title or not author:
        continue  # skip rows with missing data

    # Encode title + author for URL
    title_q = urllib.parse.quote(title)
    author_q = urllib.parse.quote(author)

    # OpenLibrary API Search Endpoint
    url = f"https://openlibrary.org/search.json?title={title_q}&author={author_q}"

    try:
        response = requests.get(url, timeout=5)
    except:
        print(f"❗ Network error for book {book_id}, skipping.")
        continue

    if response.status_code != 200:
        print(f"❗ API error for {book_id}, status: {response.status_code}")
        continue

    data = response.json()

    if "docs" not in data or len(data["docs"]) == 0:
        continue  # no metadata found

    doc = data["docs"][0]  # best match

    # -------------------------------
    #  EXTRACT METADATA
    # -------------------------------
    cover_url = None
    if "cover_i" in doc:
        cover_url = f"https://covers.openlibrary.org/b/id/{doc['cover_i']}-L.jpg"

    publish_year = doc.get("first_publish_year")

    subjects = None
    if "subject" in doc:
        subjects = "; ".join(doc["subject"][:10])  # limit to 10 subjects

    description = None
    if "first_sentence" in doc:
        val = doc["first_sentence"]
        if isinstance(val, dict):
            description = val.get("value")
        elif isinstance(val, str):
            description = val
    elif "subtitle" in doc:
        description = doc["subtitle"]

    isbn = None
    if "isbn" in doc and isinstance(doc["isbn"], list) and len(doc["isbn"]) > 0:
        isbn = doc["isbn"][0]

    # -------------------------------
    #  UPDATE DATABASE
    # -------------------------------
    cursor.execute("""
        UPDATE books SET
            cover_url = %s,
            description = %s,
            subjects = %s,
            publish_year = %s,
            isbn = %s
        WHERE id = %s
    """, (cover_url, description, subjects, publish_year, isbn, book_id))

    conn.commit()

    print(f"✔ Updated {book_id}: {title[:40]}...")

    # avoid API rate limits
    time.sleep(0.15)

print("\n🎉 Metadata enrichment complete!")
cursor.close()
conn.close()