package model.exception;

public class InputException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private String reason;//declare 'reason' of String type for exception 
			   
	public InputException(String reason)
	{
		this.reason = reason;
    }
			
  	 // accessor method to get reason
	public String getReason() 
    {
		return reason;
	}
}
