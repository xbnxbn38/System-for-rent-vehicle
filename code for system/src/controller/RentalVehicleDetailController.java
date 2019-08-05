package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.constant.VehicleStatus;
import model.constant.VehicleType;
import model.dao.RentalRecordDao;
import model.entity.Car;
import model.entity.Van;
import model.entity.RentalVehicle;
import model.entity.RentalRecord;
import model.exception.CompleteMaintenanceException;
import model.exception.PerformMaintenanceException;
import model.util.DateTime;

public class RentalVehicleDetailController implements Initializable{

	@FXML GridPane gridPaneButtons;
	@FXML Label labelMake;
	@FXML Button buttonRent;
	@FXML TextArea textAreaDescription;
	@FXML ImageView imageViewMain;
	@FXML TableView<RentalRecord> tableViewRecords;
	@FXML TableColumn<RentalRecord,String> tableColumnId;
	@FXML TableColumn<RentalRecord,String> tableColumnCustomer;
	@FXML TableColumn<RentalRecord,String> tableColumnRentDate;
	@FXML TableColumn<RentalRecord,String> tableColumnEstimatedReturnDate;
	@FXML TableColumn<RentalRecord,String> tableColumnActualReturnDate;
	@FXML TableColumn<RentalRecord,Double> tableColumnRentalFee;
	@FXML TableColumn<RentalRecord,Double> tableColumnLateFee;
	@FXML Button buttonReturn;
	@FXML Button buttonMaintenance;
	@FXML Button buttonComplete;
	
	VehicleItemController vehicleItemController;

	RentalVehicle rentalVehicle;
	List<RentalRecord> rentalRecords;
	ObservableList<RentalRecord> observableListRecords;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		buttonRent.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setButtonRentAction();
			}
		});
		
		buttonReturn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setButtonReturnAction();
			}
		});
		
		buttonMaintenance.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				setButtonMaintenanceAction(rentalVehicle);
			}
		});
		
		buttonComplete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setButtonCompleteAction(rentalVehicle);
			}
		});
	}

	public void initial(RentalVehicle rentalVehicle) {
		this.rentalVehicle=rentalVehicle;
		this.rentalRecords=RentalRecordDao.getAllRecordByVehicle(rentalVehicle);

		labelMake.setText(rentalVehicle.getMake());
		textAreaDescription.setText(rentalVehicle.getManuY()+" "+rentalVehicle.getMake()+" "+rentalVehicle.getMake()+"\n\n"+rentalVehicle.getDescription());
		imageViewMain.setImage(new Image("file://../images/"+rentalVehicle.getImageName()));
		observableListRecords=FXCollections.observableArrayList(rentalRecords);

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("recordId"));
		tableColumnCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
		tableColumnRentDate.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
		tableColumnEstimatedReturnDate.setCellValueFactory(new PropertyValueFactory<>("estimatedReturnDate"));
		tableColumnActualReturnDate.setCellValueFactory(new PropertyValueFactory<>("actualReturnDate"));
		tableColumnRentalFee.setCellValueFactory(new PropertyValueFactory<>("rentalFee"));
		tableColumnLateFee.setCellValueFactory(new PropertyValueFactory<>("lateFee"));

		tableViewRecords.setItems(observableListRecords);
		
		setButtonIsDisable(rentalVehicle);
	}
	
	private void setButtonRentAction() {
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
			stage.setTitle("Book");
			stage.setScene(new Scene(vbox));
			stage.setResizable(false);
			stage.show();
			bookRentalVehicleItemController.setRentalVehicleDetailController(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setButtonReturnAction() {
		URL location=getClass().getResource("/view/BookRentalVehicleItem.fxml");
		FXMLLoader fxmlLoader=new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		try {
			VBox vbox = fxmlLoader.load();
			BookRentalVehicleItemController bookRentalVehicleItemController=fxmlLoader.getController();
			bookRentalVehicleItemController.returnRentalVehicle(rentalVehicle);
			bookRentalVehicleItemController.setRentalVehicleDetailController(this);
			Stage stage=new Stage();
			bookRentalVehicleItemController.setStage(stage);
			stage.setTitle("Return");
			stage.setScene(new Scene(vbox));
			stage.setResizable(false);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void setButtonCompleteAction(RentalVehicle rentalVehicle) {
		try {
			if(rentalVehicle.getVehicleType().equals("van"))
				((Van)rentalVehicle).completeMaintenance(new DateTime(new DateTime().getTime()));
			else if(rentalVehicle.getVehicleType().equals("car"))
				((Car)rentalVehicle).completeMaintenance(new DateTime(new DateTime().getTime()));
			Alert alert=new Alert(AlertType.INFORMATION);
			alert.setTitle("success");
			alert.setContentText("complete maintenance");
			alert.showAndWait();
			
		} catch (CompleteMaintenanceException|PerformMaintenanceException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(e.getMessage());
			alert.setContentText(e.toString());
			alert.showAndWait();
		}finally {
			vehicleItemController.refresh();
		}
	}
	
	protected void setButtonMaintenanceAction(RentalVehicle rentalVehicle2) {
		try {
			if(rentalVehicle.getVehicleType().equals("van"))
				((Van)rentalVehicle).performMaintenance();
			else if(rentalVehicle.getVehicleType().equals("car"))
				((Car)rentalVehicle).performMaintenance();
			Alert alert=new Alert(AlertType.INFORMATION);
			alert.setTitle("success");
			alert.setContentText("perform maintenance success");
			alert.showAndWait();	
		} catch (PerformMaintenanceException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(e.getMessage());
			alert.setContentText(e.toString());
			alert.showAndWait();
		}finally {
			vehicleItemController.refresh();
		}
	}
	
	
	public void setVehicleItemController(VehicleItemController vehicleItemController) {
		this.vehicleItemController = vehicleItemController;
	}
	
	public void refresh() {
		this.rentalRecords=RentalRecordDao.getAllRecordByVehicle(rentalVehicle);
		observableListRecords.clear();
		observableListRecords.addAll(rentalRecords);
		setButtonIsDisable(rentalVehicle);
		vehicleItemController.refresh();
	}
	
	private void setButtonIsDisable(RentalVehicle rentalVehicle) {
		if(rentalVehicle.getStatus().equals(VehicleStatus.AVAILABLE)) {
			buttonRent.setDisable(false);
		}else {
			buttonRent.setDisable(true);
		}
		if(rentalVehicle.getStatus().equals(VehicleStatus.BEING_RENTED)) {
			buttonReturn.setDisable(false);
		}else {
			buttonReturn.setDisable(true);
		}
		
		if(rentalVehicle.getVehicleType().equals(VehicleType.VAN)) {
			if(rentalVehicle.getStatus().equals(VehicleStatus.VanStatus.UNDER_MAINTENANCE)){
				buttonMaintenance.setDisable(true);
				buttonComplete.setDisable(false);
			}else if(rentalVehicle.getStatus().equals(VehicleStatus.VanStatus.BEING_RENTED)){
				buttonMaintenance.setDisable(true);
				buttonComplete.setDisable(true);
			}else if(rentalVehicle.getStatus().equals(VehicleStatus.VanStatus.AVAILABLE)) {
				buttonMaintenance.setDisable(false);
				buttonComplete.setDisable(true);
			}
		}else if(rentalVehicle.getVehicleType().equals(VehicleType.CAR)) {
			if(rentalVehicle.getStatus().equals(VehicleStatus.CarStatus.UNDER_MAINTENANCE)){
				buttonMaintenance.setDisable(true);
				buttonComplete.setDisable(false);
			}else if(rentalVehicle.getStatus().equals(VehicleStatus.CarStatus.BEING_RENTED)){
				buttonMaintenance.setDisable(true);
				buttonComplete.setDisable(true);
			}else if(rentalVehicle.getStatus().equals(VehicleStatus.CarStatus.AVAILABLE)) {
				buttonMaintenance.setDisable(false);
				buttonComplete.setDisable(true);
			}
		}
	}
}
