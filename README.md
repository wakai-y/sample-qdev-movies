# Movie Service - Spring Boot Demo Application 🏴‍☠️

Ahoy matey! Welcome to the Movie Service treasure chest - a swashbuckling movie catalog web application built with Spring Boot, now featuring pirate-themed search functionality that'll make ye feel like ye're sailing the seven seas!

## Features

- **Movie Catalog**: Browse 12 classic movies with detailed information
- **🔍 Pirate Search**: Search for movie treasures by name, ID, or genre with pirate-themed responses
- **⚓ REST API**: JSON endpoints for programmatic access to the movie treasure chest
- **🏴‍☠️ Pirate Language**: All search responses include authentic pirate language and terminology
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds and smooth animations

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Log4j 2.20.0**
- **JUnit 5.8.2**
- **Thymeleaf** for templating
- **Spring Web** for REST endpoints

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List with Search**: http://localhost:8080/movies
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **Search API**: http://localhost:8080/movies/search
- **Genres API**: http://localhost:8080/movies/genres

## 🏴‍☠️ New Search Features

### Web Interface Search
Navigate to `http://localhost:8080/movies` and use the pirate-themed search form:
- **Movie Name**: Search by partial movie name (case-insensitive)
- **Movie ID**: Search by specific movie ID
- **Genre**: Filter by genre using dropdown
- **Combined Search**: Use multiple criteria together

### REST API Endpoints

#### Search Movies
```
GET /movies/search
```
Search for movies using various criteria with pirate-themed JSON responses.

**Query Parameters:**
- `name` (optional): Movie name to search for (partial matching, case-insensitive)
- `id` (optional): Movie ID to search for (exact matching)
- `genre` (optional): Movie genre to search for (partial matching, case-insensitive)

**Examples:**
```bash
# Search by name
curl "http://localhost:8080/movies/search?name=prison"

# Search by genre
curl "http://localhost:8080/movies/search?genre=Drama"

# Search by ID
curl "http://localhost:8080/movies/search?id=1"

# Combined search
curl "http://localhost:8080/movies/search?name=family&genre=crime"

# Get all movies (no parameters)
curl "http://localhost:8080/movies/search"
```

**Response Format:**
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
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0
    }
  ],
  "count": 1
}
```

#### Get All Genres
```
GET /movies/genres
```
Get all available movie genres from the treasure chest.

**Example:**
```bash
curl "http://localhost:8080/movies/genres"
```

**Response:**
```json
{
  "success": true,
  "message": "Here be all the movie genres in our treasure chest, matey!",
  "genres": ["Action/Crime", "Adventure/Fantasy", "Crime/Drama", "Drama"],
  "count": 4
}
```

### Error Handling

The API includes comprehensive error handling with pirate-themed messages:

- **Invalid ID**: "Arrr! That ID be as useless as a compass that points south!"
- **No Results**: "Shiver me timbers! No movies found matching yer search criteria."
- **Server Error**: "Blimey! Something went wrong while searching the treasure chest."

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java        # Main Spring Boot application
│   │       │   ├── MoviesController.java         # Web controller for movie pages
│   │       │   ├── MovieSearchController.java    # REST controller for search API
│   │       │   ├── MovieService.java             # Business logic with search methods
│   │       │   ├── Movie.java                    # Movie data model
│   │       │   ├── Review.java                   # Review data model
│   │       │   └── ReviewService.java            # Review business logic
│   │       └── utils/
│   │           ├── MovieIconUtils.java           # Movie icon utilities
│   │           └── MovieUtils.java               # Movie validation utilities
│   └── resources/
│       ├── templates/
│       │   ├── movies.html                       # Movie list with search form
│       │   └── movie-details.html                # Movie details page
│       ├── static/css/                           # Stylesheets
│       ├── application.yml                       # Application configuration
│       ├── movies.json                           # Movie data
│       ├── mock-reviews.json                     # Mock review data
│       └── log4j2.xml                            # Logging configuration
└── test/                                         # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceTest.java             # Service layer tests
            ├── MovieSearchControllerTest.java    # REST API tests
            └── MoviesControllerTest.java         # Web controller tests
```

## API Endpoints

### Web Pages

#### Get All Movies (with Search)
```
GET /movies
```
Returns an HTML page displaying movies with an integrated search form.

**Query Parameters (optional):**
- `name`: Filter by movie name
- `id`: Filter by movie ID
- `genre`: Filter by genre

**Examples:**
```
http://localhost:8080/movies
http://localhost:8080/movies?name=prison
http://localhost:8080/movies?genre=Drama
http://localhost:8080/movies?name=family&genre=crime
```

#### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

### REST API

#### Search Movies
```
GET /movies/search
```
JSON endpoint for searching movies with pirate-themed responses.

#### Get All Genres
```
GET /movies/genres
```
JSON endpoint returning all available movie genres.

## Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest
mvn test -Dtest=MovieSearchControllerTest
mvn test -Dtest=MoviesControllerTest
```

The test suite includes:
- **MovieServiceTest**: Tests for search functionality and business logic
- **MovieSearchControllerTest**: Tests for REST API endpoints
- **MoviesControllerTest**: Tests for web controller with search parameters

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

Check the logs for pirate-themed debug messages:
```bash
tail -f logs/application.log | grep -i "ahoy\|arrr\|matey"
```

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movies to the treasure chest
- Enhance the pirate language and themes
- Add new search features like advanced filtering
- Improve the responsive design
- Add more comprehensive error handling
- Extend the REST API with additional endpoints

## Pirate Language Guide 🏴‍☠️

The application uses authentic pirate terminology throughout:
- **Ahoy!** - Greeting
- **Arrr!** - Emphasis or agreement
- **Matey/Me hearty** - Friendly address
- **Ye/Yer** - You/Your
- **Treasure chest** - Database/movie collection
- **Scallywag/Landlubber** - Playful insults
- **Shiver me timbers!** - Surprise or excitement
- **Blimey!** - Surprise or concern
- **Batten down the hatches!** - Get ready/prepare

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*Arrr! May fair winds fill yer sails as ye explore this movie treasure chest, ye savvy developer!* 🏴‍☠️⚓
