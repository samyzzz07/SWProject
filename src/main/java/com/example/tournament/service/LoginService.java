package com.example.tournament.service;

import com.example.tournament.model.User;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * Service class for user authentication and login functionality.
 */
public class LoginService {
    
    /**
     * Authenticates a user with username and password.
     * 
     * @param username the username
     * @param password the password
     * @return the authenticated User object, or null if authentication fails
     */
    public User authenticate(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
                User.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);
            
            return query.getSingleResult();
            
        } catch (NoResultException e) {
            return null; // Invalid credentials
        } finally {
            em.close();
        }
    }
    
    /**
     * Registers a new user in the system.
     * 
     * @param user the user to register
     * @return true if registration was successful, false otherwise
     */
    public boolean registerUser(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            em.persist(user);
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
     * Checks if a username already exists.
     * 
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username",
                Long.class
            );
            query.setParameter("username", username);
            
            return query.getSingleResult() > 0;
            
        } finally {
            em.close();
        }
    }
}
