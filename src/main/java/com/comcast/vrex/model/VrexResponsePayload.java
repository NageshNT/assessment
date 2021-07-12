package com.comcast.vrex.model;

import lombok.Getter;
import lombok.Setter;

/**
 * VrexCommand.java - model class for declaring the Command pojo
 * @author  Nagesh Chandra N.T
 * @version 1.0
 */
@Getter
@Setter
public class VrexResponsePayload {

	/**
	 * Field that holds the String equivalent of most frequently used command
	 */
	private String mostFrequentCommand;

	/**
	 * Field that holds the timestamp for when processing began for
	 * given state
	 */
	private Long startProcessTime;

	/**
	 * Field that holds the timestamp for when processing end for
	 * given state
	 */
	private Long stopProcessTime;
	
}
