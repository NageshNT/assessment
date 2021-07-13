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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
	public ResponseEntity  handleVrexRequest(@RequestBody Optional<Map<String, List<VrexCommand>>>  vrexRequest) {
		List<String> allCommands = Collections.synchronizedList(new ArrayList());
		Map<String, Object> vrexResponse = new ConcurrentHashMap<>();
		if (!vrexRequest.isPresent() ) {
			return new ResponseEntity<>(vrexResponse, HttpStatus.BAD_REQUEST);

		}
		log.info("Executing top commands by state.");
		try {
			synchronized (this) {
				Map<String, VrexResponsePayload> stateResponse = vrexRequest.get()
						.entrySet().parallelStream()
						.collect(Collectors.toMap(e -> e.getKey(),
								e -> vrexService.processCommandsByState((List<VrexCommand>) e.getValue()))
						);
				vrexResponse.putAll(stateResponse);

				log.info("Processing completed for top commands by state.");

				 vrexRequest.get()
						.entrySet().parallelStream()
						 .forEach( e -> e.getValue().stream().forEach(x -> allCommands.add(x.getCommand())) );


				vrexResponse.put("topCommandsNationally”", vrexService.topCommandsNationally(allCommands));
			}
		}
		catch(Exception e){
			log.error("Exeption occured while processing request {}",e);
			throw e;
		}
		log.info("Processing completed for Vrex request.");

		return new ResponseEntity<>(vrexResponse, HttpStatus.OK);

	}

}
