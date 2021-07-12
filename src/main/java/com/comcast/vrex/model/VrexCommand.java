package com.comcast.vrex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * VrexCommand.java - model class for declaring the Command pojo
 * @author  Nagesh Chandra N.T
 * @version 1.0
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VrexCommand {

	/**
	 * Field that holds the String equivalent of the audio command issued
	 */
	private String command;


	/**
	 * Field that holds the String equivalent of the speaker that issued the audio command
	 */
	private String speaker;
	
}
