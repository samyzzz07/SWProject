package com.example.tournament;

/**
 * Launcher class for the Tournament Management System.
 * 
 * This class serves as the entry point when running the application as an executable JAR file.
 * It launches the JavaFX application without directly extending Application,
 * which resolves the "JavaFX runtime components are missing" error that occurs
 * when running a shaded JAR with java -jar.
 * 
 * The Maven Shade Plugin uses this class as the main class in the MANIFEST.MF,
 * which then delegates to the actual JavaFX Application class (TournamentManagementApp).
 * 
 * This pattern is necessary because JavaFX's launcher checks if the main class
 * extends Application. When packaged in a fat JAR, this check can fail,
 * but using a separate launcher class that calls Application.launch() works correctly.
 * 
 * @see TournamentManagementApp
 */
public class Launcher {
    
    /**
     * Main method that launches the JavaFX application.
     * 
     * @param args command line arguments passed to the JavaFX application
     */
    public static void main(String[] args) {
        TournamentManagementApp.main(args);
    }
}
