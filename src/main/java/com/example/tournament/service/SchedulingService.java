package com.example.tournament.service;

import com.example.tournament.model.Match;
import com.example.tournament.model.Tournament;
import com.example.tournament.model.Venue;

import java.util.List;

/**
 * Interface for scheduling services.
 */
public interface SchedulingService {
    
    /**
     * Schedules matches for a tournament, considering venue availability and team preferences.
     * @param tournament The tournament to schedule
     * @param venues Available venues for scheduling
     */
    void scheduleMatches(Tournament tournament, List<Venue> venues);
    
    /**
     * Reschedules a specific match.
     * @param match The match to reschedule
     * @param venues Available venues
     * @return true if rescheduling was successful
     */
    boolean rescheduleMatch(Match match, List<Venue> venues);
    
    /**
     * Finds optimal venue for a match based on team preferences and availability.
     * @param match The match to find a venue for
     * @param venues Available venues
     * @return Optimal venue or null if none available
     */
    Venue findOptimalVenue(Match match, List<Venue> venues);
    
    /**
     * Validates that the schedule has no conflicts.
     * @param tournament The tournament to validate
     * @return true if schedule is valid
     */
    boolean validateSchedule(Tournament tournament);
}
