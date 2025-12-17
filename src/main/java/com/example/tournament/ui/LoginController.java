package com.example.tournament.ui;

import com.example.tournament.model.*;
import com.example.tournament.service.LoginService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the login interface.
 */
public class LoginController {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private ComboBox<UserRole> roleComboBox;
    
    @FXML
    private TextField registerUsernameField;
    
    @FXML
    private PasswordField registerPasswordField;
    
    @FXML
    private TextField registerEmailField;
    
    @FXML
    private Button registerButton;
    
    private LoginService loginService;
    
    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        loginService = new LoginService();
        
        // Populate role combo box
        roleComboBox.getItems().addAll(
            UserRole.ADMINISTRATOR,
            UserRole.TEAM,
            UserRole.GAME_COORDINATOR,
            UserRole.TOURNAMENT_ORGANIZER
        );
        
        statusLabel.setText("Ready - Please login or register");
        statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        
        System.out.println("LoginController initialized successfully");
    }
    
    /**
     * Handles login button click.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        System.out.println("Login attempt for username: " + username);
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password");
            statusLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
            System.out.println("Login failed: Empty credentials");
            return;
        }
        
        statusLabel.setText("Authenticating...");
        statusLabel.setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;");
        
        User user = loginService.authenticate(username, password);
        
        if (user != null) {
            statusLabel.setText("Login successful! Redirecting to dashboard...");
            statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            System.out.println("Login successful for user: " + username + " with role: " + user.getRole());
            openRoleBasedView(user);
        } else {
            statusLabel.setText("Invalid credentials - Please check username and password");
            statusLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
            System.out.println("Login failed: Invalid credentials for username: " + username);
            showAlert("Login Failed", "Invalid username or password");
        }
    }
    
    /**
     * Handles register button click.
     */
    @FXML
    private void handleRegister() {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String email = registerEmailField.getText();
        UserRole role = roleComboBox.getValue();
        
        System.out.println("Registration attempt for username: " + username + " with role: " + role);
        
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role == null) {
            statusLabel.setText("Please fill all registration fields");
            statusLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
            System.out.println("Registration failed: Missing fields");
            return;
        }
        
        statusLabel.setText("Checking username availability...");
        statusLabel.setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;");
        
        if (loginService.usernameExists(username)) {
            statusLabel.setText("Username already exists - Please choose another");
            statusLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
            System.out.println("Registration failed: Username already exists: " + username);
            showAlert("Registration Failed", "Username already exists");
            return;
        }
        
        statusLabel.setText("Creating account...");
        statusLabel.setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;");
        
        User newUser = createUserByRole(username, password, email, role);
        
        if (loginService.registerUser(newUser)) {
            statusLabel.setText("Registration successful! You can now login");
            statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            System.out.println("Registration successful for user: " + username + " with role: " + role);
            showAlert("Success", "Registration successful! You can now login.");
            
            // Clear registration fields
            registerUsernameField.clear();
            registerPasswordField.clear();
            registerEmailField.clear();
            roleComboBox.setValue(null);
        } else {
            statusLabel.setText("Registration failed - Please try again");
            statusLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
            System.out.println("Registration failed for user: " + username);
            showAlert("Error", "Registration failed. Please try again.");
        }
    }
    
    /**
     * Creates a user object based on the selected role.
     */
    private User createUserByRole(String username, String password, String email, UserRole role) {
        return switch (role) {
            case ADMINISTRATOR -> new Administrator(username, password, email);
            case TEAM -> new TeamManager(username, password, email);
            case GAME_COORDINATOR -> new GameCoordinator(username, password, email);
            case TOURNAMENT_ORGANIZER -> new TournamentOrganizer(username, password, email);
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        };
    }
    
    /**
     * Opens the appropriate view based on user role.
     */
    private void openRoleBasedView(User user) {
        try {
            System.out.println("Opening view for user role: " + user.getRole());
            
            String fxmlFile = switch (user.getRole()) {
                case ADMINISTRATOR -> "/fxml/administrator_view.fxml";
                case TEAM -> "/fxml/participant_team_view.fxml";
                case GAME_COORDINATOR -> "/fxml/game_coordinator_view.fxml";
                case TOURNAMENT_ORGANIZER -> "/fxml/tournament_organizer_view.fxml";
                default -> "/fxml/participant_team_view.fxml"; // Fallback
            };
            
            System.out.println("Loading FXML file: " + fxmlFile);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            // Set the current user in the controller
            Object controller = loader.getController();
            System.out.println("Controller loaded: " + controller.getClass().getSimpleName());
            
            if (controller instanceof GameCoordinatorController) {
                ((GameCoordinatorController) controller).setCurrentUser(user);
            } else if (controller instanceof ParticipantTeamController) {
                ((ParticipantTeamController) controller).setCurrentUser(user);
            } else if (controller instanceof AdministratorController) {
                ((AdministratorController) controller).setCurrentUser(user);
            } else if (controller instanceof TournamentOrganizerController) {
                ((TournamentOrganizerController) controller).setCurrentUser(user);
            }
            
            System.out.println("User set in controller successfully");
            
            // Get current stage and set new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setScene(scene);
            stage.setTitle("Tournament Management System - " + user.getRole());
            stage.setFullScreen(true);  // Maintain fullscreen mode
            
            System.out.println("View successfully loaded for user: " + user.getUsername());
            
        } catch (IOException e) {
            System.err.println("Error loading view: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load view: " + e.getMessage());
        }
    }
    
    /**
     * Shows an alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert.AlertType type = title.toLowerCase().contains("error") || title.toLowerCase().contains("fail") 
            ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
