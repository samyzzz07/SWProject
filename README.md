# Tournament Management System

A JavaFX-based application for managing tournaments, teams, and scheduling.

## Main Application Entry Point

**The main application class you need to run is:**

```
src/main/java/com/example/tournament/TournamentManagementApp.java
```

This class contains the `main` method that serves as the entry point for the entire application.

## How to Run the Application

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven (for dependency management and building)
- The project includes all necessary dependencies via Maven:
  - JavaFX (UI framework)
  - JPA/Hibernate (database persistence)
  - Database drivers (H2, MySQL, PostgreSQL, SQLite)

### Running from Command Line

#### Option 1: Using Java directly (without build tool)

If you have JavaFX installed separately, run:

```bash
# Navigate to the project root directory
cd /path/to/SWProject

# Compile the application
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml \
  -d bin src/main/java/com/example/tournament/*.java \
  src/main/java/com/example/tournament/model/*.java \
  src/main/java/com/example/tournament/service/*.java \
  src/main/java/com/example/tournament/ui/*.java

# Run the application
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml \
  -cp bin com.example.tournament.TournamentManagementApp
```

#### Option 2: Using Maven

If you have a `pom.xml` configured with JavaFX dependencies:

```bash
# Compile
mvn clean compile

# Run
mvn javafx:run
```

Or run the main class directly:

```bash
mvn exec:java -Dexec.mainClass="com.example.tournament.TournamentManagementApp"
```

#### Option 3: Using Gradle

If you have a `build.gradle` configured with JavaFX plugin:

```bash
# Run the application
gradle run
```

### Running from IDE

#### IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Navigate to `src/main/java/com/example/tournament/TournamentManagementApp.java`
3. Right-click on the file and select "Run 'TournamentManagementApp.main()'"

**Note:** You may need to configure JavaFX in your IDE:
- Go to File → Project Structure → Libraries
- Add the JavaFX SDK library

#### Eclipse

1. Open the project in Eclipse
2. Navigate to `src/main/java/com/example/tournament/TournamentManagementApp.java`
3. Right-click on the file and select "Run As → Java Application"

#### VS Code

1. Open the project in VS Code
2. Install the "Extension Pack for Java" if not already installed
3. Navigate to `TournamentManagementApp.java`
4. Click the "Run" button that appears above the `main` method

## Application Features

The Tournament Management System provides the following features:

### Team Management
- Register new teams with name and contact information
- Select sport type for each team
- View list of all registered teams

### Time Slot Management
- Add preferred time slots for teams
- Specify date, start time, and end time
- View all time slots for selected teams

## Project Structure

```
SWProject/
├── pom.xml                                          (Maven build configuration)
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── tournament/
│       │               ├── TournamentManagementApp.java  ← MAIN APPLICATION
│       │               ├── model/                        (JPA entities)
│       │               ├── service/                      (Business logic)
│       │               ├── ui/                          (JavaFX controllers)
│       │               └── util/                        (JPA utilities)
│       └── resources/
│           ├── META-INF/
│           │   └── persistence.xml                  (JPA configuration)
│           └── fxml/
│               └── participant_team_view.fxml       (UI layout)
└── README.md
```

## Database Configuration

The project includes JPA/Hibernate implementation with support for multiple database drivers:

### Supported Databases

1. **H2 (Default)** - In-memory database for development
   - Persistence Unit: `TournamentPU-H2`
   - No setup required, works out of the box
   
2. **MySQL** - Production-ready relational database
   - Persistence Unit: `TournamentPU-MySQL`
   - Requires MySQL server running on localhost:3306
   
3. **PostgreSQL** - Advanced open-source database
   - Persistence Unit: `TournamentPU-PostgreSQL`
   - Requires PostgreSQL server running on localhost:5432
   
4. **SQLite** - Lightweight file-based database
   - Persistence Unit: `TournamentPU-SQLite`
   - Creates a local file `tournament.db`

### JPA Configuration

The JPA configuration is located in `src/main/resources/META-INF/persistence.xml` and includes all entity mappings and database connection settings.

To use a different database, modify the persistence unit in `JPAUtil.java` or pass it as a parameter when initializing.

### Database Drivers Included

All necessary JDBC drivers are included in the Maven dependencies:
- H2 Database Engine
- MySQL Connector/J
- PostgreSQL JDBC Driver
- SQLite JDBC Driver
- HikariCP (Connection pooling)
- Hibernate ORM (JPA implementation)

## Main Class Details

**Fully Qualified Class Name:** `com.example.tournament.TournamentManagementApp`

**Package:** `com.example.tournament`

**File Location:** `src/main/java/com/example/tournament/TournamentManagementApp.java`

**Main Method Signature:** `public static void main(String[] args)`

## Troubleshooting

### "JavaFX runtime components are missing"

If you get this error when not using Maven, add JavaFX to your module path:

```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml \
  -cp bin com.example.tournament.TournamentManagementApp
```

**Solution:** Use Maven to run the application (`mvn javafx:run`) which handles dependencies automatically.

### "Error loading FXML file"

Make sure the resources directory is in your classpath. The FXML file should be at:
```
src/main/resources/fxml/participant_team_view.fxml
```

**Solution:** Use Maven build which correctly includes resources in the classpath.

### Database Connection Issues

If you encounter database connection errors:

1. **For H2 (default):** No setup needed, it should work automatically
2. **For MySQL:** Ensure MySQL server is running and update credentials in `persistence.xml`
3. **For PostgreSQL:** Ensure PostgreSQL server is running and update credentials in `persistence.xml`
4. **For SQLite:** Ensure write permissions in the application directory

### Maven Dependencies

The project includes a complete `pom.xml` with all required dependencies:

- **JavaFX** (UI framework)
  - javafx-controls
  - javafx-fxml
  
- **JPA/Hibernate** (Database persistence)
  - jakarta.persistence-api
  - hibernate-core
  - hibernate-validator
  
- **Database Drivers**
  - H2 Database
  - MySQL Connector/J
  - PostgreSQL JDBC
  - SQLite JDBC
  
- **Additional Libraries**
  - HikariCP (Connection pooling)
  - SLF4J & Logback (Logging)

Simply run `mvn clean install` to download all dependencies automatically.

## Additional Documentation

For more details about the project architecture and design, refer to the PDF documents in the root directory:
- ClassDiagram.pdf
- UseCase.pdf
- StateDiagram.pdf
- CRC.pdf

## License

This is a software engineering project for educational purposes.
