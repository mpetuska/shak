# shak

## Task
The project is to build a simple REST service with two endpoints
1. One to take in a string, and returns a SHA256. We should persist the message and the SHA256 in a database.
2. One endpoint to take SHA256, and to return a message (if the SHA256 exists in the database)

## Notes
http://localhost:8080/h2-console
jdbcUrl = "jdbc:h2:mem:main"