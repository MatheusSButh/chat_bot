package com.buthdev.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buthdev.demo.dtos.MessageDTO;
import com.buthdev.demo.dtos.MessageResponseDTO;
import com.buthdev.demo.dtos.MessageTurn;
import com.buthdev.demo.services.GeminiService;
import com.buthdev.demo.services.MessageHistoricService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "message")
public class MessageController {
	
	@Autowired
	GeminiService geminiService;
	
	@Autowired
	MessageHistoricService messageHistoricService;
	
	@GetMapping(value = "/send")
	public ResponseEntity<String> callGemini(@RequestBody MessageDTO messageDTO, HttpSession session) {
		
		List<MessageTurn> historic = messageHistoricService.getHistoric(session);
		
        MessageResponseDTO response = geminiService.callMessage(messageDTO.message(), historic);
        
		String messageResponse = response.candidates().getFirst().content().parts().getFirst().text();
		
        messageHistoricService.saveHistoric(session, historic, messageDTO.message(), messageResponse);
		
		return ResponseEntity.ok().body(messageResponse);
	}
}
