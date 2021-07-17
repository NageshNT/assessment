package com.comcast.vrex.config;

import com.comcast.vrex.model.TopCommandFrequencyTracker;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.*;


@Component
public class HistoryTracker {

	public static Set<TopCommandFrequencyTracker> getTopNationCommandsMap() {
		return topNationCommandsMap;
	}

	public static void setTopNationCommandsMap(Set<TopCommandFrequencyTracker> topNationCommandsMap) {
		HistoryTracker.topNationCommandsMap = topNationCommandsMap;
	}

	public static Map<String, TopCommandFrequencyTracker> getTopStateCommandsMap() {
		return topStateCommandsMap;
	}

	public static void setTopStateCommandsMap(Map<String, TopCommandFrequencyTracker> topStateCommandsMap) {
		HistoryTracker.topStateCommandsMap = topStateCommandsMap;
	}

	public static Set<TopCommandFrequencyTracker> topNationCommandsMap = new HashSet<>();

	public static  Map<String, TopCommandFrequencyTracker> topStateCommandsMap = new LinkedCaseInsensitiveMap<>();

}
