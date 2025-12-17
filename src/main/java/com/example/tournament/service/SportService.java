package com.example.tournament.service;

import com.example.tournament.model.Sport;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Service class for sport-related operations.
 * Implements ViewSport use case.
 */
public class SportService {
    
    /**
     * Retrieves all sports in the system.
     * Implements ViewSport use case.
     * 
     * @return list of all sports
     */
    public List<Sport> viewAllSports() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Sport> query = em.createQuery(
                "SELECT s FROM Sport s ORDER BY s.name",
                Sport.class
            );
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Retrieves a specific sport by ID.
     * 
     * @param sportId the sport ID
     * @return the sport, or null if not found
     */
    public Sport getSportById(Long sportId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            return em.find(Sport.class, sportId);
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Retrieves a sport by its name.
     * 
     * @param name the sport name
     * @return the sport, or null if not found
     */
    public Sport getSportByName(String name) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Sport> query = em.createQuery(
                "SELECT s FROM Sport s WHERE s.name = :name",
                Sport.class
            );
            query.setParameter("name", name);
            List<Sport> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Creates a new sport.
     * 
     * @param sport the sport to create
     * @return the created sport with ID set, or null if failed
     */
    public Sport createSport(Sport sport) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            em.persist(sport);
            
            em.getTransaction().commit();
            return sport;
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
            
        } finally {
            em.close();
        }
    }
}
