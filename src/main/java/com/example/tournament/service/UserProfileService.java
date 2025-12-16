package com.example.tournament.service;

import com.example.tournament.model.User;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;

/**
 * Service class for user profile management.
 * Handles updating user profile information like email and password.
 */
public class UserProfileService {
    
    /**
     * Updates the email address for a user.
     * 
     * @param userId the ID of the user
     * @param newEmail the new email address
     * @return true if update was successful, false otherwise
     */
    public boolean updateEmail(Long userId, String newEmail) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            User user = em.find(User.class, userId);
            if (user == null) {
                return false;
            }
            
            user.setEmail(newEmail);
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
     * Updates the password for a user.
     * 
     * SECURITY NOTE: This implementation stores and compares plain-text passwords 
     * for demonstration purposes only. This is consistent with the existing 
     * LoginService implementation in this codebase.
     * 
     * In a production environment:
     * - Passwords should be hashed using a secure algorithm (e.g., BCrypt, Argon2)
     * - Comparisons should use constant-time comparison to prevent timing attacks
     * - Consider implementing password history to prevent reuse
     * - Add rate limiting to prevent brute force attacks
     * 
     * @param userId the ID of the user
     * @param currentPassword the current password for verification
     * @param newPassword the new password
     * @return true if update was successful, false otherwise
     */
    public boolean updatePassword(Long userId, String currentPassword, String newPassword) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            User user = em.find(User.class, userId);
            if (user == null) {
                return false;
            }
            
            // Verify current password (plain text comparison - see security note above)
            if (!user.getPassword().equals(currentPassword)) {
                return false;
            }
            
            user.setPassword(newPassword);
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
     * Retrieves the current user data from the database.
     * 
     * @param userId the ID of the user
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
}
