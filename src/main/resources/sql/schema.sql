-- Sports Tournament Management System Database Schema
-- This schema is automatically managed by Hibernate JPA
-- This file serves as documentation of the database structure

-- ============================================================================
-- USERS TABLE
-- ============================================================================
-- Main table for all user types using Single Table Inheritance
-- Stores login credentials and user information
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_type VARCHAR(50) NOT NULL,  -- Discriminator column
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,  -- Note: Store hashed passwords in production
    email VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    
    -- Administrator specific fields
    department VARCHAR(255),
    
    -- Team specific fields
    phone_number VARCHAR(50),
    
    -- Tournament_Organizer specific fields
    organization VARCHAR(255),
    
    -- Player specific fields
    position VARCHAR(100),
    jersey_number INTEGER,
    
    -- Referee specific fields
    certification_level VARCHAR(100),
    years_of_experience INTEGER,
    
    -- Timestamps
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================================
-- TOURNAMENTS TABLE
-- ============================================================================
CREATE TABLE tournaments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    status VARCHAR(50),
    tournament_type VARCHAR(50),  -- LEAGUE or KNOCKOUT
    organizer_id BIGINT,
    
    -- League specific fields
    points_for_win INTEGER,
    points_for_draw INTEGER,
    points_for_loss INTEGER,
    
    -- Knockout specific fields
    number_of_rounds INTEGER,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (organizer_id) REFERENCES users(id)
);

-- ============================================================================
-- SPORTS TABLE
-- ============================================================================
CREATE TABLE sports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    team_size INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- TEAMS TABLE
-- ============================================================================
CREATE TABLE teams (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    manager_id BIGINT,
    sport_id BIGINT,
    tournament_id BIGINT,
    registration_fee_paid BOOLEAN DEFAULT FALSE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (manager_id) REFERENCES users(id),
    FOREIGN KEY (sport_id) REFERENCES sports(id),
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
);

-- ============================================================================
-- PLAYERS TABLE
-- ============================================================================
-- Relationship between Player users and Teams
CREATE TABLE team_players (
    team_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    PRIMARY KEY (team_id, player_id),
    FOREIGN KEY (team_id) REFERENCES teams(id),
    FOREIGN KEY (player_id) REFERENCES users(id)
);

-- ============================================================================
-- VENUES TABLE
-- ============================================================================
CREATE TABLE venues (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    capacity INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- TIME_SLOTS TABLE
-- ============================================================================
CREATE TABLE time_slots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    slot_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- TEAM_TIME_SLOT_PREFERENCES TABLE
-- ============================================================================
-- Many-to-many relationship between teams and preferred time slots
CREATE TABLE team_time_slot_preferences (
    team_id BIGINT NOT NULL,
    time_slot_id BIGINT NOT NULL,
    PRIMARY KEY (team_id, time_slot_id),
    FOREIGN KEY (team_id) REFERENCES teams(id),
    FOREIGN KEY (time_slot_id) REFERENCES time_slots(id)
);

-- ============================================================================
-- MATCHES TABLE
-- ============================================================================
CREATE TABLE matches (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tournament_id BIGINT,
    home_team_id BIGINT,
    away_team_id BIGINT,
    venue_id BIGINT,
    scheduled_time TIMESTAMP,
    home_score INTEGER DEFAULT 0,
    away_score INTEGER DEFAULT 0,
    status VARCHAR(50),  -- SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    
    -- Match specific fields
    round_number INTEGER,
    match_number INTEGER,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY (home_team_id) REFERENCES teams(id),
    FOREIGN KEY (away_team_id) REFERENCES teams(id),
    FOREIGN KEY (venue_id) REFERENCES venues(id)
);

-- ============================================================================
-- MATCH_REFEREES TABLE
-- ============================================================================
-- Relationship between matches and referee users
CREATE TABLE match_referees (
    match_id BIGINT NOT NULL,
    referee_id BIGINT NOT NULL,
    referee_type VARCHAR(50),  -- MAIN, ASSISTANT, etc.
    PRIMARY KEY (match_id, referee_id),
    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (referee_id) REFERENCES users(id)
);

-- ============================================================================
-- EVENT_ORGANIZERS TABLE
-- ============================================================================
-- Additional organizer information (not user accounts)
CREATE TABLE event_organizers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    contact_email VARCHAR(255),
    phone_number VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- INDEXES FOR PERFORMANCE
-- ============================================================================
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_teams_manager ON teams(manager_id);
CREATE INDEX idx_teams_sport ON teams(sport_id);
CREATE INDEX idx_matches_tournament ON matches(tournament_id);
CREATE INDEX idx_matches_teams ON matches(home_team_id, away_team_id);
CREATE INDEX idx_tournaments_organizer ON tournaments(organizer_id);

-- ============================================================================
-- ACTOR ROLES AND PERMISSIONS (Reference)
-- ============================================================================
-- Based on Sports Tournament Management Use Case

-- ADMINISTRATOR
--   Can perform: View_Tournament, Update_Team, View_Sport, Register_Team

-- TEAM  
--   Can perform: View_Tournament, Register_Team, Add_Games, View_Schedule

-- GAME_COORDINATOR
--   Can perform: Add_Games, Collect_Fees, View_Schedule, Manage_Referee, 
--                Final_Tournament, Post_Scores, Record_Match_Results, 
--                View_Games, View_Tournament

-- TOURNAMENT_ORGANIZER
--   Can perform: Post_Scores
