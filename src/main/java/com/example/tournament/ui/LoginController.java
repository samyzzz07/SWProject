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
    private TextField registerPhoneField;
    
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
            UserRole.PLAYER,
            UserRole.NON_MANAGER,
            UserRole.GAME_MANAGER,
            UserRole.REFEREE
        );
        
        statusLabel.setText("Please login or register");
    }
    
    /**
     * Handles login button click.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password");
            return;
        }
        
        User user = loginService.authenticate(username, password);
        
        if (user != null) {
            statusLabel.setText("Login successful!");
            openRoleBasedView(user);
        } else {
            statusLabel.setText("Invalid credentials");
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
        String phoneNumber = registerPhoneField.getText();
        UserRole role = roleComboBox.getValue();
        
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role == null) {
            statusLabel.setText("Please fill all registration fields");
            return;
        }
        
        if (loginService.usernameExists(username)) {
            statusLabel.setText("Username already exists");
            showAlert("Registration Failed", "Username already exists");
            return;
        }
        
        User newUser = createUserByRole(username, password, email, phoneNumber, role);
        
        if (loginService.registerUser(newUser)) {
            statusLabel.setText("Registration successful! Please login");
            showAlert("Success", "Registration successful! You can now login.");
            
            // Clear registration fields
            registerUsernameField.clear();
            registerPasswordField.clear();
            registerEmailField.clear();
            registerPhoneField.clear();
            roleComboBox.setValue(null);
        } else {
            statusLabel.setText("Registration failed");
            showAlert("Error", "Registration failed. Please try again.");
        }
    }
    
    /**
     * Creates a user object based on the selected role.
     */
    private User createUserByRole(String username, String password, String email, String phoneNumber, UserRole role) {
        // Normalize empty string to null for consistency
        String normalizedPhone = (phoneNumber != null && !phoneNumber.trim().isEmpty()) ? phoneNumber : null;
        
        return switch (role) {
            case PLAYER -> new Player(username, password, email, normalizedPhone);
            case NON_MANAGER -> new NonManager(username, password, email, normalizedPhone);
            case GAME_MANAGER -> new GameManager(username, password, email, normalizedPhone);
            case REFEREE -> new Referee(username, password, email, normalizedPhone);
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        };
    }
    
    /**
     * Opens the appropriate view based on user role.
     */
    private void openRoleBasedView(User user) {
        try {
            String fxmlFile = switch (user.getRole()) {
                case PLAYER -> "/fxml/player_view.fxml";
                case NON_MANAGER -> "/fxml/non_manager_view.fxml";
                case GAME_MANAGER -> "/fxml/game_manager_view.fxml";
                case REFEREE -> "/fxml/referee_view.fxml";
                default -> "/fxml/participant_team_view.fxml"; // Fallback
            };
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            // Set the current user in the controller
            Object controller = loader.getController();
            if (controller instanceof PlayerController) {
                ((PlayerController) controller).setCurrentUser(user);
            } else if (controller instanceof NonManagerController) {
                ((NonManagerController) controller).setCurrentUser(user);
            } else if (controller instanceof GameManagerController) {
                ((GameManagerController) controller).setCurrentUser(user);
            } else if (controller instanceof RefereeController) {
                ((RefereeController) controller).setCurrentUser(user);
            }
            
            // Get current stage and set new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setScene(scene);
            stage.setTitle("Tournament Management System - " + user.getRole());
            
        } catch (IOException e) {
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
