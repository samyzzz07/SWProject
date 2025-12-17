package com.example.tournament.service;

import com.example.tournament.model.Venue;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Service class for venue-related operations.
 * Handles CRUD operations for venues.
 */
public class VenueService {
    
    /**
     * Retrieves all venues from the database.
     * 
     * @return list of all venues
     */
    public List<Venue> getAllVenues() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Venue> query = em.createQuery(
                "SELECT v FROM Venue v ORDER BY v.name",
                Venue.class
            );
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Retrieves a specific venue by ID.
     * 
     * @param venueId the venue ID
     * @return the venue, or null if not found
     */
    public Venue getVenueById(Long venueId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            return em.find(Venue.class, venueId);
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Creates a new venue in the database.
     * 
     * @param venue the venue to create
     * @return true if successful, false otherwise
     */
    public boolean createVenue(Venue venue) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            em.persist(venue);
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
     * Updates an existing venue.
     * 
     * @param venue the venue to update
     * @return true if successful, false otherwise
     */
    public boolean updateVenue(Venue venue) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            em.merge(venue);
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
     * Deletes a venue from the database.
     * 
     * @param venueId the ID of the venue to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteVenue(Long venueId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Venue venue = em.find(Venue.class, venueId);
            if (venue == null) {
                em.getTransaction().rollback();
                return false;
            }
            
            em.remove(venue);
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
