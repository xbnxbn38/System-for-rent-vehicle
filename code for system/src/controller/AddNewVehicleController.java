package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javax.imageio.ImageIO;
import model.util.AlertHelper;
import model.util.DateTime;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import model.entity.Van;
import model.entity.RentalVehicle;
import model.entity.Car;
import model.addValidation.AddVehicleWindowValidation;
import model.dao.CarDao;
import model.dao.VanDao;
import model.dao.RentalVehicleDao;
import model.exception.InputException;


public class AddNewVehicleController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label typeValidLable;

    @FXML
    private Label mkValidLable;

    @FXML
    private Label mkInvalidLable;

    @FXML
    private Label modelValidLable;

    @FXML
    private Label modelInvalidLable;

    @FXML
    private Label lastValidLable;

    @FXML
    private Label lastInvalidLable;

    @FXML
    private Label seatNumLabel;

    @FXML
    private TextField imageName;

    @FXML
    private Button addBtn;

    @FXML
    private Label mYInvalidLable;

    @FXML
    private TextField manuYearBox;

    @FXML
    private TextField makeBox;

    @FXML
    private TextField desBox;

    @FXML
    private Label lengthReminder;

    @FXML
    private Label desValidLable;

    @FXML
    private Label desInvalidLable;

    @FXML
    private Label charNumLabel;

    @FXML
    private TextField modelBox;

    @FXML
    private Label lastDateLable;

    @FXML
    private DatePicker dateBox;

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private Label mYValidLable;

    @FXML
    private ComboBox<String> seatNumBox;
    
    BufferedImage newImage;
    
    private boolean isseatNumVaild = false;
    
    File selectedFile;

    ObservableList<String> type = FXCollections.observableArrayList("Car", "Van");
    
    ObservableList<String> seatNum = FXCollections.observableArrayList("4", "7", "15");
    
    @FXML
    void addVehicle(MouseEvent event) throws Exception {
    	if(!validateInput())
			return;
    	if(selectedFile != null && 
				(".PNG".equalsIgnoreCase(selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".")))&&
						(ImageIO.read(selectedFile).getHeight()==370 && ImageIO.read(selectedFile).getWidth()==750)))
		{
			File file = new File("images");
			File[] images = file.listFiles();
			for(int i = 0; i<images.length; i++)
			{
				File temp = images[i];
				if(temp.getName().compareTo(selectedFile.getName())==0)
				{
					AlertHelper.showAlert(Alert.AlertType.ERROR, "Error!", 
		                    "Duplicated image name, please rename image");
		            return;
				}
			}
			copyImageToFolder();
		}
    	
    	try
		{
			AddVehicleWindowValidation.isDuplicatedAddress(Integer.parseInt(manuYearBox.getText()), makeBox.getText(), modelBox.getText());
			
		}catch(InputException ex) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, "Error!", 
                    ex.getReason());
            return;
		}
    	//ID
    	String vehicleID = null;
    	String image;
    	if(selectedFile != null) 
		{
			image = selectedFile.getName();
		}
		else 
		{
			image = "ImageNoFound.PNG";
		}
    	//////////////insert
    	RentalVehicle newVehicle = null;
    	if(typeBox.getSelectionModel().selectedItemProperty().getValue().compareTo("Van") == 0)
		{
    		vehicleID = "V_"+ manuYearBox.getText()+makeBox.getText().substring(0, 1)+modelBox.getText().substring(0, 1);
			String date = dateBox.getValue().toString().substring(8)+"/"+ dateBox.getValue().toString().substring(5, 7)+"/"+ dateBox.getValue().toString().substring(0, 4);
			DateTime last = new DateTime(date);
			newVehicle = new Van(vehicleID, manuYearBox.getText(), makeBox.getText(), modelBox.getText(), "van", last, desBox.getText(), image);	
			VanDao.save((Van)newVehicle);
			
		}
    	else if (typeBox.getSelectionModel().selectedItemProperty().getValue().compareTo("Car") == 0)
		{
    		vehicleID = "C_"+manuYearBox.getText()+manuYearBox.getText().substring(0, 1)+modelBox.getText().substring(0, 1);
    		newVehicle = new Car(vehicleID, manuYearBox.getText(), makeBox.getText(), modelBox.getText(), "car", Integer.parseInt(seatNumBox.getValue()), 
    				desBox.getText(), image);
    		CarDao.save((Car)newVehicle);
		}
    	RentalVehicleDao.save(newVehicle);
    	AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Saved", 
        		"The vehicle has been added successful\nVehicle ID: "+vehicleID);
    }
    
    private void copyImageToFolder() 
	{
		String desLocation = "images/"+selectedFile.getName();
		try 
		{
			ImageIO.write(newImage, "PNG", new File(desLocation));
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

    @FXML
    void desTyped(KeyEvent event) {
    	desBox.setOnKeyReleased(e -> {
    		try 
    		{
    			AddVehicleWindowValidation.validateDescription(desBox.getText());
    			desValidLable.setOpacity(1);
				desInvalidLable.setOpacity(0);
    			
    		}catch(InputException ex) {
    			desValidLable.setOpacity(0);
				desInvalidLable.setOpacity(1);
    		}
		});
    	charNumLabel.setText("Length: "+(desBox.getText().length()));
    }

    @FXML
    void numSelected(MouseEvent event) {
    	seatNumBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() 
		{
			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) 
			{
				lastValidLable.setOpacity(1);	
				isseatNumVaild = true;
			}
		});
    }

    @FXML
    void mkTyped(KeyEvent event) {
    	makeBox.setOnKeyReleased(e -> {
			try 
			{
				AddVehicleWindowValidation.validateMake(makeBox.getText());
				mkValidLable.setOpacity(1);
				mkInvalidLable.setOpacity(0);
				
			}catch(InputException ex) {
				mkValidLable.setOpacity(0);
				mkInvalidLable.setOpacity(1);
			}
		});
    }

    @FXML
    void mYearTyped(KeyEvent event) {
    	manuYearBox.setOnKeyReleased(e -> {
			try {
				AddVehicleWindowValidation.validateManuYear(manuYearBox.getText());
				mYValidLable.setOpacity(1);
				mYInvalidLable.setOpacity(0);	
				
			}catch(InputException  ex) {
				mYValidLable.setOpacity(0);
			    mYInvalidLable.setOpacity(1);
			}
		});
    }

    @FXML
    void modelTyped(KeyEvent event) {
    	modelBox.setOnKeyReleased(e -> {
			try 
			{
				AddVehicleWindowValidation.validateModel(modelBox.getText());
				modelValidLable.setOpacity(1);
				modelInvalidLable.setOpacity(0);
				
			}catch(InputException ex) {
				modelValidLable.setOpacity(0);
				modelInvalidLable.setOpacity(1);
			}
		});
    }

    @FXML
    void typeSelected(MouseEvent event) {
    	 typeBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() 
    	 {             
    		 @Override            
    		 public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) 
    	 	 {                
    		   typeValidLable.setOpacity(1);   
    		   if(typeBox.getSelectionModel().selectedItemProperty().getValue().compareTo("Van")==0) 
    		   {
    			   lastDateLable.setOpacity(1);
    			   dateBox.setOpacity(1);
    			   dateBox.setDisable(false);
    			   seatNumBox.setOpacity(0);
    			   seatNumLabel.setOpacity(0);
    			   seatNumBox.setDisable(true);
    			   lastValidLable.setOpacity(0);	
    			   lastInvalidLable.setOpacity(0);	
    		   }
    		   else 
    		   {
    			   lastDateLable.setOpacity(0);
    			   dateBox.setOpacity(0);
    			   dateBox.setDisable(true);
    			   seatNumBox.setOpacity(1);
    			   seatNumLabel.setOpacity(1);
    			   seatNumBox.setDisable(false);
    			   if(seatNumBox.getValue().compareTo("Select a number")!=0) 
    			   {
    				   lastValidLable.setOpacity(1);	
    			   }
    			   else
    				   lastValidLable.setOpacity(0);	
    			   lastInvalidLable.setOpacity(0);	
    		   }
    	     }        
    	 });
    }

    @FXML
    void uploadImage(MouseEvent event) throws IOException {
    	imageName.setText("");
		FileChooser chooser = new FileChooser();
		selectedFile = chooser.showOpenDialog(null);  //file
		if(selectedFile != null) 
		{
			try 
			{
				AddVehicleWindowValidation.validateImage(selectedFile);
				imageName.setText(selectedFile.getName());
				newImage = ImageIO.read(selectedFile);
			
			}catch(InputException e) {
				if(e.getReason().equals("Invalid Image Size")) 
				{
					imageName.setText("Invalid Size");
				}
				if(e.getReason().equals("Invalid Image Format")) 
				{
					imageName.setText("Invalid Format");
				}
			}
		}
    }
    
    @FXML
    void initialize() 
    {
    	typeBox.setItems(type);
    	typeBox.setValue("Select a type");
    	seatNumBox.setItems(seatNum);
    	seatNumBox.setValue("Select a number");
    	dateBox.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) 
            {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) > 0 );
            }
        });   
    	lastDateLable.setOpacity(0);
		dateBox.setOpacity(0);
		seatNumBox.setOpacity(0);
		seatNumLabel.setOpacity(0);
		seatNumBox.setDisable(true);
    }

    private boolean validateInput()
	{
    	try 
		{
			AddVehicleWindowValidation.isTypeSelected(typeBox.getSelectionModel().selectedItemProperty().getValue());
			AddVehicleWindowValidation.validateManuYear(manuYearBox.getText());
			AddVehicleWindowValidation.validateMake(makeBox.getText());
			AddVehicleWindowValidation.validateModel(modelBox.getText());
			if(typeBox.getSelectionModel().selectedItemProperty().getValue().compareTo("Car")==0) 
			{
				AddVehicleWindowValidation.isSeatSelected(isseatNumVaild);
			}
			if(typeBox.getSelectionModel().selectedItemProperty().getValue().compareTo("Van")==0) 
			{
				AddVehicleWindowValidation.isDateSelected(dateBox.getValue());
			}
			AddVehicleWindowValidation.validateDescription(desBox.getText());
			if(selectedFile != null)
			{
				AddVehicleWindowValidation.validateImage(selectedFile);
			}
			
		}catch(InputException ex) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, "Error!", 
                    ex.getReason());
            return false;
		}catch(IOException ex) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, "Error!", 
                    ex.getMessage());
			return false;
		}
		return true;
	}
    
}
