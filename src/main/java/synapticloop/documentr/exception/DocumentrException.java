package synapticloop.documentr.exception;

/*
 * Copyright (c) 2016 - 2017 Synapticloop.
 * 
 * All rights reserved.
 * 
 * This code may contain contributions from other parties which, where 
 * applicable, will be listed in the default build file for the project 
 * ~and/or~ in a file named CONTRIBUTORS.txt in the root of the project.
 * 
 * This source code and any derived binaries are covered by the terms and 
 * conditions of the Licence agreement ("the Licence").  You may not use this 
 * source code or any derived binaries except in compliance with the Licence.  
 * A copy of the Licence is available in the file named LICENSE.txt shipped with 
 * this source code or binaries.
 */

public class DocumentrException extends Exception {
	private static final long serialVersionUID = 3499859602264427962L;

	/**
	 * Instantiate a documentr exception
	 * 
	 * @param message the message for the exception
	 */
	public DocumentrException(String message) {
		super(message);
	}

	/**
	 * Instantiate a documentr exception
	 * 
	 * @param message the message for the exception
	 * @param throwable the root cause of the exception
	 */
	public DocumentrException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
