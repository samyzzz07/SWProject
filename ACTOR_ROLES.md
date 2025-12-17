# Actor Roles and Use Cases

This document describes the actor roles in the Sports Tournament Management System and their associated use cases.

## System Overview

**System Name:** SportsTournamentManagement

**Description:** A system to manage the registration, scheduling, scoring, and finalization of events within a sports tournament.

---

## Actors

### 1. Administrator
**Description:** The top-level actor responsible for high-level operations.

**Permissions:**
- View_Tournament - View the overall tournament structure and status
- Update_Team - Update team information
- View_Sport - View details about specific sports in the tournament
- Register_Team - Add new teams to the tournament
  - Includes: Collect_Fees (sub-process for handling registration fees)
  - Includes: Update_Team_Info (process to modify existing team details)

---

### 2. Team_Manager
**Description:** Actor responsible for handling team-specific tasks.

**Permissions:**
- View_Tournament - View the overall tournament structure and status
- Register_Team - Add new teams to the tournament
- Add_Games - Create and schedule new game entries
- View_Schedule - Check dates, times, and locations of games

---

### 3. Game_Coordinator (GameCoordinator)
**Description:** Actor managing game creation, scheduling, and tournament operations.

**Permissions:**
- Add_Games - Create and schedule new game entries
- Collect_Fees - Handle registration fees
- View_Schedule - Check dates, times, and locations of games
- Final_Tournament - Conclude the tournament
- Post_Scores - Submit final scores for a game
- Record_Match_Results - Input game outcomes
  - Includes: Update_Game (update the game entity with the result)
  - Includes: Update_Score (update the official score entity)
- View_Games - Review all scheduled games
- View_Tournament - View the overall tournament structure and status

---

### 4. Tournament_Organizer (TournamentOrganizer)
**Description:** Actor responsible for submitting final scores and results.

**Permissions:**
- Post_Scores - Submit final scores for a game

---

## Use Cases

### Primary Functions

1. **View_Tournament**
   - Allows users to see the overall tournament structure and status

2. **View_Sport**
   - Allows users to see details about specific sports played in the tournament

3. **View_Schedule**
   - Allows users to check the dates, times, and locations of games

4. **Post_Scores**
   - Allows officials to submit final scores for a game

5. **Final_Tournament**
   - The final process to conclude the tournament

### Registration and Team Management

6. **Register_Team**
   - Process for adding a new team to the tournament
   - Includes: Collect_Fees
   - Includes: Update_Team_Info

7. **Collect_Fees**
   - Sub-process for handling registration fees
   - Dependency: include (parent: Register_Team)

8. **Update_Team**
   - General function to update team information

9. **Update_Team_Info**
   - Process to modify existing team details
   - Dependency: include (parent: Register_Team)

### Game Management and Scoring

10. **Add_Games**
    - Process for creating and scheduling new game entries

11. **View_Games**
    - Allows users to review all scheduled games

12. **Record_Match_Results**
    - The main process for inputting game outcomes
    - Includes: Update_Game
    - Includes: Update_Score

13. **Update_Game**
    - Sub-process to update the game entity with the result
    - Dependency: include (parent: Record_Match_Results)

14. **Update_Score**
    - Sub-process to update the official score entity
    - Dependency: include (parent: Record_Match_Results)

---

## Implementation Mapping

The actor roles are implemented in the system as follows:

### Database/Code Mapping

| Actor Role | UserRole Enum | Java Class | Discriminator Value |
|------------|---------------|------------|---------------------|
| Administrator | ADMINISTRATOR | Administrator | ADMINISTRATOR |
| Team_Manager | TEAM_MANAGER | TeamManager | TEAM_MANAGER |
| Game_Coordinator | GAME_COORDINATOR | GameCoordinator | GAME_COORDINATOR |
| Tournament_Organizer | TOURNAMENT_ORGANIZER | TournamentOrganizer | TOURNAMENT_ORGANIZER |

---

## Database Schema

The SQL schema is defined in `/src/main/resources/sql/schema.sql` and includes:

- **users** table - Stores all user types with Single Table Inheritance
- **tournaments** table - Tournament information
- **teams** table - Team registration and management
- **matches** table - Game scheduling and results
- Other supporting tables for venues, time slots, sports, etc.

The database supports login/registration integration through JPA/Hibernate with multiple backend options (H2, MySQL, PostgreSQL, SQLite).

---

## Login and Registration

Users can register through the login screen by:
1. Selecting their role from the dropdown
2. Providing username, password, and email
3. The system validates uniqueness and creates the appropriate user entity
4. After registration, users can login with their credentials
5. Upon successful login, users are directed to their role-specific interface

All login credentials and user data are persisted in the SQL database, ensuring data integrity and seamless authentication across sessions.
