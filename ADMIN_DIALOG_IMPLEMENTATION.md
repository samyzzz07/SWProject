# Administrator Dashboard Dialog Functionality

## Overview

This implementation adds comprehensive dialog windows for all administrator dashboard functions, replacing the simple inline forms with fully-featured dialogs that integrate with the database to manage tournaments.

## Implemented Dialogs

### 1. Create Tournament Dialog

**File**: `create_tournament_dialog.fxml` / `CreateTournamentDialogController.java`

**Functionality**:
- **Tournament Name**: Text field for entering tournament name
- **Sport Selection**: Dropdown with options (Football, Basketball, Cricket, Tennis, Volleyball, Baseball)
- **Tournament Type**: Dropdown with options (League, Knockout, Round Robin)
- **Start Date**: Date picker for tournament start date
- **End Date**: Date picker for tournament end date
- **Maximum Teams**: Spinner to set max number of teams (2-64)
- **Registration Deadline**: Date picker for team registration deadline
- **Description**: Optional text area for tournament description
- **Database Integration**: Creates tournament in database using `TournamentService`
- **Validation**: Comprehensive form validation before submission
  - All required fields must be filled
  - End date must be after start date
  - Registration deadline must be before start date
- **Tournament Types**: Automatically creates correct tournament type:
  - Knockout → `KnockoutTournament` entity
  - League/Round Robin → `LeagueTournament` entity
- **Success Confirmation**: Shows dialog with tournament details after creation

**Database Operations**:
- Creates or retrieves `Sport` entity
- Creates new tournament entity with all details
- Persists to database with initial `SCHEDULED` status

### 2. Define Tournament Rules Dialog

**File**: `define_tournament_rules_dialog.fxml` / `DefineTournamentRulesDialogController.java`

**Functionality**:
- **Tournament Selection**: Dropdown populated from database with all existing tournaments
- **Tournament Information Display**: Shows name, type, and sport (read-only)
- **General Rules**:
  - Players per Team: Spinner (1-50, default 11)
  - Substitutes Allowed: Spinner (0-20, default 3)
  - Match Duration: Spinner (30-180 minutes, default 90)
- **League-Specific Rules** (shown only for League/Round Robin tournaments):
  - Points for Win: Spinner (0-10, default 3)
  - Points for Draw: Spinner (0-10, default 1)
  - Points for Loss: Spinner (0-10, default 0)
- **Knockout-Specific Rules** (shown only for Knockout tournaments):
  - Number of Rounds: Spinner (1-10, default 4)
  - Extra Time: Spinner (0-60 minutes, default 30)
  - Penalty Shootout: Checkbox
- **Additional Rules**: Text area for custom rules and notes
- **Dynamic UI**: Shows/hides rule sections based on tournament type
- **Database Integration**: Updates tournament rules in database
- **Load Existing Rules**: Loads and displays current rule values

**Database Operations**:
- Fetches all tournaments from database
- Loads selected tournament details
- Updates tournament rules via `TournamentService.updateTournament()`
- Persists rule changes to database

### 3. View Tournament Report Dialog

**File**: `view_tournament_report_dialog.fxml` / `ViewTournamentReportDialogController.java`

**Functionality**:
- **Tournament Selection**: Dropdown populated from database
- **Tournament Overview**: Displays:
  - Name, Sport, Type, Status
  - Start Date, End Date
- **Statistics Section**:
  - Total Teams
  - Total Matches
  - Completed Matches
  - Upcoming Matches
  - Total Goals/Points
  - Average Score per match
  - Completion Percentage
- **Team Standings Table**: (Especially for League tournaments)
  - Position, Team Name, Matches Played
  - Wins, Draws, Losses, Points
  - Automatically calculated from match results
  - Sorted by points (descending)
- **Recent Matches Table**:
  - Date, Team 1, Score, Team 2, Status
  - Shows all matches with their current status
- **Export Report**: Button for future report export functionality
- **Print**: Button for future print functionality
- **Real-time Calculation**: All statistics calculated dynamically from database data

**Database Operations**:
- Fetches all tournaments from database
- Loads complete tournament data including teams and matches
- Calculates standings from match results
- Displays real-time statistics

### 4. End Tournament Dialog

**File**: `end_tournament_dialog.fxml` / `EndTournamentDialogController.java`

**Functionality**:
- **Tournament Selection**: Dropdown showing only active (non-completed) tournaments
- **Tournament Details Display**:
  - Name, Sport, Type, Current Status
  - Total Teams, Total Matches, Completed Matches
- **Warning Section**: Prominent warning about permanent action:
  - Cannot be undone
  - Marks tournament as COMPLETED
  - Finalizes standings and results
  - Prevents further modifications
  - Archives tournament data
- **Final Notes**: Optional text area for closing comments
- **Confirmation Dialog**: Double confirmation before finalizing
- **Database Integration**: Updates tournament status to COMPLETED
- **Auto-refresh**: Updates tournament lists after ending

**Database Operations**:
- Fetches active tournaments (status != COMPLETED)
- Finalizes tournament via `TournamentService.finalizeTournament()`
- Updates tournament status to `COMPLETED`
- Persists changes to database

## Service Layer Updates

### TournamentService Enhancements

**New Methods**:

1. **`createTournament(Tournament tournament)`**
   - Creates new tournament in database
   - Sets initial status to SCHEDULED
   - Returns success/failure boolean

2. **`updateTournament(Tournament tournament)`**
   - Updates existing tournament details
   - Used for saving rule changes
   - Returns success/failure boolean

3. **`finalizeTournament(Long tournamentId)`** (existing, documented)
   - Marks tournament as COMPLETED
   - Finalizes standings and results

4. **`viewAllTournaments()`** (existing, documented)
   - Retrieves all tournaments from database
   - Ordered by start date (descending)

### SportService Enhancements

**New Methods**:

1. **`getSportByName(String name)`**
   - Retrieves sport by name
   - Returns null if not found

2. **`createSport(Sport sport)`**
   - Creates new sport in database
   - Returns created sport with ID

## Administrator Controller Updates

### Updated Methods

1. **`handleCreateTournament()`**
   - Opens Create Tournament Dialog
   - Refreshes tournament lists after creation

2. **`handleDefineTournamentRules()`**
   - Opens Define Tournament Rules Dialog

3. **`handleViewTournamentReport()`**
   - Opens View Tournament Report Dialog

4. **`handleEndTournament()`**
   - Opens End Tournament Dialog
   - Refreshes tournament lists after ending

5. **`initialize()`**
   - Loads tournaments from database on startup
   - Removed hardcoded placeholder data

### New Helper Methods

1. **`refreshTournamentLists()`**
   - Reloads tournaments from database
   - Called after creating or ending tournaments

## Administrator View Updates

### FXML Changes

- Removed inline text fields and combo boxes from main view
- Updated all function cards to show descriptive text
- Buttons now open respective dialogs
- Cleaner, more professional interface

## Database Integration

All dialogs are fully integrated with the database:

- **Tournaments are persisted** to the database when created
- **Tournament rules are saved** to the database when defined
- **Tournament data is loaded** from the database for reports
- **Tournament status is updated** in the database when ended
- **Dropdown lists are populated** from database tournaments
- **Changes are immediately persisted** and reflected across the system

## User Experience Features

1. **Form Validation**: All inputs validated before submission
2. **Confirmation Dialogs**: Important actions require confirmation
3. **Success Messages**: Clear feedback on successful operations
4. **Error Handling**: Graceful error handling with user-friendly messages
5. **Dynamic UI**: Interface adapts based on tournament type
6. **Database-driven**: All data from/to database, not hardcoded
7. **Real-time Updates**: Lists refresh after creating/ending tournaments

## Example Usage Flow

### Creating a Tournament

1. Administrator clicks "Create Tournament" button
2. Dialog opens with empty form
3. Administrator fills in:
   - Name: "Spring Championship 2025"
   - Sport: "Football"
   - Type: "League"
   - Start Date: April 1, 2025
   - End Date: May 30, 2025
   - Max Teams: 16
   - Registration Deadline: March 25, 2025
4. Clicks "Create Tournament"
5. System validates input and saves to database
6. Success dialog shows tournament details
7. Tournament now appears in all dropdown lists

### Defining Tournament Rules

1. Administrator clicks "Define Rules" button
2. Dialog opens with tournament dropdown
3. Administrator selects "Spring Championship 2025"
4. System loads tournament details and shows League-specific rules
5. Administrator sets:
   - Players per Team: 11
   - Substitutes: 5
   - Match Duration: 90 minutes
   - Points for Win: 3
   - Points for Draw: 1
   - Points for Loss: 0
6. Clicks "Save Rules"
7. Rules are persisted to database
8. Confirmation dialog shows all configured rules

### Viewing Tournament Report

1. Administrator clicks "View Report" button
2. Dialog opens with tournament dropdown
3. Administrator selects tournament
4. System loads and displays:
   - Overview information
   - Statistics (teams, matches, goals, completion %)
   - Team standings table with calculated points
   - Recent matches with scores
5. Administrator can export or print (future functionality)

### Ending a Tournament

1. Administrator clicks "End Tournament" button
2. Dialog shows only active tournaments
3. Administrator selects tournament to end
4. System displays tournament details and warning
5. Administrator confirms action
6. Tournament status updated to COMPLETED in database
7. Tournament removed from active lists

## Technical Implementation Details

### Design Patterns

- **MVC Pattern**: Clear separation of view (FXML), controller (Java), and model (entities)
- **Service Layer**: Business logic in service classes
- **Repository Pattern**: Database access through JPA/Hibernate
- **Validation Pattern**: Form validation before database operations

### Database Schema

The implementation works with existing entities:
- `Tournament` (abstract base class)
- `LeagueTournament` (extends Tournament)
- `KnockoutTournament` (extends Tournament)
- `Sport`
- `Team`
- `Match`
- `TournamentStatus` (enum)

### Transaction Management

- All database operations wrapped in transactions
- Proper rollback on errors
- EntityManager lifecycle properly managed

## Future Enhancements

Potential improvements for future development:

1. **Export Reports**: Implement PDF/CSV export functionality
2. **Print Support**: Add print preview and printing
3. **Advanced Filtering**: Filter tournaments by status, date, sport
4. **Bulk Operations**: Create multiple tournaments at once
5. **Tournament Templates**: Save and reuse tournament configurations
6. **Validation Rules**: Custom validation rules per sport
7. **Notifications**: Email notifications when tournaments are created/ended
8. **Audit Log**: Track all changes to tournaments
9. **Advanced Statistics**: More detailed analytics and charts
10. **Import/Export**: Import tournament data from files

## Testing Recommendations

When testing the functionality, verify:

1. **Create Tournament**:
   - All fields accept valid input
   - Validation prevents invalid dates
   - Tournament appears in database
   - Tournament appears in other dialog dropdowns

2. **Define Rules**:
   - Correct rule sections shown for tournament type
   - Rules save to database
   - Rules load correctly when reopening dialog

3. **View Report**:
   - Statistics calculated correctly
   - Standings sorted properly
   - Match data displayed accurately

4. **End Tournament**:
   - Only active tournaments shown
   - Status changes to COMPLETED
   - Tournament removed from end tournament dropdown
   - Still appears in reports dropdown

## Conclusion

This implementation provides a complete, database-integrated solution for tournament management in the administrator dashboard. All dialogs follow consistent design patterns, include proper validation, and ensure data integrity through database transactions.
