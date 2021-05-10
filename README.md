# shak

## Task
The project is to build a simple REST service with two endpoints
1. One to take in a string, and returns a SHA256. We should persist the message and the SHA256 in a database.
2. One endpoint to take SHA256, and to return a message (if the SHA256 exists in the database)

## Running
### App
The app can be started via `./gradlew app:run`. Alternatively, you can `./gradlew app:build` 
and start it from distribution via `./app/build/distributions/app-0.0.1.zip/app-0.0.1/bin/app`

### Database
For demo purposes the app starts with in-memory H2 database with database console available at 
[http://localhost:8082](http://localhost:8082).
Once opened, just change JDBC URL to `jdbc:h2:mem:main` and connect.

Alternatively, you can start the app in file mode, by setting a `DB_FILE` environment variable with the path to database.
This is especially useful to showcase automated database schema management and migrations.
e.g.: `DB_FILE=path/to/db ./gradlew app:run"` or `DB_FILE=path/to/db ./app/build/distributions/app-0.0.1.zip/app-0.0.1/bin/app`