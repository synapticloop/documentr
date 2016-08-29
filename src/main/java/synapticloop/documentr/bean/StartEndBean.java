package synapticloop.documentr.bean;

/*
 * Copyright (c) 2016 Synapticloop.
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

/**
 * A simple POJO to hold the start and end positions for a string of characters
 * within a larger string of characters.
 * 
 * @author synapticloop
 *
 */
public class StartEndBean {

	private int start;
	private int end;

	/**
	 * Instantiate the bean with the start and end offset of the characters
	 * 
	 * @param start The start of string of characters
	 * @param end The end of the string of characters
	 */
	public StartEndBean(int start, int end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Get the start of the offset of the characters
	 * 
	 * @return the start offset of the characters
	 */
	public int getStart() { return this.start; }

	/**
	 * Get the end of the offset of the characters
	 * 
	 * @return the end offset of the characters
	 */
	public int getEnd() { return this.end; }

}
