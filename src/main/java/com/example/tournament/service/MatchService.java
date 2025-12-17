package com.example.tournament.service;

import com.example.tournament.model.Match;
import com.example.tournament.model.Tournament;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Service class for match-related operations.
 * Handles CRUD operations for matches.
 */
public class MatchService {
    
    /**
     * Retrieves all matches from the database.
     * 
     * @return list of all matches
     */
    public List<Match> getAllMatches() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m " +
                "LEFT JOIN FETCH m.team1 " +
                "LEFT JOIN FETCH m.team2 " +
                "LEFT JOIN FETCH m.venue " +
                "ORDER BY m.scheduledTime",
                Match.class
            );
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Retrieves matches for a specific tournament.
     * 
     * @param tournamentId the tournament ID
     * @return list of matches for the tournament
     */
    public List<Match> getMatchesByTournament(Long tournamentId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m " +
                "LEFT JOIN FETCH m.team1 " +
                "LEFT JOIN FETCH m.team2 " +
                "LEFT JOIN FETCH m.venue " +
                "WHERE m.tournament.id = :tournamentId " +
                "ORDER BY m.scheduledTime",
                Match.class
            );
            query.setParameter("tournamentId", tournamentId);
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Retrieves a specific match by ID.
     * 
     * @param matchId the match ID
     * @return the match, or null if not found
     */
    public Match getMatchById(Long matchId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m " +
                "LEFT JOIN FETCH m.team1 " +
                "LEFT JOIN FETCH m.team2 " +
                "LEFT JOIN FETCH m.venue " +
                "WHERE m.id = :id",
                Match.class
            );
            query.setParameter("id", matchId);
            List<Match> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Creates a new match in the database.
     * 
     * @param match the match to create
     * @return true if successful, false otherwise
     */
    public boolean createMatch(Match match) {
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
     * Saves multiple matches to the database.
     * 
     * @param matches the list of matches to save
     * @return true if successful, false otherwise
     */
    public boolean saveMatches(List<Match> matches) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            for (Match match : matches) {
                if (match.getId() == null) {
                    em.persist(match);
                } else {
                    em.merge(match);
                }
            }
            
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
     * Updates an existing match.
     * 
     * @param match the match to update
     * @return true if successful, false otherwise
     */
    public boolean updateMatch(Match match) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            em.merge(match);
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
     * Deletes a match from the database.
     * 
     * @param matchId the ID of the match to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteMatch(Long matchId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Match match = em.find(Match.class, matchId);
            if (match == null) {
                em.getTransaction().rollback();
                return false;
            }
            
            em.remove(match);
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
}
