# Sports Tournament Management System - User Guide

## Overview
The Sports Tournament Management System now includes a complete login and role-based access control system with the following actors:

### Actors and Their Capabilities

#### 1. Player
A participant in the tournament with the following capabilities:
- **ViewTournament**: View all tournaments and their details
- **ViewSport**: View all sports in the system
- **RegisterTeam**: Register a new team in the tournament
- **UpdateTeam**: Update their team's information

#### 2. NonManager
A user who is not a Game Manager (possibly a team captain or general user) with capabilities:
- **ViewTournament**: View all tournaments
- **RegisterTeam**: Register teams
- **AddGames**: Add new games to the schedule
- **ViewSchedule**: View the tournament schedule

#### 3. GameManager
The primary administrator responsible for organizing and managing the tournament with full access:
- **AddGames**: Create and schedule new games
- **CollectFees**: Handle registration fee collection
- **ViewSchedule**: View the complete tournament schedule
- **ManageReferee**: Add, update, or remove referees
- **FinalTournament**: Finalize and close tournaments
- **PostScores**: Post game scores
- **RecordMatchResults**: Record complete match results (includes UpdateGame and UpdateScore)

#### 4. Referee
An official responsible for posting game scores with capabilities:
- **PostScores**: Submit final scores for games

## How to Use the System

### 1. Starting the Application

Run the application using one of these methods:

```bash
# Using the executable JAR
mvn clean package
java -jar target/tournament-management-app.jar

# Using Maven
mvn javafx:run

# Using Maven exec
mvn exec:java -Dexec.mainClass="com.example.tournament.TournamentManagementApp"
```

### 2. Login/Registration Screen

When the application starts, you'll see a login screen with two panels:

**Left Panel - Login:**
- Username field
- Password field
- Login button

**Right Panel - Registration:**
- Username field
- Password field
- Email field
- Role dropdown (select from Player, NonManager, GameManager, Referee)
- Register button

### 3. First-Time Users

1. Fill out the registration form on the right panel
2. Select your role from the dropdown menu:
   - **Player** - If you're a participant
   - **NonManager** - If you're a team captain or general user
   - **GameManager** - If you're managing the tournament
   - **Referee** - If you're officiating games
3. Click "Register"
4. After successful registration, use the login panel on the left to log in

### 4. Existing Users

1. Enter your username and password in the login panel
2. Click "Login"
3. You'll be directed to your role-specific interface

## Role-Specific Interfaces

### Player Interface
After logging in as a Player, you'll see:
- **Left Panel:**
  - List of all tournaments with "View Details" button
  - List of all sports with "View Details" button
- **Right Panel:**
  - Team registration form (Team Name, Contact Info)
  - Team update section

### NonManager Interface
After logging in as a NonManager, you'll see:
- **Left Panel:**
  - List of tournaments
  - Schedule view with match details
- **Right Panel:**
  - Team registration form
  - "Add Game" button to schedule new games

### GameManager Interface
After logging in as a GameManager, you'll see a tabbed interface:
- **Tab 1 - Schedule & Scoring:**
  - View all scheduled matches
  - Post scores for matches (Team 1 Score, Team 2 Score)
- **Tab 2 - Tournament Management:**
  - List of active tournaments
  - Finalize Tournament button
  - Add Game button
  - Collect Fees button
- **Tab 3 - Referee Management:**
  - List of all referees
  - Add/Edit/Remove Referee button

### Referee Interface
After logging in as a Referee, you'll see:
- **Left Panel:**
  - List of all matches
  - Refresh button
- **Right Panel:**
  - Match details display
  - Score posting form (Team 1 Score, Team 2 Score)
  - Post Score button

## Use Case Implementation

### Primary Functions
1. **ViewTournament**: Available to all users - see tournament list and details
2. **ViewSport**: Available to Players - see sports information
3. **ViewSchedule**: Available to NonManagers and GameManagers - see game schedules
4. **PostScores**: Available to Referees and GameManagers - submit match scores
5. **FinalTournament**: Available to GameManagers - close tournaments

### Management Functions
1. **RegisterTeam**: Available to Players and NonManagers - add new teams
   - Includes **CollectFees** (sub-process)
   - Includes **UpdateTeamInfo** (sub-process)
2. **UpdateTeam**: Available to Players - modify team details
3. **AddGames**: Available to NonManagers and GameManagers - schedule matches
4. **ViewGames**: Available to GameManagers - review all games
5. **ManageReferee**: Available to GameManagers - manage referee roster

### Scoring and Match Results
1. **RecordMatchResults**: Available to GameManagers - input game outcomes
   - Includes **UpdateGame** (sub-process)
   - Includes **UpdateScore** (sub-process)

## Technical Implementation

### Service Layer
The system implements the following services:
- **LoginService**: User authentication and registration
- **TournamentService**: Tournament viewing and finalization
- **SportService**: Sport information retrieval
- **ScheduleService**: Schedule viewing
- **ScoringService**: Score posting and match result recording
- **TeamService**: Team registration and updates
- **GameService**: Game addition and viewing
- **RefereeService**: Referee management

### Database
The system uses JPA/Hibernate with support for:
- H2 (default, in-memory)
- MySQL
- PostgreSQL
- SQLite

All user data, teams, matches, and scores are persisted to the database.

## Security Note
The current implementation uses basic authentication. In a production environment, passwords should be hashed and additional security measures should be implemented.
