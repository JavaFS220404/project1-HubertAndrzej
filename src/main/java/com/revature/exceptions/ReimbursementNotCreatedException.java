package com.revature.exceptions;

public class ReimbursementNotCreatedException extends RuntimeException {
	
	public ReimbursementNotCreatedException() {
		super();
	}
	
	public ReimbursementNotCreatedException(String message) {
		super(message);
	}

	public ReimbursementNotCreatedException(Throwable cause) {
		super(cause);
	}

	public ReimbursementNotCreatedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ReimbursementNotCreatedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
