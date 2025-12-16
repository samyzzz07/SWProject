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
}
