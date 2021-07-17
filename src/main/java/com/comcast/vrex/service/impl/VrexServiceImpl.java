package com.comcast.vrex.service.impl;

import com.comcast.vrex.model.TopCommandFrequencyTracker;
import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.model.VrexResponsePayload;
import com.comcast.vrex.service.VrexService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.comcast.vrex.config.HistoryTracker.topNationCommandsMap;
import static com.comcast.vrex.config.HistoryTracker.topStateCommandsMap;

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
@Getter
public class VrexServiceImpl implements VrexService {

	@Override
	public VrexResponsePayload processCommandsByState(String state,List<VrexCommand> commands) {
		VrexResponsePayload responsePayload=new VrexResponsePayload();
		responsePayload.setStartProcessTime(System.currentTimeMillis());
		try{
			TopCommandFrequencyTracker topStateCommand = calculateMostFrequentCommand(state,commands);
			responsePayload.setMostFrequentCommand(topStateCommand.getCommand());
			log.error("Completed calculating top command for state: {}, topCommand: {}",state,topStateCommand.getCommand());
		}
		catch(Exception e){
			log.error("Error occured while processing commands for state: {}, Error: {}",state,e);
			throw e;
		}
		responsePayload.setStopProcessTime(System.currentTimeMillis());
		return responsePayload;
	}

	private TopCommandFrequencyTracker calculateMostFrequentCommand(String state,List<VrexCommand> commands) {
		List<String> commandValues=commands.stream()
				.filter(e -> e.getCommand() != null)
				.map(e -> e.getCommand()).collect(Collectors.toList());
		TopCommandFrequencyTracker currentStateTop = findMostRepeated(commandValues);
		TopCommandFrequencyTracker historyStateTop = topStateCommandsMap.get(state);
		if(null !=historyStateTop  && historyStateTop.getFrequency() > currentStateTop.getFrequency()){
			currentStateTop.setFrequency(currentStateTop.getFrequency()+historyStateTop.getFrequency());
			topStateCommandsMap.put(state, currentStateTop);
			return historyStateTop;
		}
		topStateCommandsMap.put(state, currentStateTop);
		return currentStateTop;
	}

	/**
	 * Processes given list of commands for  returns the most frequently occurring
	 *
	 * @param  commandValues  List of all the VrexCommands that hold the information of the command and the speaker
	 * @return  String  response payload with the most frequently used command
	 */
	private TopCommandFrequencyTracker findMostRepeated(List<String> commandValues){
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
		return new TopCommandFrequencyTracker(max.getKey().toUpperCase(),max.getValue());
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
		Set<TopCommandFrequencyTracker> curentNationTop = new HashSet<>();
		for(int i=0;i<k;i++) {
			TopCommandFrequencyTracker val = findMostRepeated(input);
			curentNationTop.add(val);
			removeAll(input, val.getCommand());

		}

		List<String> results=new ArrayList<>();
		if(null != topNationCommandsMap && topNationCommandsMap.size() != 0) {
			curentNationTop.addAll(topNationCommandsMap);
			TopCommandFrequencyTracker first = new TopCommandFrequencyTracker();
			TopCommandFrequencyTracker second = new TopCommandFrequencyTracker();
			TopCommandFrequencyTracker third = new TopCommandFrequencyTracker();

			for (TopCommandFrequencyTracker command : curentNationTop) {
				if (command.getFrequency() > first.getFrequency()) {
					third = second;
					second = first;
					first = command;
				} else if (command.getFrequency() > second.getFrequency()) {
					third = second;
					second = command;
				} else if (command.getFrequency() > third.getFrequency())
					third = command;
			}
			curentNationTop.clear();
			curentNationTop.add(first);
			curentNationTop.add(second);
			curentNationTop.add(third);
			updateTopNationalMap(curentNationTop);


		}
		curentNationTop.stream().forEach(e -> results.add(e.getCommand()));

		return results;
	}

	private void updateTopNationalMap(Set<TopCommandFrequencyTracker> curentNationTop) {

		for (TopCommandFrequencyTracker s : topNationCommandsMap) {

			for (TopCommandFrequencyTracker c : curentNationTop) {

				if (s.getCommand().toUpperCase().equals(c.getCommand())) {
					s.setFrequency(s.getFrequency() + c.getFrequency());
				} else {
					topNationCommandsMap.add(c);
				}
			}
		}
	}


	void removeAll(List<String> list, String element) {
		list.removeIf(n -> Objects.nonNull(n) && Objects.equals(n.toUpperCase(), element.toUpperCase()));
	}
}
