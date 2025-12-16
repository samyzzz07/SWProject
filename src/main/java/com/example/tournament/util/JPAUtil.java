package com.example.tournament.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class for managing JPA EntityManager instances.
 * Provides centralized database connection management for the Tournament Management System.
 */
public class JPAUtil {
    
    private static EntityManagerFactory entityManagerFactory;
    private static final String DEFAULT_PERSISTENCE_UNIT = "TournamentPU-H2";
    
    /**
     * Initialize the EntityManagerFactory with the default persistence unit (H2 in-memory database).
     */
    public static void initialize() {
        initialize(DEFAULT_PERSISTENCE_UNIT);
    }
    
    /**
     * Initialize the EntityManagerFactory with a specific persistence unit.
     * 
     * @param persistenceUnitName Name of the persistence unit to use
     *                           Options: TournamentPU-H2, TournamentPU-MySQL, 
     *                                    TournamentPU-PostgreSQL, TournamentPU-SQLite
     */
    public static void initialize(String persistenceUnitName) {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            try {
                System.out.println("Initializing JPA with persistence unit: " + persistenceUnitName);
                entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
                System.out.println("JPA initialized successfully!");
            } catch (Exception e) {
                System.err.println("Failed to initialize JPA: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to create EntityManagerFactory", e);
            }
        }
    }
    
    /**
     * Get an EntityManager instance.
     * Initializes the factory if not already done.
     * 
     * @return EntityManager instance
     */
    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            initialize();
        }
        return entityManagerFactory.createEntityManager();
    }
    
    /**
     * Close the EntityManagerFactory.
     * Should be called when the application shuts down.
     */
    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            System.out.println("Shutting down JPA...");
            entityManagerFactory.close();
            System.out.println("JPA shutdown complete.");
        }
    }
    
    /**
     * Check if the EntityManagerFactory is initialized and open.
     * 
     * @return true if initialized and open, false otherwise
     */
    public static boolean isInitialized() {
        return entityManagerFactory != null && entityManagerFactory.isOpen();
    }
}
