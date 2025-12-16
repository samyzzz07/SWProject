package com.example.tournament.service;

import com.example.tournament.model.User;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;

/**
 * Service class for user profile management.
 */
public class UserService {
    
    /**
     * Updates user profile information.
     * 
     * @param user the user with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateUserProfile(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            em.merge(user);
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
     * Retrieves a user by ID.
     * 
     * @param userId the user ID
     * @return the User object, or null if not found
     */
    public User getUserById(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            return em.find(User.class, userId);
        } finally {
            em.close();
        }
    }
    
    /**
     * Changes user password.
     * 
     * NOTE: This implementation uses plain-text password for demonstration purposes.
     * In a production environment, passwords should be hashed using a secure algorithm.
     * 
     * @param userId the user ID
     * @param newPassword the new password
     * @return true if password change was successful, false otherwise
     */
    public boolean changePassword(Long userId, String newPassword) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setPassword(newPassword);
                em.merge(user);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
            
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
