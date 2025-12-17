# Tournament Organizer Dialog Functionality

## Overview

This implementation adds comprehensive dialog windows for all tournament organizer functions. Each button in the Tournament Organizer dashboard now opens a dedicated dialog window with appropriate functionality.

## Implemented Dialogs

### 1. View Registered Teams
- **File**: `view_teams_dialog.fxml` / `ViewTeamsDialogController.java`
- **Functionality**: 
  - Displays a table of all registered teams
  - Filter teams by status (All, Approved, Pending, Rejected)
  - View detailed team information
  - Sample data includes team names, managers, contact info, and status

### 2. Approve Team
- **File**: `approve_team_dialog.fxml` / `ApproveTeamDialogController.java`
- **Functionality**:
  - Dropdown to select pending teams
  - Displays team information (name, manager, contact, players, registration date)
  - Approve or reject team with optional notes
  - Confirmation dialogs for both approve and reject actions

### 3. Publish Schedule
- **File**: `publish_schedule_dialog.fxml` / `PublishScheduleDialogController.java`
- **Functionality**:
  - Select tournament from dropdown
  - Preview schedule in table format (matches, date/time, venue, status)
  - View schedule information (total matches, start/end dates, published status)
  - Options to notify teams and make schedule publicly accessible
  - Publish with confirmation

### 4. Collect Fees
- **File**: `collect_fees_dialog.fxml` / `CollectFeesDialogController.java`
- **Functionality**:
  - Summary dashboard showing total teams, paid count, pending count, and total collected
  - Table view of all teams with fee amount, status, and payment date
  - Record payment for pending teams
  - Export report functionality
  - Real-time summary updates

### 5. Find Sponsor
- **File**: `find_sponsor_dialog.fxml` / `FindSponsorDialogController.java`
- **Functionality**:
  - List of current sponsors
  - Add new sponsor with company details (name, contact person, email, phone, contribution, type)
  - Sponsor types: Platinum, Gold, Silver, Bronze, Media Partner, Equipment Sponsor
  - Remove existing sponsors
  - Additional notes field

### 6. Check Schedule
- **File**: `check_schedule_dialog.fxml` / `CheckScheduleDialogController.java`
- **Functionality**:
  - Select tournament to validate
  - Validation results showing:
    - All matches scheduled status
    - Venue conflicts check
    - Referee assignments status
    - Time conflicts check
  - Detailed schedule table with match, date/time, venue, referee, and issues columns
  - Issues and warnings log
  - Export validation report

### 7. Assign Referees
- **File**: `assign_referees_dialog.fxml` / `AssignRefereesDialogController.java`
- **Functionality**:
  - Dropdown to select match
  - Display match details (venue, duration)
  - List of available referees with certification levels
  - Assign referee to selected match
  - Clear existing referee assignment
  - Shows currently assigned referee

### 8. Postpone Match
- **File**: `postpone_match_dialog.fxml` / `PostponeMatchDialogController.java`
- **Functionality**:
  - Select match to postpone from dropdown
  - Display current schedule (date/time, venue)
  - Set new schedule:
    - Date picker for new date
    - Time field for new time
    - Venue selection dropdown
  - Reason text area for postponement
  - Confirmation before postponing

### 9. Request Menu
- **File**: `request_menu_dialog.fxml` / `RequestMenuDialogController.java`
- **Functionality**:
  - Filter requests by type (All, Team Registration, Schedule Change, Venue Change, Postponement)
  - Filter by status (All, Pending, Approved, Rejected)
  - Table view showing request ID, type, requester, date, and status
  - Detailed request information display
  - Response/notes field for organizer
  - Approve or reject pending requests

## Sample Data

All dialogs include sample data for demonstration purposes:

- **Teams**: Alpha, Beta, Gamma, Delta, Epsilon, Zeta with various statuses
- **Matches**: 5 sample matches with different teams, dates, and venues
- **Referees**: 6 referees with different certification levels
- **Sponsors**: 3 existing sponsors with different tiers
- **Tournaments**: Winter Championship 2025, Spring League 2025, Summer Cup 2025
- **Requests**: 5 sample requests of different types and statuses
- **Venues**: 4 different stadiums/arenas

## Technical Details

### Controller Implementation

All dialog controllers follow a consistent pattern:
1. Initialize with sample data in `initialize()` method
2. Handle user interactions with `@FXML` annotated methods
3. Validate inputs before performing actions
4. Show confirmation dialogs for critical actions
5. Update UI elements based on user selections
6. Use JavaFX properties (SimpleStringProperty, SimpleIntegerProperty) for table bindings

### Dialog Opening

The `TournamentOrganizerController` has been updated with an `openDialog()` method that:
- Loads the FXML file
- Creates a new Stage
- Sets the title and dimensions
- Shows the dialog as a modal window

### UI Consistency

All dialogs follow a consistent design:
- Title at the top with separator
- Main content area with appropriate controls
- Action buttons at the bottom (aligned right)
- Consistent color scheme matching the main dashboard
- Responsive layouts using VBox, HBox, and GridPane

## Testing

To test the dialogs:

1. Build the application: `mvn clean package`
2. Run the application: `java -jar target/tournament-management-app.jar`
3. Login as a Tournament Organizer
4. Click each button on the dashboard to open the respective dialog
5. Interact with the controls to verify functionality

## Future Enhancements

Potential improvements:
- Connect to actual database instead of sample data
- Add data persistence for changes made in dialogs
- Implement email notifications when publishing schedules or approving teams
- Add PDF export functionality for reports
- Implement search functionality in larger tables
- Add pagination for tables with many records
- Implement drag-and-drop for referee assignments
- Add calendar view for schedule management
