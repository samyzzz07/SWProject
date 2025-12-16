package com.example.tournament.service;

import com.example.tournament.model.Match;
import com.example.tournament.model.Team;
import com.example.tournament.model.Tournament;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Service class for game-related operations.
 * Implements AddGames and ViewGames use cases.
 */
public class GameService {
    
    /**
     * Adds a new game/match to the system.
     * Implements AddGames use case.
     * 
     * @param match the match to add
     * @return true if successful, false otherwise
     */
    public boolean addGame(Match match) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            em.persist(match);
            em.getTransaction().commit();
            return true;
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Views all games/matches.
     * Implements ViewGames use case.
     * 
     * @return list of all matches
     */
    public List<Match> viewAllGames() {
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
     * Views games for a specific tournament.
     * 
     * @param tournamentId the tournament ID
     * @return list of matches for the tournament
     */
    public List<Match> viewGamesByTournament(Long tournamentId) {
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
