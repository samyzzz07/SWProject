# How to Run the Tournament Management System

## Quick Answer: Where is the Main App?

**The main application entry point is:**

```
src/main/java/com/example/tournament/TournamentManagementApp.java
```

This file contains the `main()` method that starts the entire application.

---

## Running the Application

### Easiest Method: Using Maven

```bash
# Navigate to project directory
cd /path/to/SWProject

# Run the application
mvn javafx:run
```

### Alternative Methods

#### Using Maven Exec Plugin:
```bash
mvn exec:java -Dexec.mainClass="com.example.tournament.TournamentManagementApp"
```

#### From Your IDE:
1. Open `TournamentManagementApp.java`
2. Find the `main()` method (line 51)
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
# Download all dependencies
mvn clean install

# Run the application
mvn javafx:run
```

That's it! The application will start with an H2 in-memory database automatically.

---

## Files You Created

1. **Main Application**: `TournamentManagementApp.java` - Entry point
2. **UI Layout**: `participant_team_view.fxml` - JavaFX interface
3. **Database Config**: `persistence.xml` - JPA configuration
4. **Database Utility**: `JPAUtil.java` - Database connection manager
5. **Build File**: `pom.xml` - Maven configuration with all dependencies

---

## Need Help?

See the full README.md for complete documentation.
