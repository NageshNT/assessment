package com.comcast.vrex.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VrexResponsePayload {

	private String mostFrequentCommand;
	private Long startProcessTime;
	private Long stopProcessTime;
	
}
