package com.comcast.vrex;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * VrexApplication.java - Application that accepts a POST request consisting of recent speech commands and returns information
 * about the most frequent commands by state and nationally.
 *
 * @author  Nagesh Chandra N.T
 * @version 1.0
 *
 */


@SpringBootApplication
public class VrexApplication {

	public static void main(String[] args) {
		SpringApplication.run(VrexApplication.class, args);
	}

}
