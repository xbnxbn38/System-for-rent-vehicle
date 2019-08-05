package controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.constant.VehicleStatus;
import model.dao.CarDao;
import model.dao.VanDao;
import model.dao.RentalVehicleDao;
import model.dao.RentalRecordDao;
import model.entity.Car;
import model.entity.Van;
import model.entity.RentalVehicle;
import model.entity.RentalRecord;
import model.util.AlertHelper;
import model.util.DateTime;

public class MainController implements Initializable {

	@FXML
	ScrollPane scrollPaneVehicleList;

	@FXML
	TilePane tilePaneVehicleItemList;

	@FXML
	MenuItem menuItemExport;

	@FXML
	MenuItem menuItemImport;

	@FXML
	MenuItem menuItemQuit;
	
	@FXML
	MenuItem menuItemAdd;

	@FXML
	RadioButton radioButtonAll;

	@FXML
	RadioButton radioButtonCar;

	@FXML
	RadioButton radioButtonVan;

	@FXML
	ChoiceBox<String> choiceBoxSeatNumber;

	@FXML
	ChoiceBox<String> choiceBoxStatus;

	@FXML
	TextField textFieldModel;

	final ToggleGroup group = new ToggleGroup();

	Stage stage;
	
	List<RentalVehicle> rentalVehicles;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rentalVehicles = RentalVehicleDao.getAllRentalVehicle();
		radioButtonAll.setUserData("(1=1)");
		radioButtonAll.setToggleGroup(group);
		radioButtonCar.setUserData("(vehicle_type='car')");
		radioButtonCar.setToggleGroup(group);
		radioButtonVan.setUserData("(vehicle_type='van')");
		radioButtonVan.setToggleGroup(group);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (group.getSelectedToggle() != null) {
					String seatsNumberCondition = "(seats_number='" + choiceBoxSeatNumber.getValue() + "')";
					if (choiceBoxSeatNumber.getValue().equals("All")) {
						seatsNumberCondition = "(1=1)";
					}
					String statusCondition = "(vehicle_status='" + choiceBoxStatus.getValue() + "')";
					if (choiceBoxStatus.getValue().equals("All")) {
						statusCondition = "(1=1)";
					}

					String vehicleTypeCondition = (String) group.getSelectedToggle().getUserData();

					String modelCondition = "(model like '%" + textFieldModel.getText() + "%')";
					if (textFieldModel.getText() == null || textFieldModel.getText().trim().equals("")) {
						modelCondition = "(1=1)";
					}

					rentalVehicles = RentalVehicleDao
							.getAllRentalVehicleWithConditon(vehicleTypeCondition + " and " + seatsNumberCondition
									+ " and " + statusCondition + " and " + modelCondition);
					initial(stage);

				}
			}
		});

		choiceBoxSeatNumber.setItems(FXCollections.observableArrayList("All", "4", "7", "15"));
		choiceBoxSeatNumber.setValue("All");
		choiceBoxSeatNumber.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String vehicleTypeCondition = group.getSelectedToggle().getUserData().toString();

				String seatsNumberCondition = newValue.equals("All") ? "(1=1)"
						: "(seats_number='" + newValue + "')";

				String statusCondition = "(vehicle_status='" + choiceBoxStatus.getValue() + "')";
				if (choiceBoxStatus.getValue().equals("All")) {
					statusCondition = "(1=1)";
				}

				String modelCondition = "(model like '%" + textFieldModel.getText() + "%')";
				if (textFieldModel.getText() == null || textFieldModel.getText().trim().equals("")) {
					modelCondition = "(1=1)";
				}
				rentalVehicles = RentalVehicleDao.getAllRentalVehicleWithConditon(vehicleTypeCondition + " and "
						+ seatsNumberCondition + " and " + statusCondition + " and " + modelCondition);
				initial(stage);

			}
		});

		choiceBoxStatus.setItems(FXCollections.observableArrayList("All", VehicleStatus.AVAILABLE,
				VehicleStatus.BEING_RENTED, VehicleStatus.UNDER_MAINTENANCE));
		choiceBoxStatus.setValue("All");
		choiceBoxStatus.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String seatsNumberCondition = "(seats_number='" + choiceBoxSeatNumber.getValue() + "')";
				if (choiceBoxSeatNumber.getValue().equals("All")) {
					seatsNumberCondition = "(1=1)";
				}

				String vehicleTypeCondition = group.getSelectedToggle().getUserData().toString();

				String statusCondition = "(vehicle_status='" + newValue + "')";
				if (newValue.equals("All")) {
					statusCondition = "(1=1)";
				}

				String modelCondition = "(suburb like '%" + textFieldModel.getText() + "%')";
				if (textFieldModel.getText() == null || textFieldModel.getText().trim().equals("")) {
					modelCondition = "(1=1)";
				}

				rentalVehicles = RentalVehicleDao.getAllRentalVehicleWithConditon(vehicleTypeCondition + " and "
						+ seatsNumberCondition + " and " + statusCondition + " and " + modelCondition);
				initial(stage);

					
			}
		});

		textFieldModel.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String vehicleTypeCondition = group.getSelectedToggle().getUserData().toString();

				String seatsNumberCondition = "(seats_number='" + choiceBoxSeatNumber.getValue() + "')";
				if (choiceBoxSeatNumber.getValue().equals("All")) {
					seatsNumberCondition = "(1=1)";
				}

				String statusCondition = "(vehicle_status='" + choiceBoxStatus.getValue() + "')";
				if (choiceBoxStatus.getValue().equals("All")) {
					statusCondition = "(1=1)";
				}

				String modelCondition = "(model like '%" + newValue + "%')";
				if (textFieldModel.getText() == null || newValue.trim().equals("")) {
					modelCondition = "(1=1)";
				}

				rentalVehicles = RentalVehicleDao.getAllRentalVehicleWithConditon(vehicleTypeCondition + " and "
						+ seatsNumberCondition + " and " + statusCondition + " and " + modelCondition);
				initial(stage);

			}
		});
		menuItemImport.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setImportAction();
			}
		});
		menuItemExport.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setExportAction();
			}
		});
		menuItemQuit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
		menuItemAdd.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				addVehicleAction();
			}
		}); 
	}

	protected void setExportAction() {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("./"));
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setInitialFileName("export_data");
			Stage s = new Stage();
			File file = fileChooser.showSaveDialog(s);
			if (file == null)
				return;
			if (file.exists()) {
				file.delete();
			}
			
			BufferedOutputStream outputStream = null;
			outputStream = new BufferedOutputStream(new FileOutputStream(file));
			////////////////////////////////////////////////////////
			List<RentalVehicle> rentalVehicles = RentalVehicleDao.getAllRentalVehicleForExport();
			for(int i = 0; i<rentalVehicles.size(); i++) {
					String temp = rentalVehicles.get(rentalVehicles.size()-i-1).getVehicleId()+":"+rentalVehicles.get(rentalVehicles.size()-i-1).getManuY()+":"+rentalVehicles.get(rentalVehicles.size()-i-1).getMake()+":"+
							rentalVehicles.get(rentalVehicles.size()-i-1).getModel()+":"+rentalVehicles.get(rentalVehicles.size()-i-1).getVehicleType()+":"+rentalVehicles.get(rentalVehicles.size()-i-1).getSeatsNum()+":"+
							rentalVehicles.get(rentalVehicles.size()-i-1).getStatus()+":"+rentalVehicles.get(rentalVehicles.size()-i-1).getImageName()+":"+rentalVehicles.get(rentalVehicles.size()-i-1).getDescription();
					outputStream.write(temp.getBytes("UTF-8"));
					String newLine = System.getProperty("line.separator");
					outputStream.write(newLine.getBytes());
					
					List<RentalRecord> rentalRecords = RentalRecordDao.getAllRecordByVehicle(rentalVehicles.get(rentalVehicles.size()-i-1));
					for(int j = 0; j<rentalRecords.size(); j++) {
						String reDetail = rentalRecords.get(j).getDetail();
						outputStream.write(reDetail.getBytes("UTF-8"));
						String newRe = System.getProperty("line.separator");
						outputStream.write(newRe.getBytes());
					}
			}
			outputStream.flush();
			outputStream.close();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setContentText("export success");

			alert.showAndWait();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warnning");
			alert.setHeaderText(e.toString());
			alert.setContentText(e.getMessage());

			alert.showAndWait();
		}
	}

	protected void setImportAction() {
		BufferedReader bufferedReader = null;
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("./"));

			ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(extensionFilter);
			Stage s = new Stage();
			File file = fileChooser.showOpenDialog(s);
			try {
				if (file == null) {
					throw new Exception("file is null");
				}
			}catch(Exception e) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error!", 
	                    "File is null");
	            return;
			}
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			List<Car> cars = new ArrayList<>();
			List<Van> vans = new ArrayList<>();
			List<RentalVehicle> rentalVehicles = new ArrayList<>();
			List<RentalRecord> rentalRecords = new ArrayList<>();
			String temp = null;
			if ((temp = bufferedReader.readLine()) != null && (temp.startsWith("C_") || temp.startsWith("V_"))) {
				
				while ((temp = bufferedReader.readLine()) != null && (temp.matches("^C_[0-9]*[A-Z]{2}:.*$")||temp.matches("^V_[0-9]*[A-Z]{2}:.*$"))) {
					String[] vehicles = temp.split(":");
					RentalVehicle rentalVehicle = new RentalVehicle();
					rentalVehicle.setVehicleId(vehicles[0]);
					rentalVehicle.setManuY(vehicles[1]);
					rentalVehicle.setMake(vehicles[2]);
					rentalVehicle.setModel(vehicles[3]);
					rentalVehicle.setVehicleType(vehicles[4]);
					rentalVehicle.setSeatsNum(Integer.parseInt(vehicles[5]));
					rentalVehicle.setStatus(vehicles[6]);
					if(vehicles[0].startsWith("C")) {
						rentalVehicle.setImageName(vehicles[7]);
						rentalVehicle.setDescription(vehicles[8]);
						rentalVehicles.add(rentalVehicle);
					}
					else {
						rentalVehicle.setImageName(vehicles[8]);
						rentalVehicle.setDescription(vehicles[9]);
						rentalVehicles.add(rentalVehicle);
					}
					if(vehicles[0].startsWith("C")) {
						Car car = new Car();
						car.setVehicleId(vehicles[0]);
						if(Integer.parseInt(vehicles[5])==4)
							car.setPerDayFee(78);
						if(Integer.parseInt(vehicles[5])==7)
							car.setPerDayFee(115);
						if(Integer.parseInt(vehicles[5])==15)
							car.setPerDayFee(235);
						cars.add(car);
					}
					else if(vehicles[0].startsWith("V")){
						Van van = new Van();
						van.setVehicleId(vehicles[0]);
						van.setLastMaintenance(new DateTime(vehicles[7]));
						van.setPerDayFee(235);
						vans.add(van);
					}
				}
				// record
				while ((temp = bufferedReader.readLine()) != null && (temp.matches("^C_[0-9]*[A-Z]{2}_.*$")||temp.matches("^V_[0-9]*[A-Z]{2}_.*$"))) {
					String[] vehicles = temp.split(":");
					RentalRecord rentalRecord = new RentalRecord();
					rentalRecord.setRecordId(vehicles[0]);
					RentalVehicle rentalVehicle = new RentalVehicle();
					rentalVehicle.setVehicleId(vehicles[0].substring(0, vehicles[0].indexOf("_", 3)));
					rentalRecord.setRentalVehicle(rentalVehicle);
					int index=vehicles[0].indexOf("_");
					index=vehicles[0].indexOf("_", index+1);
					index=vehicles[0].indexOf("_", index+1);
					rentalRecord.setCustomerId(vehicles[0].substring(vehicles[0].indexOf("CUS"), index));
					rentalRecord.setRentDate(new DateTime(vehicles[1]));
					rentalRecord.setEstimatedReturnDate(new DateTime(vehicles[2]));
					rentalRecord.setActualReturnDate(vehicles[3].equals("none") ? null : new DateTime(vehicles[3]));
					rentalRecord.setRentalFee(Double.valueOf(vehicles[4]));
					rentalRecord.setLateFee(Double.valueOf(vehicles[5]));
					rentalRecords.add(rentalRecord);
				}
			}
			CarDao.saveAll(cars);
			VanDao.saveAll(vans);
			RentalVehicleDao.saveAll(rentalVehicles);
			RentalRecordDao.saveAll(rentalRecords);

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setContentText("import success");

			alert.showAndWait();
			this.initial(stage);

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warnning");
			alert.setHeaderText(e.toString());
			alert.setContentText(e.getMessage());

			alert.showAndWait();
		}
	}

	protected void addVehicleAction() {
		URL location = getClass().getResource("/view/AddNewVehicle.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		Parent parent;
		try {
			parent = (Parent) fxmlLoader.load();
			Stage stage=new Stage();
			stage.setTitle("Add");
			stage.setScene(new Scene(parent));
			stage.setResizable(false);
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initial(Stage stage) {
		this.stage=stage;
		tilePaneVehicleItemList.getChildren().clear();
		if (rentalVehicles == null) {
			return;
		}
		for (int i = 0; i < rentalVehicles.size(); i++) {
			RentalVehicle rentalVehicle = rentalVehicles.get(i);
			URL location = getClass().getResource("/view/RentalVehicleItem.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			try {
				GridPane gridPaneRentalVehicleItem = fxmlLoader.load();
				VehicleItemController vehicleItemController = fxmlLoader.getController();
				vehicleItemController.initial(rentalVehicle);
				if (i == 0) {
					vehicleItemController.labelNew.setVisible(true);
				}
				tilePaneVehicleItemList.getChildren().add(gridPaneRentalVehicleItem);
				TilePane.setMargin(gridPaneRentalVehicleItem, new Insets(10));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
