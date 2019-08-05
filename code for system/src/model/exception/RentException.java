package model.exception;

public class RentException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int exceptionCode;
	
	public RentException(int exceptionCode) {
		this.exceptionCode=exceptionCode;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage()+"\n"+exceptionCode;
	}
	
	@Override
	public void printStackTrace() {
		System.err.println("exception code is:"+exceptionCode);
		super.printStackTrace();
	}
}
