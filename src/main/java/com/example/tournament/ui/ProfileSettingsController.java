package com.example.tournament.ui;

import com.example.tournament.model.User;
import com.example.tournament.service.UserProfileService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for the Profile Settings dialog.
 * Allows users to update their email and password.
 */
public class ProfileSettingsController {
    
    @FXML private Label usernameLabel;
    @FXML private Label currentEmailLabel;
    @FXML private TextField newEmailField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button updateEmailButton;
    @FXML private Button updatePasswordButton;
    @FXML private Button closeButton;
    @FXML private Label statusLabel;
    
    private User currentUser;
    private UserProfileService profileService;
    
    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        profileService = new UserProfileService();
        statusLabel.setText("Ready");
    }
    
    /**
     * Sets the current user and displays their information.
     * 
     * @param user the current user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        usernameLabel.setText(user.getUsername());
        currentEmailLabel.setText(user.getEmail());
    }
    
    /**
     * Handles the update email button click.
     */
    @FXML
    private void handleUpdateEmail() {
        String newEmail = newEmailField.getText().trim();
        
        if (newEmail.isEmpty()) {
            statusLabel.setText("Please enter a new email address");
            showAlert("Validation Error", "Email address cannot be empty");
            return;
        }
        
        // Improved email validation using regex
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!newEmail.matches(emailPattern)) {
            statusLabel.setText("Invalid email format");
            showAlert("Validation Error", "Please enter a valid email address");
            return;
        }
        
        if (profileService.updateEmail(currentUser.getId(), newEmail)) {
            statusLabel.setText("Email updated successfully!");
            currentEmailLabel.setText(newEmail);
            currentUser.setEmail(newEmail); // Update local copy
            newEmailField.clear();
            showAlert("Success", "Your email has been updated successfully");
        } else {
            statusLabel.setText("Failed to update email");
            showAlert("Error", "Failed to update email. Please try again.");
        }
    }
    
    /**
     * Handles the update password button click.
     */
    @FXML
    private void handleUpdatePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("All password fields are required");
            showAlert("Validation Error", "Please fill in all password fields");
            return;
        }
        
        if (newPassword.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters");
            showAlert("Validation Error", "Password must be at least 6 characters long");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            statusLabel.setText("New passwords do not match");
            showAlert("Validation Error", "New password and confirmation do not match");
            return;
        }
        
        if (profileService.updatePassword(currentUser.getId(), currentPassword, newPassword)) {
            statusLabel.setText("Password updated successfully!");
            // Note: Password is stored in the database. Local User object password 
            // is not updated to avoid keeping sensitive data in memory.
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
            showAlert("Success", "Your password has been updated successfully");
        } else {
            statusLabel.setText("Failed to update password");
            showAlert("Error", "Failed to update password. Please check your current password and try again.");
        }
    }
    
    /**
     * Handles the close button click.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Shows an alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert.AlertType type = title.toLowerCase().contains("error") 
            ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
