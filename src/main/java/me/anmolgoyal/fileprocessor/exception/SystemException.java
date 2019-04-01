package me.anmolgoyal.fileprocessor.exception;


public class SystemException extends RuntimeException {
	
	private static final long serialVersionUID = -1961522112511805799L;

	private final String userMessage;
	
	public SystemException(String message, Throwable cause, String userMessage) {
		super(message, cause);
		this.userMessage = userMessage;
	}


	public SystemException(String userMessage,Throwable cause) {
		super(cause);
		this.userMessage = userMessage;
	}

	public String getUserMessage() {
		return userMessage;
	}
	
}
