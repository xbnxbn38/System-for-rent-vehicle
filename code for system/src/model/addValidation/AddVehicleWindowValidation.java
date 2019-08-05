package model.addValidation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;



import model.dao.RentalVehicleDao;
import model.exception.InputException;

public class AddVehicleWindowValidation 
{
	public static void validateManuYear(String number) throws InputException 
	{
		try {
			if(number.matches("^[+]{0,1}(\\d+)$")) 
			{
				if(Long.parseLong(number)>2147483647)				
				{
					throw new InputException("Manu Year.: value out of range");
				}
			}
			if(!number.matches("^[+]{0,1}(\\d+)$")) 
			{
				throw new InputException("Manu Year.: value type mismatch");
			}
			
		}catch(NumberFormatException e) {
			throw new InputException("Manu Year.: value out of range");
		}
	}
	
	public static void validateMake(String make) throws InputException 
	{
		if(make.isEmpty() || make.indexOf(" ")==0)  
		{
			throw new InputException("Make: invalid format");
		}
	}
	
	public static void validateModel(String model) throws InputException 
	{
		if(model.isEmpty() || model.indexOf(" ")==0) 
		{
			throw new InputException("Suburb: invalid format");
		}
	}
	
	public static void validateDescription(String des) throws InputException 
	{
		if(des.length() < 10 || des.length() > 100) 
		{
			throw new InputException("Descrption: invalid length");
		}
	}
	
	public static void validateImage(File file) throws InputException, IOException 
	{
		if(!".PNG".equalsIgnoreCase(file.getName().substring(file.getName().lastIndexOf(".")))) 
		{
			throw new InputException("Invalid Image Format");
		}
		
	}
	
	public static void isTypeSelected(String type) throws InputException 
	{
		if(type.equals("Select a type"))
		{
			throw new InputException("Please select a type");
		}
	}
	
	public static void isSeatSelected(Boolean selected) throws InputException 
	{
		if(!selected)
		{
			throw new InputException("Please select Number of Seat");
		}
	}
	
	public static void isDateSelected(LocalDate localDate) throws InputException 
	{
		if(localDate == null)
		{
			throw new InputException("Please select the Last Maintenance Date");
		}
	}
	
	public static void isDuplicatedAddress(int manuYear, String make, String model) throws InputException 
	{
		if(RentalVehicleDao.findByAddress(manuYear, make, model))
		{
			throw new InputException("The Vehicle already exists, please try another Vehicle");
		}
	}
}
