package com.example.tournament.service;

import com.example.tournament.model.Team;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Service class for team-related operations.
 * Implements RegisterTeam, CollectFees, UpdateTeamInfo, and UpdateTeam use cases.
 */
public class TeamService {
    
    /**
     * Registers a new team.
     * Implements RegisterTeam use case.
     * 
     * @param team the team to register
     * @return true if successful, false otherwise
     */
    public boolean registerTeam(Team team) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Persist the team (includes CollectFees as part of registration)
            em.persist(team);
            
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
     * Updates team information.
     * Implements UpdateTeamInfo and UpdateTeam use cases.
     * 
     * @param team the team with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateTeam(Team team) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            em.merge(team);
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
     * Retrieves all teams.
     * 
     * @return list of all teams
     */
    public List<Team> getAllTeams() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Team> query = em.createQuery(
                "SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.players ORDER BY t.name",
                Team.class
            );
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Simulates fee collection for team registration.
     * Implements CollectFees use case.
     * 
     * @param teamId the team ID
     * @param feeAmount the fee amount
     * @return true if successful, false otherwise
     */
    public boolean collectFees(Long teamId, Double feeAmount) {
        // In a real system, this would integrate with a payment gateway
        // For now, we just mark it as collected
        System.out.println("Collecting fee of $" + feeAmount + " for team ID: " + teamId);
        return true;
    }
}
