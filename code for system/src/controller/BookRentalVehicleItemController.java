package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.constant.VehicleType;
import model.dao.RentalRecordDao;
import model.entity.Car;
import model.entity.Van;
import model.entity.RentalVehicle;
import model.entity.RentalRecord;
import model.util.DateTime;

public class BookRentalVehicleItemController implements Initializable{

	@FXML Button buttonConfirm;
	@FXML Button buttonCancel;
	@FXML DatePicker datePickerRentDate;
	@FXML DatePicker datePickerActualRenturnDate;
	@FXML DatePicker datePickerEstimatedRenturnDate;
	@FXML TextField textFieldLateFee;
	@FXML TextField textFieldRentFee;
	@FXML TextField textFieldVehicleId;
	@FXML GridPane gridPaneButtons;
	@FXML TextField textFieldRecordId;
	@FXML TextField textFieldCustomerId;
	
	private Stage stage;
	private RentalVehicleDetailController rentalVehicleDetailController;
	private VehicleItemController vehicleItemController;

	private RentalVehicle rentalVehicle;
	private RentalRecord record;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datePickerRentDate.setValue(LocalDate.now());
		
		final Callback<DatePicker, DateCell> dayCellFactory=new Callback<DatePicker, DateCell>() {

			@Override
			public DateCell call(DatePicker arg0) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item,boolean empty) {
						if(datePickerRentDate==null) {
							return;
						}
						if(item.isBefore(datePickerRentDate.getValue().plusDays(1))) {
							setDisable(true);
							setStyle("-fx-background-color:#EEEEEE");
						}
					}
				};
			}
		};
		
		datePickerEstimatedRenturnDate.setDayCellFactory(dayCellFactory);
		datePickerActualRenturnDate.setDayCellFactory(dayCellFactory);
		
		buttonConfirm.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				setButtonConfirmAction();
			}
		});
		
		buttonCancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				stage.close();
			}
		});
		datePickerActualRenturnDate.valueProperty().addListener(new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0, LocalDate arg1, LocalDate arg2) {
				if(record==null) {
					return;
				}
				record.setActualReturnDateAndFees(new DateTime(arg2.format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
				
				textFieldRentFee.setText(String.format("%.2f", record.getRentalFee()));
				textFieldLateFee.setText(String.format("%.2f", record.getLateFee()));
			}
		});
		textFieldVehicleId.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				setRecordId();
			}
		});
		textFieldCustomerId.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				setRecordId();
			}
		});
		datePickerRentDate.valueProperty().addListener(new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				setRecordId();
			}
		});
		
	}
	
	public void bookRentalVehicle(RentalVehicle rentalVehicle) {
		this.rentalVehicle=rentalVehicle;
		textFieldVehicleId.setText(rentalVehicle.getVehicleId());
		datePickerActualRenturnDate.setDisable(true);
		textFieldRecordId.setDisable(true);
		textFieldRentFee.setDisable(true);
		textFieldLateFee.setDisable(true);
		
	}
	
	public void returnRentalVehicle(RentalVehicle rentalVehicle) {
		this.rentalVehicle=rentalVehicle;
		record=RentalRecordDao.findLatestRecordByVehicle(rentalVehicle);
		textFieldRecordId.setText(record.getRecordId());
		textFieldRecordId.setDisable(true);
		textFieldVehicleId.setText(record.getRentalVehicle().getVehicleId());
		textFieldVehicleId.setDisable(true);
		textFieldCustomerId.setText(record.getCustomerId());
		textFieldCustomerId.setDisable(true);
		textFieldRentFee.setEditable(false);
		textFieldLateFee.setEditable(false);
		datePickerRentDate.setValue(LocalDate.parse(record.getRentDate().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		datePickerRentDate.setDisable(true);
		datePickerEstimatedRenturnDate.setValue(LocalDate.parse(record.getEstimatedReturnDate().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		datePickerEstimatedRenturnDate.setDisable(true);
		datePickerActualRenturnDate.setValue(LocalDate.now());
	}

	public void setStage(Stage stage) {
		this.stage=stage;
	}
	
	private void setRecordId() {
		if(textFieldVehicleId.getText()==null||datePickerRentDate.getValue()==null||textFieldCustomerId.getText()==null) {
			return;
		}
		textFieldRecordId.setText(rentalVehicle.getVehicleId()+"_"+textFieldCustomerId.getText()+"_"+new DateTime(datePickerRentDate.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy"))).getEightDigitDate());
	}
	
	public void setRentalVehicleDetailController(RentalVehicleDetailController rentalVehicleDetailController) {
		this.rentalVehicleDetailController = rentalVehicleDetailController;
	}

	public void setVehicleItemController(VehicleItemController vehicleItemController) {
		this.vehicleItemController = vehicleItemController;
	}
	
	protected void setButtonConfirmAction() {
		try {
			if(record==null) {
				RentalRecord record=new RentalRecord();
				record.setRecordId(textFieldRecordId.getText());
				record.setRentalVehicle(rentalVehicle);
				record.setRentDate(new DateTime(datePickerRentDate.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
				record.setEstimatedReturnDate(new DateTime(datePickerEstimatedRenturnDate.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
				record.setCustomerId(textFieldCustomerId.getText());

				
				if(rentalVehicle.getVehicleType().equals(VehicleType.CAR)) {
					((Car)rentalVehicle).rent(record.getCustomerId(), record.getRentDate(), record.getEstimatedReturnDate());
				}else if(rentalVehicle.getVehicleType().equals(VehicleType.VAN)) {
					((Van)rentalVehicle).rent(record.getCustomerId(), record.getRentDate(), record.getEstimatedReturnDate());
				}
				
				this.record=record;
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setTitle("success");
				alert.setContentText("rent success");
				alert.showAndWait();
			}else {
				record.setActualReturnDateAndFees(new DateTime(datePickerActualRenturnDate.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
				if(rentalVehicle.getVehicleType().equals(VehicleType.CAR)) {
					((Car)rentalVehicle).returnVehicle(record.getActualReturnDate());
				}else if(rentalVehicle.getVehicleType().equals(VehicleType.VAN)) {
					((Van)rentalVehicle).returnVehicle(record.getActualReturnDate());
				}
				//						RentalRecordDao.update(record);
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setTitle("success");
				alert.setContentText("return success");
				alert.showAndWait();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(e.getMessage());
			alert.setContentText(e.toString());
			alert.showAndWait();

		} finally {
			if(vehicleItemController!=null) {
				vehicleItemController.refresh();
			}
			if(rentalVehicleDetailController!=null) {
				rentalVehicleDetailController.refresh();
			}
		}
		
	}

}
