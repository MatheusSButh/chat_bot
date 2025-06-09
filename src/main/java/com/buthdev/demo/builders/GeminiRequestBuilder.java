package com.buthdev.demo.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.buthdev.demo.dtos.MessageTurn;
import com.google.gson.Gson;

@Component
public class GeminiRequestBuilder {
	
	Gson gson = new Gson();

	public String buildRequest(List<MessageTurn> historic, String message, String SYSTEM_INSTRUCTION_TEXT) {
	
		List<Map<String, Object>> contents = new ArrayList<>();
		
		if (historic != null) {
			historic.forEach(turn -> contents.add(Map.of(
					"role", turn.role(), 
					"parts", List.of(Map.of("text", turn.text()))
					)));
		}
		
		contents.add(Map.of(
	            "role", "user",
	            "parts", List.of(Map.of("text", message))
	        ));
		
		Map<String, Object> systemInstruction = Map.of("parts", List.of(Map.of("text", SYSTEM_INSTRUCTION_TEXT)));
		
		Map<String, Object> requestPayload = Map.of(
	            "systemInstruction", systemInstruction,
	            "contents", contents
	        );
		
		String body = gson.toJson(requestPayload);
		
		return body;
	}
}
