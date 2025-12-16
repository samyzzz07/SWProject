package com.example.tournament.service;

import com.example.tournament.model.Referee;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Service class for referee-related operations.
 * Implements ManageReferee use case.
 */
public class RefereeService {
    
    /**
     * Adds a new referee to the system.
     * Part of ManageReferee use case.
     * 
     * @param referee the referee to add
     * @return true if successful, false otherwise
     */
    public boolean addReferee(Referee referee) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            em.persist(referee);
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
     * Updates referee information.
     * Part of ManageReferee use case.
     * 
     * @param referee the referee with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateReferee(Referee referee) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            em.merge(referee);
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
     * Removes a referee from the system.
     * Part of ManageReferee use case.
     * 
     * @param refereeId the ID of the referee to remove
     * @return true if successful, false otherwise
     */
    public boolean removeReferee(Long refereeId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Referee referee = em.find(Referee.class, refereeId);
            if (referee == null) {
                em.getTransaction().rollback();
                return false;
            }
            
            em.remove(referee);
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
     * Retrieves all referees.
     * 
     * @return list of all referees
     */
    public List<Referee> getAllReferees() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Referee> query = em.createQuery(
                "SELECT r FROM Referee r ORDER BY r.username",
                Referee.class
            );
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }
}
