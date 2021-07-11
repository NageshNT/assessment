package com.comcast.vrex.service.impl;

import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.model.VrexResponsePayload;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class VrexServiceImplTest{

	VrexServiceImpl vrexService=new VrexServiceImpl();


	@Test
	public void testProcessCommandsByState() {
		List<VrexCommand> commands=new ArrayList<>();
		VrexCommand vrexCommand1=new VrexCommand("Criminal Minds","John");
		VrexCommand vrexCommand2=new VrexCommand("Turn off TV","Katie");
		VrexCommand vrexCommand3=new VrexCommand("Criminal Minds","Paris");
		commands.add(vrexCommand1);
		commands.add(vrexCommand2);
		commands.add(vrexCommand3);

		VrexResponsePayload response = vrexService.processCommandsByState(commands);
		assertNotNull(response.getStartProcessTime());
		assertEquals(response.getMostFrequentCommand(),"Criminal Minds");

		assertNotNull(response.getStopProcessTime());
	}


	@Test
	public void testProcessCommandsByStateCaseSensitivy() {
		List<VrexCommand> commands=new ArrayList<>();
		VrexCommand vrexCommand1=new VrexCommand("CNn","John");
		VrexCommand vrexCommand2=new VrexCommand("Turn off TV","Katie");
		VrexCommand vrexCommand4=new VrexCommand("Turn off TV","Marie");
		VrexCommand vrexCommand3=new VrexCommand("cnn","Paris");
		VrexCommand vrexCommand5=new VrexCommand("CNN","Seth");

		commands.add(vrexCommand1);
		commands.add(vrexCommand2);
		commands.add(vrexCommand3);
		commands.add(vrexCommand4);
		commands.add(vrexCommand5);

		VrexResponsePayload response = vrexService.processCommandsByState(commands);
		assertNotNull(response.getStartProcessTime());
		assertEquals(response.getMostFrequentCommand(),"CNN");

		assertNotNull(response.getStopProcessTime());
	}

	@Test
	public void testTopCommandsNationally() {
		List<String> commands=new ArrayList<>();
		commands.add("CNN");
		commands.add("TRUTV");
		commands.add("HBO");
		commands.add("truTV");
		commands.add("CNN");
		commands.add("HBO");
		commands.add("BBC");
		commands.add("Cartoon");
		commands.add("Some TV");
		commands.add("Random TV");
		commands.add("Test TV");

		List<String> response = vrexService.topCommandsNationally(commands);

		assertEquals(response.size(),3);
		assertEquals(response.get(0),"CNN");
		assertEquals(response.get(1),"HBO");
		assertEquals(response.get(2),"truTV");
	}


}
