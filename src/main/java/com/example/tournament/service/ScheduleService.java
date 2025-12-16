package com.example.tournament.service;

import com.example.tournament.model.Match;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Service class for schedule-related operations.
 * Implements ViewSchedule use case.
 */
public class ScheduleService {
    
    /**
     * Retrieves all scheduled matches.
     * Implements ViewSchedule use case.
     * 
     * @return list of all matches
     */
    public List<Match> viewSchedule() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m ORDER BY m.scheduledTime",
                Match.class
            );
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Retrieves schedule for a specific tournament.
     * 
     * @param tournamentId the tournament ID
     * @return list of matches for the tournament
     */
    public List<Match> viewScheduleByTournament(Long tournamentId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m WHERE m.tournament.id = :tournamentId ORDER BY m.scheduledTime",
                Match.class
            );
            query.setParameter("tournamentId", tournamentId);
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }
}
