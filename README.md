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

- Java Development Kit (JDK) 11 or higher
- JavaFX SDK (if not included with your JDK)
- Maven or Gradle (optional, for dependency management)

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
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── tournament/
│       │               ├── TournamentManagementApp.java  ← MAIN APPLICATION
│       │               ├── model/                        (Domain models)
│       │               ├── service/                      (Business logic)
│       │               └── ui/                          (UI controllers)
│       └── resources/
│           └── fxml/
│               └── participant_team_view.fxml           (UI layout)
└── README.md
```

## Main Class Details

**Fully Qualified Class Name:** `com.example.tournament.TournamentManagementApp`

**Package:** `com.example.tournament`

**File Location:** `src/main/java/com/example/tournament/TournamentManagementApp.java`

**Main Method Signature:** `public static void main(String[] args)`

## Troubleshooting

### "JavaFX runtime components are missing"

If you get this error, you need to add JavaFX to your module path:

```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml \
  -cp bin com.example.tournament.TournamentManagementApp
```

### "Error loading FXML file"

Make sure the resources directory is in your classpath. The FXML file should be at:
```
src/main/resources/fxml/participant_team_view.fxml
```

### Build Tool Configuration

For Maven, add this to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.2</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17.0.2</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-maven-plugin</artifactId>
            <version>0.0.8</version>
            <configuration>
                <mainClass>com.example.tournament.TournamentManagementApp</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Additional Documentation

For more details about the project architecture and design, refer to the PDF documents in the root directory:
- ClassDiagram.pdf
- UseCase.pdf
- StateDiagram.pdf
- CRC.pdf

## License

This is a software engineering project for educational purposes.
