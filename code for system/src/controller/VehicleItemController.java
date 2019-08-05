package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.constant.VehicleStatus;
import model.entity.RentalVehicle;

public class VehicleItemController implements Initializable{
	
	@FXML TextArea textAreaDescription;
	@FXML Label labelNew;
	@FXML Label labelMake;
	@FXML Button buttonInfo;
	@FXML Button buttonRent;
	@FXML ImageView imageViewMain;
	@FXML GridPane gridPaneButtons;
	
	private RentalVehicle rentalVehicle;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		buttonInfo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setButtonInfoAction();
				
			}
		});
		
		buttonRent.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				setButtonRentAction();
			}
		});
	}
	
	protected void setButtonInfoAction() {
		URL location=getClass().getResource("/view/RentalVehicleDetail.fxml");
		FXMLLoader fxmlLoader=new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		
		try {
			VBox vbox = fxmlLoader.load();
			RentalVehicleDetailController rentalVehicleDetailController=fxmlLoader.getController();
			rentalVehicleDetailController.initial(rentalVehicle);
			
			Stage stage=new Stage();
			stage.setTitle("Detail");
			stage.setScene(new Scene(vbox));
			stage.setResizable(false);
			stage.show();
			
			rentalVehicleDetailController.setVehicleItemController(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	protected void setButtonRentAction() {
		URL location=getClass().getResource("/view/BookRentalVehicleItem.fxml");
		FXMLLoader fxmlLoader=new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		try {
			VBox vbox = fxmlLoader.load();
			BookRentalVehicleItemController bookRentalVehicleItemController=fxmlLoader.getController();
			bookRentalVehicleItemController.bookRentalVehicle(rentalVehicle);
			Stage stage=new Stage();
			bookRentalVehicleItemController.setStage(stage);
			bookRentalVehicleItemController.setVehicleItemController(this);
			stage.setTitle("Book");
			stage.setScene(new Scene(vbox));
			stage.setResizable(false);
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void initial(RentalVehicle rentalVehicle) {
		if(rentalVehicle==null) {
			return;
		}
		this.rentalVehicle=rentalVehicle;
		
		labelNew.setVisible(false);
		labelMake.setText(rentalVehicle.getMake());
		textAreaDescription.setText(rentalVehicle.getManuY()+" "+rentalVehicle.getMake()+" "+rentalVehicle.getModel()+"\n\n"+rentalVehicle.getDescription());
		String img = "images/" + rentalVehicle.getImageName();
		File file = new File(img);
		Image newImage = new Image(file.toURI().toString());
		imageViewMain.setImage(newImage);
		setButtonIsDisable(rentalVehicle);
	}

	private void setButtonIsDisable(RentalVehicle rentalVehicle) {
		if(rentalVehicle.getStatus().equals(VehicleStatus.AVAILABLE)) {
			buttonRent.setDisable(false);
		}else {
			buttonRent.setDisable(true);
		}
	}
	
	public void refresh() {
		setButtonIsDisable(rentalVehicle);
	}

}
