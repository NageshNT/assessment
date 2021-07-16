package com.comcast.vrex.service.impl;

import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.model.VrexResponsePayload;
import com.comcast.vrex.service.VrexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Service Implementation class for handling processCommandsByState and topCommandsNationally requests
 * @see @Vrex
 *
 * @author Nagesh Chandra N.T.
 * @version 1.0
 *
 */

@Service
@Slf4j
public class VrexServiceImpl implements VrexService {
	
	
	@Override
	public VrexResponsePayload processCommandsByState(List<VrexCommand> commands) {
		VrexResponsePayload responsePayload=new VrexResponsePayload();
		responsePayload.setStartProcessTime(System.currentTimeMillis());
		try{
			responsePayload.setMostFrequentCommand(calculateMostFrequentCommand(commands));
		}
		catch(Exception e){
			log.error("Error occured while processing commands by state: {}",e);
			throw e;
		}
		responsePayload.setStopProcessTime(System.currentTimeMillis());
		return responsePayload;
	}

	private String calculateMostFrequentCommand(List<VrexCommand> commands) {
		List<String> commandValues=commands.stream()
				.filter(e -> e.getCommand() != null)
				.map(e -> e.getCommand()).collect(Collectors.toList());
		return findMostRepeated(commandValues);
		
	}

	/**
	 * Processes given list of commands for  returns the most frequently occurring
	 *
	 * @param  commandValues  List of all the VrexCommands that hold the information of the command and the speaker
	 * @return  String  response payload with the most frequently used command
	 */
	private String findMostRepeated(List<String> commandValues){
		Map<String, Integer> map = new LinkedCaseInsensitiveMap<>();

		for (String command: commandValues) {
			if(null != command){
				Integer val = map.get(command);
				map.put(command, val == null ? 1 : val + 1);
			}
		}

		Entry<String, Integer> max = null;

		for (Entry<String, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue())
				max = e;
		}

		return max.getKey();
	}

	@Override
	public List<String> topCommandsNationally(List<String> commands) {
		return findTopK(commands,3);
	}

	/**
	 * Processes given list of commands for  returns the top 3 most frequently occurring
	 *
	 * @param  input  List of all the VrexCommands that hold the information of the command and the speaker
	 * @param  k  integer representing the value of top howmany
	 * @return  String  response payload with the most frequently used command
	 */
	private List<String> findTopK(List<String> input, int k) {
		List<String> results=new ArrayList<>();
		for(int i=0;i<k;i++){
			String val = findMostRepeated(input);
			results.add(val);
			removeAll(input,val);
		}
		return results;
	}

	void removeAll(List<String> list, String element) {
		list.removeIf(n -> Objects.equals(n, element));
	}
}
