package com.comcast.vrex.service;

import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.model.VrexResponsePayload;

import java.util.List;

/**
 * Service class for handling processCommandsByState and topCommandsNationally requests
 *
 * @author Nagesh Chandra N.T.
 * @version 1.0
 *
 */
public interface VrexService {

	/**
	 * Processes commands for a given state and returns the most frequently
	 * used command by that state
	 *
	 * @param  commands  List of all the VrexCommands for a particular state that hold the information of the command and the speaker
	 * @return  VrexResponsePayload  response payload with the most frequently used command and start and stop processing times for a given state
	 */
	VrexResponsePayload processCommandsByState(List<VrexCommand> commands);


	/**
	 * Processes commands for the entire  nation and returns the top 3 most frequently
	 * used commands across the nation
	 *
	 * @param  commands  List of all the VrexCommands for the entire nation t
	 * @return  List<String>  response payload with the top 3 most frequently used commands
	 */
	 List<String> topCommandsNationally(List<String> commands);
}