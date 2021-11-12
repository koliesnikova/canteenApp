package sk.upjs.paz1c.storage;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4438931047690136219L;

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
		// implement warning dialog instead of showing exception message
	}

	public EntityNotFoundException(String message) {
		super(message);
	}
	
	

}
