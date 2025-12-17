package com.example.tournament.service;

import com.example.tournament.model.Tournament;
import com.example.tournament.model.TournamentStatus;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Service class for tournament-related operations.
 * Implements ViewTournament and FinalTournament use cases.
 */
public class TournamentService {
    
    /**
     * Retrieves all tournaments in the system.
     * Implements ViewTournament use case.
     * 
     * @return list of all tournaments
     */
    public List<Tournament> viewAllTournaments() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Tournament> query = em.createQuery(
                "SELECT DISTINCT t FROM Tournament t " +
                "LEFT JOIN FETCH t.teams " +
                "LEFT JOIN FETCH t.matches " +
                "ORDER BY t.startDate DESC",
                Tournament.class
            );
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Retrieves a specific tournament by ID.
     * 
     * @param tournamentId the tournament ID
     * @return the tournament, or null if not found
     */
    public Tournament getTournamentById(Long tournamentId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Tournament> query = em.createQuery(
                "SELECT t FROM Tournament t " +
                "LEFT JOIN FETCH t.teams " +
                "LEFT JOIN FETCH t.matches " +
                "WHERE t.id = :id",
                Tournament.class
            );
            query.setParameter("id", tournamentId);
            List<Tournament> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Finalizes a tournament.
     * Implements FinalTournament use case.
     * 
     * @param tournamentId the tournament to finalize
     * @return true if successful, false otherwise
     */
    public boolean finalizeTournament(Long tournamentId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Tournament tournament = em.find(Tournament.class, tournamentId);
            if (tournament == null) {
                em.getTransaction().rollback();
                return false;
            }
            
            tournament.setStatus(TournamentStatus.COMPLETED);
            em.merge(tournament);
            
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
     * Creates a new tournament in the system.
     * 
     * @param tournament the tournament to create
     * @return true if successful, false otherwise
     */
    public boolean createTournament(Tournament tournament) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Set initial status if not set
            if (tournament.getStatus() == null) {
                tournament.setStatus(TournamentStatus.SCHEDULED);
            }
            
            em.persist(tournament);
            
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
     * Updates an existing tournament.
     * 
     * @param tournament the tournament to update
     * @return true if successful, false otherwise
     */
    public boolean updateTournament(Tournament tournament) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            em.merge(tournament);
            
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
