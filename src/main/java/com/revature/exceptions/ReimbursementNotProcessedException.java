package com.revature.exceptions;

public class ReimbursementNotProcessedException extends RuntimeException {
	
	public ReimbursementNotProcessedException() {
		super();
	}
	
	public ReimbursementNotProcessedException(String message) {
		super(message);
	}

	public ReimbursementNotProcessedException(Throwable cause) {
		super(cause);
	}

	public ReimbursementNotProcessedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ReimbursementNotProcessedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
