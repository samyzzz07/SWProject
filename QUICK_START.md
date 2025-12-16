# How to Run the Tournament Management System

## Quick Answer: Where is the Main App?

**The main application entry point is:**

```
src/main/java/com/example/tournament/TournamentManagementApp.java
```

This file contains the `main()` method that starts the entire application.

---

## Running the Application

### Easiest Method: Using the Executable JAR (Recommended)

```bash
# Navigate to project directory
cd /path/to/SWProject

# Build the standalone JAR with all dependencies
mvn clean package

# Run the application
java -jar target/tournament-management-app.jar
```

The executable JAR (`tournament-management-app.jar`) includes all JavaFX runtime components and dependencies, so you don't need to worry about classpath or module path configuration.

### Alternative Method 1: Using Maven JavaFX Plugin

```bash
# Navigate to project directory
cd /path/to/SWProject

# Run the application
mvn javafx:run
```

### Alternative Method 2: Using Maven Exec Plugin

```bash
mvn exec:java -Dexec.mainClass="com.example.tournament.TournamentManagementApp"
```

### From Your IDE:
1. Open `TournamentManagementApp.java`
2. Find the `main()` method
3. Click "Run" or right-click and select "Run"

---

## What This Application Does

- **Team Registration**: Register teams with contact information
- **Time Slot Management**: Add preferred time slots for teams
- **Sport Selection**: Choose sports for tournaments
- **Database Persistence**: Stores data using JPA/Hibernate

---

## First Time Setup

```bash
# Download all dependencies and build the project
mvn clean package

# Run the application
java -jar target/tournament-management-app.jar
```

That's it! The application will start with an H2 in-memory database automatically.

---

## Troubleshooting

### "JavaFX runtime components are missing"

This error has been resolved! The project now creates a standalone JAR with all JavaFX components included.

Just run:
```bash
mvn clean package
java -jar target/tournament-management-app.jar
```

---

## Files You Created

1. **Main Application**: `TournamentManagementApp.java` - Entry point
2. **Launcher**: `Launcher.java` - JAR launcher (fixes JavaFX runtime error)
3. **UI Layout**: `participant_team_view.fxml` - JavaFX interface
4. **Database Config**: `persistence.xml` - JPA configuration
5. **Database Utility**: `JPAUtil.java` - Database connection manager
6. **Build File**: `pom.xml` - Maven configuration with all dependencies

---

## Need Help?

See the full README.md for complete documentation.
