package com.buthdev.demo.exceptions;

public class AICommunicationException extends RuntimeException  {
	private static final long serialVersionUID = 1L;

	public AICommunicationException() {
		super("Communication error with AI");
	}

}
