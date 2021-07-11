package com.comcast.vrex.controller;

import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.service.VrexService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@AllArgsConstructor
@RestController
public class VrexController {

	private final VrexService vrexService;

	@PostMapping(value = "/commands", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity  handleVrexRequest(@RequestBody Map<String, List<VrexCommand>>  vrexRequest) {
		List<String> allCommands = new ArrayList<>();
		Map<String, Object> vrexResponse = new HashMap<>();

		if (null != vrexRequest ) {
		    Iterator it = vrexRequest.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
				vrexResponse.put((String) pair.getKey(), vrexService.processCommandsByState((List<VrexCommand>) pair.getValue()));
				for(VrexCommand command: (List<VrexCommand>) pair.getValue()) {
					allCommands.add(command.getCommand());
				}
				it.remove();
		    }
		}
		vrexResponse.put("topCommandsNationally”",vrexService.topCommandsNationally(allCommands));
		return new ResponseEntity<>(vrexResponse, HttpStatus.OK);

	}

}
