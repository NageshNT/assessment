package com.comcast.vrex.controller;

import com.comcast.vrex.model.VrexCommand;
import com.comcast.vrex.service.VrexService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = VrexController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class VrexControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private VrexService vrexService;


	@Test
	public void testHandleVrexRequest()throws Exception {

		Map<String, List<VrexCommand>> vrexRequest = new HashMap<>();
		List<VrexCommand> commands=new ArrayList<>();
		VrexCommand vrexCommand1=new VrexCommand("Criminal Minds","John");
		VrexCommand vrexCommand2=new VrexCommand("Turn off TV","Katie");
		VrexCommand vrexCommand3=new VrexCommand("Criminal Minds","Paris");
		commands.add(vrexCommand1);
		commands.add(vrexCommand2);
		commands.add(vrexCommand3);

		vrexRequest.put("Delaware",commands);

		List<VrexCommand> commands2=new ArrayList<>();
		VrexCommand vrexCommand4=new VrexCommand("Becky","John");
		VrexCommand vrexCommand5=new VrexCommand("becky","Katie");
		VrexCommand vrexCommand6=new VrexCommand("Random TV","Paris");
		commands.add(vrexCommand1);
		commands.add(vrexCommand2);
		commands.add(vrexCommand3);

		vrexRequest.put("Jersey",commands2);


		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(vrexRequest);

		MvcResult result = mvc.perform(
				post("/commands")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andReturn();

		verify(vrexService, times(2)).processCommandsByState(Mockito.any());
		verify(vrexService, times(1)).topCommandsNationally(Mockito.any());

	}

}
