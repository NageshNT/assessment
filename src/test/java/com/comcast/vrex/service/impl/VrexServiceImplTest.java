package com.comcast.vrex.service.impl;

import com.comcast.vrex.config.HistoryTracker;
import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.model.VrexResponsePayload;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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

		VrexResponsePayload response = vrexService.processCommandsByState("alabama",commands);
		assertNotNull(response.getStartProcessTime());
		assertEquals(response.getMostFrequentCommand(),"CRIMINAL MINDS");

		assertNotNull(response.getStopProcessTime());

		HistoryTracker.getTopStateCommandsMap().clear();
		HistoryTracker.getTopNationCommandsMap().clear();

	}


	@Test
	public void testProcessCommandsByStateCaseSensitivyHistory() {
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

		VrexResponsePayload response = vrexService.processCommandsByState("alabama",commands);
		assertNotNull(response.getStartProcessTime());
		assertEquals(response.getMostFrequentCommand(),"CNN");

		assertNotNull(response.getStopProcessTime());

		List<VrexCommand> commands2=new ArrayList<>();
		VrexCommand vrexCommand21=new VrexCommand("CNn","John");
		VrexCommand vrexCommand22=new VrexCommand("Turn off TV","Katie");
		VrexCommand vrexCommand24=new VrexCommand("Turn off TV","Marie");

		commands2.add(vrexCommand21);
		commands2.add(vrexCommand22);
		commands2.add(vrexCommand24);

		VrexResponsePayload response2 = vrexService.processCommandsByState("alabama",commands2);
		assertNotNull(response2.getStartProcessTime());
		assertEquals(response2.getMostFrequentCommand(),"CNN");

		HistoryTracker.getTopStateCommandsMap().clear();
		HistoryTracker.getTopStateCommandsMap().clear();


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
		commands.add("CNN");

		List<String> response = vrexService.topCommandsNationally(commands);

		assertEquals(response.size(),3);
		assertTrue(response.contains("TRUTV"));
		assertTrue(response.contains("CNN"));
		assertTrue(response.contains("HBO"));
	}


}
