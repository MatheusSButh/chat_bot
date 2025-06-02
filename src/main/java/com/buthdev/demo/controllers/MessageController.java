package com.buthdev.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buthdev.demo.dtos.MessageDTO;
import com.buthdev.demo.dtos.MessageResponseDTO;
import com.buthdev.demo.services.GeminiService;

@RestController
@RequestMapping(value = "message")
public class MessageController {

	@Autowired
	GeminiService geminiService;
	
	@PostMapping(value = "/send")
	public ResponseEntity<MessageResponseDTO> callGemini(@RequestBody MessageDTO messageDTO) {
		return ResponseEntity.ok().body(geminiService.callMessage(messageDTO.message()));
	}
}
