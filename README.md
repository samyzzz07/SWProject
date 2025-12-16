# Tournament Management System

A JavaFX-based application for managing tournaments, teams, and scheduling with comprehensive role-based access control.

## New Features

### Role-Based Access Control System

The application now includes a complete login and authentication system with four distinct user roles:

1. **Player** - Tournament participants
2. **NonManager** - Team captains and general users
3. **GameManager** - Primary tournament administrators
4. **Referee** - Officials responsible for scoring

Each role has specific permissions aligned with the Sports Tournament Management use cases.

## Quick Start

### First Time Setup

1. **Build the Application**
   ```bash
   mvn clean package
   ```

2. **Run the Application**
   ```bash
   java -jar target/tournament-management-app.jar
   ```
   
   Or use Maven:
   ```bash
   mvn javafx:run
   ```

3. **Register a User**
   - On the login screen, use the right panel to register
   - Choose your role from the dropdown
   - After registration, login using the left panel

4. **Access Role-Specific Features**
   - Each role has a customized interface with appropriate functionalities

## User Roles and Capabilities

### Player
- View tournaments and sports
- Register teams
- Update team information

### NonManager
- View tournaments and schedules
- Register teams
- Add games to the schedule

### GameManager
- Full tournament management access
- Add and schedule games
- Collect registration fees
- Manage referees
- Post scores and record results
- Finalize tournaments

### Referee
- View all matches
- Post final scores for games

## Documentation

- **[USER_GUIDE.md](USER_GUIDE.md)** - Comprehensive user guide for all roles
- **[QUICK_START.md](QUICK_START.md)** - Quick start guide for developers

## Main Application Entry Point

**The main application class you need to run is:**

```
src/main/java/com/example/tournament/TournamentManagementApp.java
```

This class contains the `main` method that serves as the entry point for the entire application.

## How to Run the Application

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven (for building the project)
- The project includes all necessary dependencies via Maven:
  - JavaFX (UI framework)
  - JPA/Hibernate (database persistence)
  - Database drivers (H2, MySQL, PostgreSQL, SQLite)

### Running from Command Line

#### Option 1: Using the Executable JAR (Recommended)

Build and run the application with all dependencies bundled:

```bash
# Navigate to the project root directory
cd /path/to/SWProject

# Build the executable JAR with all dependencies
mvn clean package

# Run the application
java -jar target/tournament-management-app.jar
```

The build creates a standalone JAR file (`tournament-management-app.jar`) that includes all JavaFX runtime components and dependencies, so you don't need to configure module paths or classpath manually.

#### Option 2: Using Maven

If you have a `pom.xml` configured with JavaFX dependencies:

```bash
# Compile
mvn clean compile

# Run using JavaFX Maven Plugin
mvn javafx:run
```

Or run the main class directly:

```bash
mvn exec:java -Dexec.mainClass="com.example.tournament.TournamentManagementApp"
```

#### Option 3: Using Gradle (if configured)

If you have a `build.gradle` configured with JavaFX plugin:

```bash
# Run the application
gradle run
```

### Running from IDE

The project uses Maven as its build tool and includes all JavaFX dependencies automatically.

#### IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Import as a Maven project (if not automatically detected)
3. Navigate to `src/main/java/com/example/tournament/TournamentManagementApp.java`
4. Right-click on the file and select "Run 'TournamentManagementApp.main()'"

#### Eclipse

1. Open the project in Eclipse
2. Import as a Maven project
3. Navigate to `src/main/java/com/example/tournament/TournamentManagementApp.java`
4. Right-click on the file and select "Run As → Java Application"

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

This error has been resolved! The project now includes Maven Shade Plugin that creates a standalone executable JAR with all JavaFX runtime components bundled.

**Solutions:**

1. **Using the executable JAR (Recommended):**
   ```bash
   mvn clean package
   java -jar target/tournament-management-app.jar
   ```

2. **Using Maven JavaFX Plugin:**
   ```bash
   mvn javafx:run
   ```

3. **Using Maven Exec Plugin:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.tournament.TournamentManagementApp"
   ```

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

### Building the Project

To build the standalone executable JAR:

```bash
mvn clean package
```

This creates two JAR files in the `target` directory:
- `tournament-management-1.0-SNAPSHOT.jar` - Standard JAR (requires classpath configuration)
- `tournament-management-app.jar` - **Executable JAR with all dependencies** (recommended)

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
