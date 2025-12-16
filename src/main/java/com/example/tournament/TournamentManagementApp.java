package com.example.tournament;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the Tournament Management System.
 * This is the application class you should run to start the tournament management application.
 */
public class TournamentManagementApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the login FXML file
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/login.fxml")
            );
            Parent root = loader.load();
            
            // Set up the scene
            Scene scene = new Scene(root, 800, 600);
            
            // Configure the primary stage
            primaryStage.setTitle("Tournament Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            
            // Show the application window
            primaryStage.show();
            
            System.out.println("Tournament Management System started successfully!");
            
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Main method - the entry point of the application.
     * Run this method to start the Tournament Management System.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting Tournament Management System...");
        launch(args);
    }
}
