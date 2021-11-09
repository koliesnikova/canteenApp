package sk.upjs.paz1c.storage;

public class EntityUndeletableException extends Exception {
	private static final long serialVersionUID = 2492998893229620606L;

	public EntityUndeletableException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityUndeletableException(String message) {
		super(message);
	}

}
