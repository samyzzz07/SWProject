package com.example.tournament.ui;

import com.example.tournament.model.User;
import com.example.tournament.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for User Profile interface.
 * Allows users to view and update their profile information.
 */
public class UserProfileController {
    
    @FXML private Label welcomeLabel;
    @FXML private Label userIdLabel;
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private TextField emailField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button updateEmailButton;
    @FXML private Button changePasswordButton;
    @FXML private Button backButton;
    @FXML private Label statusLabel;
    
    private User currentUser;
    private UserService userService;
    
    @FXML
    public void initialize() {
        userService = new UserService();
        statusLabel.setText("Ready");
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadUserProfile();
    }
    
    private void loadUserProfile() {
        if (currentUser != null) {
            welcomeLabel.setText("User Profile");
            userIdLabel.setText("User ID: " + currentUser.getId());
            usernameLabel.setText("Username: " + currentUser.getUsername());
            roleLabel.setText("Role: " + currentUser.getRole());
            emailField.setText(currentUser.getEmail());
        }
    }
    
    @FXML
    private void handleUpdateEmail() {
        String newEmail = emailField.getText();
        
        if (newEmail == null || newEmail.trim().isEmpty()) {
            statusLabel.setText("Email cannot be empty");
            showAlert("Error", "Email cannot be empty");
            return;
        }
        
        if (!isValidEmail(newEmail)) {
            statusLabel.setText("Invalid email format");
            showAlert("Error", "Please enter a valid email address");
            return;
        }
        
        currentUser.setEmail(newEmail);
        
        if (userService.updateUserProfile(currentUser)) {
            statusLabel.setText("Email updated successfully!");
            showAlert("Success", "Your email has been updated successfully");
        } else {
            statusLabel.setText("Failed to update email");
            showAlert("Error", "Failed to update email. Please try again.");
            // Reload the original email
            emailField.setText(currentUser.getEmail());
        }
    }
    
    @FXML
    private void handleChangePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("All password fields are required");
            showAlert("Error", "Please fill in all password fields");
            return;
        }
        
        if (!currentPassword.equals(currentUser.getPassword())) {
            statusLabel.setText("Current password is incorrect");
            showAlert("Error", "Current password is incorrect");
            clearPasswordFields();
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            statusLabel.setText("New passwords do not match");
            showAlert("Error", "New password and confirm password do not match");
            clearPasswordFields();
            return;
        }
        
        if (newPassword.length() < 4) {
            statusLabel.setText("Password must be at least 4 characters");
            showAlert("Error", "Password must be at least 4 characters long");
            clearPasswordFields();
            return;
        }
        
        if (userService.changePassword(currentUser.getId(), newPassword)) {
            currentUser.setPassword(newPassword);
            statusLabel.setText("Password changed successfully!");
            showAlert("Success", "Your password has been changed successfully");
            clearPasswordFields();
        } else {
            statusLabel.setText("Failed to change password");
            showAlert("Error", "Failed to change password. Please try again.");
            clearPasswordFields();
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            String fxmlFile = switch (currentUser.getRole()) {
                case PLAYER -> "/fxml/player_view.fxml";
                case NON_MANAGER -> "/fxml/non_manager_view.fxml";
                case GAME_MANAGER -> "/fxml/game_manager_view.fxml";
                case REFEREE -> "/fxml/referee_view.fxml";
                default -> "/fxml/login.fxml";
            };
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            // Set the current user in the controller
            Object controller = loader.getController();
            if (controller instanceof PlayerController) {
                ((PlayerController) controller).setCurrentUser(currentUser);
            } else if (controller instanceof NonManagerController) {
                ((NonManagerController) controller).setCurrentUser(currentUser);
            } else if (controller instanceof GameManagerController) {
                ((GameManagerController) controller).setCurrentUser(currentUser);
            } else if (controller instanceof RefereeController) {
                ((RefereeController) controller).setCurrentUser(currentUser);
            }
            
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setScene(scene);
            stage.setTitle("Tournament Management System - " + currentUser.getRole());
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load view: " + e.getMessage());
        }
    }
    
    private void clearPasswordFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
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
