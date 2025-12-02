# Movie Search API Documentation 🏴‍☠️

Ahoy matey! This be the comprehensive API documentation for the Movie Search and Filtering service. Chart yer course through these endpoints to find the movie treasures ye seek!

## Base URL
```
http://localhost:8080
```

## Authentication
No authentication required - this treasure chest be open to all sailors!

## Content Types
- **Request**: `application/x-www-form-urlencoded` (for form submissions) or query parameters
- **Response**: `application/json` (for API endpoints) or `text/html` (for web pages)

---

## 🔍 Search Endpoints

### 1. Search Movies (REST API)

**Endpoint:** `GET /movies/search`

**Description:** Search for movies using various criteria with pirate-themed JSON responses.

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `name` | String | No | Movie name to search for (partial matching, case-insensitive) |
| `id` | Long | No | Movie ID to search for (exact matching, must be > 0) |
| `genre` | String | No | Movie genre to search for (partial matching, case-insensitive) |

#### Response Format
```json
{
  "success": boolean,
  "message": "Pirate-themed response message",
  "movies": [
    {
      "id": number,
      "movieName": "string",
      "director": "string", 
      "year": number,
      "genre": "string",
      "description": "string",
      "duration": number,
      "imdbRating": number
    }
  ],
  "count": number
}
```

#### Examples

**Search by name:**
```bash
curl "http://localhost:8080/movies/search?name=prison"
```

**Response:**
```json
{
  "success": true,
  "message": "Yo ho ho! Found one fine movie treasure for ye!",
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
      "duration": 142,
      "imdbRating": 5.0
    }
  ],
  "count": 1
}
```

**Search by genre:**
```bash
curl "http://localhost:8080/movies/search?genre=Crime"
```

**Search by ID:**
```bash
curl "http://localhost:8080/movies/search?id=1"
```

**Combined search:**
```bash
curl "http://localhost:8080/movies/search?name=family&genre=crime"
```

**Get all movies:**
```bash
curl "http://localhost:8080/movies/search"
```

#### Error Responses

**Invalid ID (400 Bad Request):**
```json
{
  "success": false,
  "message": "Arrr! That ID be as useless as a compass that points south! Provide a valid movie ID, ye scallywag!",
  "movies": [],
  "count": 0
}
```

**No results found (200 OK):**
```json
{
  "success": true,
  "message": "Shiver me timbers! No movies found matching yer search criteria. The treasure chest be empty for this quest, matey!",
  "movies": [],
  "count": 0
}
```

**Server error (500 Internal Server Error):**
```json
{
  "success": false,
  "message": "Blimey! Something went wrong while searching the treasure chest. Try again later, ye landlubber!",
  "movies": [],
  "count": 0
}
```

---

### 2. Get All Genres

**Endpoint:** `GET /movies/genres`

**Description:** Get all available movie genres from the treasure chest.

#### Response Format
```json
{
  "success": boolean,
  "message": "Pirate-themed response message",
  "genres": ["string"],
  "count": number
}
```

#### Example

**Request:**
```bash
curl "http://localhost:8080/movies/genres"
```

**Response:**
```json
{
  "success": true,
  "message": "Here be all the movie genres in our treasure chest, matey!",
  "genres": [
    "Action/Crime",
    "Action/Sci-Fi", 
    "Adventure/Fantasy",
    "Adventure/Sci-Fi",
    "Crime/Drama",
    "Drama",
    "Drama/History",
    "Drama/Romance",
    "Drama/Thriller"
  ],
  "count": 9
}
```

---

## 🌐 Web Interface Endpoints

### 3. Movie List with Search Form

**Endpoint:** `GET /movies`

**Description:** Returns an HTML page with movie listings and an integrated search form.

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `name` | String | No | Filter by movie name |
| `id` | Long | No | Filter by movie ID |
| `genre` | String | No | Filter by genre |

#### Examples

**View all movies:**
```
http://localhost:8080/movies
```

**Search via URL parameters:**
```
http://localhost:8080/movies?name=prison
http://localhost:8080/movies?genre=Drama
http://localhost:8080/movies?name=family&genre=crime
```

#### Features
- Interactive search form with pirate theming
- Real-time genre dropdown population
- Responsive design for mobile and desktop
- Pirate-themed success/error messages
- Clear search functionality

---

### 4. Movie Details

**Endpoint:** `GET /movies/{id}/details`

**Description:** Returns detailed information about a specific movie.

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Movie ID (1-12) |

#### Example
```
http://localhost:8080/movies/1/details
```

---

## 📊 Available Movies

The treasure chest currently contains 12 movies:

| ID | Movie Name | Genre | Year | Rating |
|----|------------|-------|------|--------|
| 1 | The Prison Escape | Drama | 1994 | 5.0 |
| 2 | The Family Boss | Crime/Drama | 1972 | 5.0 |
| 3 | The Masked Hero | Action/Crime | 2008 | 5.0 |
| 4 | Urban Stories | Crime/Drama | 1994 | 4.5 |
| 5 | Life Journey | Drama/Romance | 1994 | 4.0 |
| 6 | Dream Heist | Action/Sci-Fi | 2010 | 4.5 |
| 7 | The Virtual World | Action/Sci-Fi | 1999 | 4.5 |
| 8 | The Wise Guys | Crime/Drama | 1990 | 4.5 |
| 9 | The Quest for the Ring | Adventure/Fantasy | 2001 | 4.5 |
| 10 | Space Wars: The Beginning | Adventure/Sci-Fi | 1977 | 4.0 |
| 11 | The Factory Owner | Drama/History | 1993 | 4.5 |
| 12 | Underground Club | Drama/Thriller | 1999 | 4.5 |

---

## 🏴‍☠️ Pirate Language Reference

The API uses authentic pirate terminology in all responses:

### Greetings & Exclamations
- **Ahoy!** - Hello/Greeting
- **Arrr!** - Emphasis or agreement
- **Yo ho ho!** - Excitement or celebration
- **Shiver me timbers!** - Surprise or amazement
- **Blimey!** - Surprise or concern
- **Batten down the hatches!** - Get ready/prepare

### Terms of Address
- **Matey** - Friend/buddy
- **Me hearty** - Good friend
- **Ye/Yer** - You/Your
- **Scallywag** - Playful insult
- **Landlubber** - Someone unfamiliar with the sea

### Nautical Terms
- **Treasure chest** - Database/movie collection
- **Chart a course** - Plan or navigate
- **Compass that points south** - Something useless
- **Seven seas** - Everywhere

---

## 🧪 Testing the API

### Using cURL

**Test search functionality:**
```bash
# Basic search
curl -X GET "http://localhost:8080/movies/search?name=prison"

# Search with multiple parameters
curl -X GET "http://localhost:8080/movies/search?name=family&genre=crime"

# Get all genres
curl -X GET "http://localhost:8080/movies/genres"

# Test error handling
curl -X GET "http://localhost:8080/movies/search?id=-1"
```

### Using Browser

1. Navigate to `http://localhost:8080/movies`
2. Use the search form to test different combinations
3. Observe pirate-themed messages and responses
4. Test edge cases like empty searches and invalid IDs

### Response Time
- Typical response time: < 100ms
- Search operations are performed in-memory for optimal performance

---

## 🔧 Error Handling

The API provides comprehensive error handling with appropriate HTTP status codes:

| Status Code | Scenario | Pirate Message |
|-------------|----------|----------------|
| 200 OK | Successful search with results | "Yo ho ho! Found X movie treasures for ye!" |
| 200 OK | Successful search with no results | "Shiver me timbers! No movies found..." |
| 200 OK | No search criteria provided | "Ahoy! No search criteria provided..." |
| 400 Bad Request | Invalid ID (≤ 0) | "Arrr! That ID be as useless as a compass..." |
| 500 Internal Server Error | Server error | "Blimey! Something went wrong..." |

---

## 📈 Performance Considerations

- **In-Memory Search**: All searches are performed on in-memory data structures for optimal performance
- **Case-Insensitive Matching**: String searches use lowercase conversion for consistent results
- **Partial Matching**: Name and genre searches support partial matching for better user experience
- **Caching**: Movie data is loaded once at startup and cached in memory

---

## 🚀 Future Enhancements

Potential improvements for the API:
- Pagination for large result sets
- Advanced filtering (year range, rating range)
- Sorting options (by name, year, rating)
- Full-text search across all movie fields
- Movie recommendations based on search history
- Rate limiting and authentication
- Database persistence instead of JSON files

---

*Arrr! May this documentation guide ye safely through the treacherous waters of movie searching, ye savvy developer!* 🏴‍☠️⚓