package com.example.tournament.service;

import com.example.tournament.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of SchedulingService with advanced scheduling logic.
 */
public class SchedulingManager implements SchedulingService {
    
    @Override
    public void scheduleMatches(Tournament tournament, List<Venue> venues) {
        System.out.println("=== Advanced Scheduling System ===");
        System.out.println("Tournament: " + tournament.getName());
        System.out.println("Available venues: " + venues.size());
        System.out.println("Matches to schedule: " + tournament.getMatches().size());
        
        if (venues.isEmpty()) {
            System.out.println("Error: No venues available for scheduling.");
            return;
        }
        
        // Generate base schedule using tournament-specific logic
        tournament.generateSchedule();
        
        // Now assign venues and time slots to each match
        LocalDateTime currentTime = tournament.getStartDate().atTime(9, 0);
        int matchDurationHours = 2;
        
        for (Match match : tournament.getMatches()) {
            System.out.println("\n--- Scheduling Match: " + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + " ---");
            
            // Step 1: Find team time slot preferences
            List<TimeSlot> team1Preferences = match.getTeam1().getPreferredTimeSlots();
            List<TimeSlot> team2Preferences = match.getTeam2().getPreferredTimeSlots();
            
            System.out.println("Team 1 has " + team1Preferences.size() + " time slot preferences");
            System.out.println("Team 2 has " + team2Preferences.size() + " time slot preferences");
            
            // Step 2: Find common time preferences
            TimeSlot selectedTimeSlot = findCommonTimeSlot(team1Preferences, team2Preferences);
            
            if (selectedTimeSlot == null) {
                // No common preference, create a default time slot
                selectedTimeSlot = new TimeSlot(currentTime, currentTime.plusHours(matchDurationHours));
                System.out.println("No common preference found. Using default time: " + currentTime);
            } else {
                System.out.println("Found common time slot preference: " + selectedTimeSlot.getStartTime());
            }
            
            // Step 3: Find optimal venue
            Venue optimalVenue = findOptimalVenue(match, venues);
            
            if (optimalVenue != null) {
                // Step 4: Check venue availability
                if (optimalVenue.isAvailable(selectedTimeSlot)) {
                    System.out.println("Venue '" + optimalVenue.getName() + "' is available.");
                    
                    // Assign venue and time slot
                    match.setVenue(optimalVenue);
                    match.setTimeSlot(selectedTimeSlot);
                    match.setScheduledTime(selectedTimeSlot.getStartTime());
                    
                    // Book the venue
                    optimalVenue.bookTimeSlot(selectedTimeSlot);
                    
                    System.out.println("✓ Match scheduled at " + optimalVenue.getName() + " on " + selectedTimeSlot.getStartTime());
                } else {
                    System.out.println("Venue '" + optimalVenue.getName() + "' is not available. Finding alternative...");
                    
                    // Try to find another venue
                    Venue alternativeVenue = findAlternativeVenue(selectedTimeSlot, venues);
                    if (alternativeVenue != null) {
                        match.setVenue(alternativeVenue);
                        match.setTimeSlot(selectedTimeSlot);
                        match.setScheduledTime(selectedTimeSlot.getStartTime());
                        alternativeVenue.bookTimeSlot(selectedTimeSlot);
                        System.out.println("✓ Match scheduled at alternative venue " + alternativeVenue.getName());
                    } else {
                        System.out.println("⚠ No venue available. Match remains unscheduled.");
                    }
                }
            } else {
                System.out.println("⚠ No optimal venue found. Match remains unscheduled.");
            }
            
            // Move to next time slot
            currentTime = currentTime.plusHours(matchDurationHours + 1);
        }
        
        System.out.println("\n=== Scheduling Complete ===");
        validateSchedule(tournament);
    }
    
    @Override
    public boolean rescheduleMatch(Match match, List<Venue> venues) {
        System.out.println("=== Rescheduling Match ===");
        System.out.println("Match: " + match.getTeam1().getName() + " vs " + match.getTeam2().getName());
        
        // Free up the current venue booking if exists
        if (match.getVenue() != null && match.getTimeSlot() != null) {
            System.out.println("Releasing current booking at " + match.getVenue().getName());
        }
        
        // Find new venue
        Venue newVenue = findOptimalVenue(match, venues);
        if (newVenue != null) {
            // Create new time slot
            TimeSlot newTimeSlot = new TimeSlot(
                LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(16).withMinute(0)
            );
            
            if (newVenue.isAvailable(newTimeSlot)) {
                match.setVenue(newVenue);
                match.setTimeSlot(newTimeSlot);
                match.setScheduledTime(newTimeSlot.getStartTime());
                newVenue.bookTimeSlot(newTimeSlot);
                
                System.out.println("✓ Match rescheduled successfully to " + newVenue.getName());
                return true;
            }
        }
        
        System.out.println("✗ Failed to reschedule match");
        return false;
    }
    
    @Override
    public Venue findOptimalVenue(Match match, List<Venue> venues) {
        System.out.println("Finding optimal venue...");
        
        if (venues.isEmpty()) {
            return null;
        }
        
        // Placeholder logic: select first available venue
        // In a real implementation, this would consider:
        // - Distance to teams
        // - Venue capacity
        // - Venue facilities
        // - Historical preferences
        
        for (Venue venue : venues) {
            System.out.println("Considering venue: " + venue.getName() + " (capacity: " + venue.getCapacity() + ")");
        }
        
        Venue optimal = venues.get(0);
        System.out.println("Selected optimal venue: " + optimal.getName());
        return optimal;
    }
    
    @Override
    public boolean validateSchedule(Tournament tournament) {
        System.out.println("\n=== Validating Schedule ===");
        
        int scheduledMatches = 0;
        int unscheduledMatches = 0;
        
        for (Match match : tournament.getMatches()) {
            if (match.getVenue() != null && match.getScheduledTime() != null) {
                scheduledMatches++;
            } else {
                unscheduledMatches++;
            }
        }
        
        System.out.println("Scheduled matches: " + scheduledMatches);
        System.out.println("Unscheduled matches: " + unscheduledMatches);
        
        // Check for venue conflicts
        boolean hasConflicts = checkVenueConflicts(tournament);
        
        if (!hasConflicts && unscheduledMatches == 0) {
            System.out.println("✓ Schedule is valid with no conflicts");
            return true;
        } else {
            System.out.println("⚠ Schedule has issues");
            return false;
        }
    }
    
    /**
     * Finds a common time slot between two teams' preferences.
     */
    private TimeSlot findCommonTimeSlot(List<TimeSlot> team1Prefs, List<TimeSlot> team2Prefs) {
        for (TimeSlot slot1 : team1Prefs) {
            for (TimeSlot slot2 : team2Prefs) {
                if (slot1.overlapsWith(slot2)) {
                    System.out.println("Found overlapping preference window");
                    return slot1;
                }
            }
        }
        return null;
    }
    
    /**
     * Finds an alternative venue that is available at the given time slot.
     */
    private Venue findAlternativeVenue(TimeSlot timeSlot, List<Venue> venues) {
        for (Venue venue : venues) {
            if (venue.isAvailable(timeSlot)) {
                return venue;
            }
        }
        return null;
    }
    
    /**
     * Checks for venue booking conflicts in the tournament schedule.
     */
    private boolean checkVenueConflicts(Tournament tournament) {
        List<Match> scheduledMatches = new ArrayList<>();
        
        for (Match match : tournament.getMatches()) {
            if (match.getVenue() != null && match.getTimeSlot() != null) {
                scheduledMatches.add(match);
            }
        }
        
        // Check each pair of matches for conflicts
        for (int i = 0; i < scheduledMatches.size(); i++) {
            for (int j = i + 1; j < scheduledMatches.size(); j++) {
                Match match1 = scheduledMatches.get(i);
                Match match2 = scheduledMatches.get(j);
                
                if (match1.getVenue().equals(match2.getVenue()) && 
                    match1.getTimeSlot().overlapsWith(match2.getTimeSlot())) {
                    System.out.println("⚠ Conflict detected: Same venue booked for overlapping times");
                    return true;
                }
            }
        }
        
        return false;
    }
}
