package com.comcast.vrex.service;

import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.model.VrexResponsePayload;

import java.util.List;

public interface VrexService {
	
	
	VrexResponsePayload processCommandsByState(List<VrexCommand> commands);
	
	 List<String> topCommandsNationally(List<String> commands);
}