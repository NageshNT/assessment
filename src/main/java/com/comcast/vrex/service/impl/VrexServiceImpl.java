package com.comcast.vrex.service.impl;

import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.model.VrexResponsePayload;
import com.comcast.vrex.service.VrexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.*;
import java.util.Map.Entry;

@Service
@Slf4j
public class VrexServiceImpl implements VrexService {
	
	
	@Override
	public VrexResponsePayload processCommandsByState(List<VrexCommand> commands) {
		VrexResponsePayload responsePayload=new VrexResponsePayload();
		responsePayload.setStartProcessTime(System.currentTimeMillis());
		responsePayload.setMostFrequentCommand(calculateMostFrequentCommand(commands));
		responsePayload.setStopProcessTime(System.currentTimeMillis());

		return responsePayload;
	}

	private String calculateMostFrequentCommand(List<VrexCommand> commands) {
		List<String> commandValues=new ArrayList<>();
		for(VrexCommand command: commands) {
			commandValues.add(command.getCommand());
		}
	return findMostRepeated(commandValues);
		
	}

	private String findMostRepeated(List<String> commandValues){
		Map<String, Integer> map = new LinkedCaseInsensitiveMap<>();

		for (String command: commandValues) {
			Integer val = map.get(command);
			map.put(command, val == null ? 1 : val + 1);
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

	public List<String> findTopK(List<String> input, int k) {
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
