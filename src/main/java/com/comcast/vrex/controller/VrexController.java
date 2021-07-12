package com.comcast.vrex.controller;

import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.model.VrexResponsePayload;
import com.comcast.vrex.service.VrexService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Controller that exposes end point /commands to process a Vrex application
 *
 * @author Nagesh Chandra N.T.
 * @version 1.0
 *
 */

@AllArgsConstructor
@RestController
@Slf4j
public class VrexController {

	private final VrexService vrexService;

	@PostMapping(value = "/commands", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity  handleVrexRequest(@RequestBody Map<String, List<VrexCommand>>  vrexRequest) {
		List<String> allCommands = new ArrayList<>();
		Map<String, Object> vrexResponse = new HashMap<>();

		if (null != vrexRequest ) {
		    log.info("Executing top commands by state.");
		    try {
				Iterator it = vrexRequest.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					VrexResponsePayload stateResponse = vrexService.processCommandsByState((List<VrexCommand>) pair.getValue());
					vrexResponse.put((String) pair.getKey(), stateResponse);
					log.info("Total time taken for processing state: {} is :{} nanos", pair.getKey(), stateResponse.getStopProcessTime() - stateResponse.getStartProcessTime());
					for (VrexCommand command : (List<VrexCommand>) pair.getValue()) {
						allCommands.add(command.getCommand());
					}
					it.remove();
				}
				vrexResponse.put("topCommandsNationally”",vrexService.topCommandsNationally(allCommands));
			}
		    catch(Exception e){
		    	log.error("Exeption occured while processing request {}",e);
		    	throw e;
			}
			log.info("Processing completed for Vrex request.");

		}
		return new ResponseEntity<>(vrexResponse, HttpStatus.OK);

	}

}
