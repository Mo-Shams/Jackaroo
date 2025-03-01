package exception;

public abstract class CannotFieldException extends ActionException{
	public CannotFieldException () {
		super(); 
	}
	
	public CannotFieldException (String message) {
		super(message); 
	}
}
