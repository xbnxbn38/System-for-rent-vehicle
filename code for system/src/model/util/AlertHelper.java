package model.util;

import javafx.scene.control.Alert;
import javafx.stage.Window;

public class AlertHelper 
{
    static Alert alert;
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) 
    {
    	setAlert(alertType, title, message);
        alert.initOwner(owner);
        alert.show();
    }
    
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
    	setAlert(alertType, title, message);
        alert.show();
    }
    
    static void setAlert(Alert.AlertType alertType, String title, String message) 
    {
    	alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
    }
    
    public static void removeAlert() 
    {
    	alert.close();
    }
}