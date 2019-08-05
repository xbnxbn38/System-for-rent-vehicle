package controller.application;
	
import java.net.URL;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			URL location=getClass().getResource("/view/MainWindow.fxml");
			FXMLLoader fxmlLoader=new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent root = fxmlLoader.load();
			
			primaryStage.setTitle("ThriftyRent");
			primaryStage.setScene(new Scene(root));
			primaryStage.setMaximized(true);
			
			MainController mainController=fxmlLoader.getController();
			mainController.initial(primaryStage);
			
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
