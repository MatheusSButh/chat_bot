package com.buthdev.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buthdev.demo.dtos.MessageDTO;
import com.buthdev.demo.services.GeminiService;

@RestController
@RequestMapping(value = "message")
public class MessageController {

	@Autowired
	GeminiService geminiService;
	
	@GetMapping(value = "/send")
	public ResponseEntity<String> callGemini(@RequestBody MessageDTO messageDTO) {
		
		var a = geminiService.callMessage(messageDTO.message());
		
		
		return ResponseEntity.ok().body(a.candidates().getFirst().content().parts().getFirst().text());
	}
}
