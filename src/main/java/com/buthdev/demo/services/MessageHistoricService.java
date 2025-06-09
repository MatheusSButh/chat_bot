package com.buthdev.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.buthdev.demo.dtos.MessageTurn;

import jakarta.servlet.http.HttpSession;

@Service
public class MessageHistoricService {

	public List<MessageTurn> getHistoric(HttpSession session) {
        @SuppressWarnings("unchecked")
		List<MessageTurn> historic = (List<MessageTurn>) session.getAttribute("chatHistoric");
        
        if (historic == null) {
            return new ArrayList<>();
        }
        
        return historic;
    }
	
	public void saveHistoric(HttpSession session, List<MessageTurn> historic, String message, String messageResponse) {
		historic.add(new MessageTurn("user", message));
        historic.add(new MessageTurn("model", messageResponse));
		session.setAttribute("chatHistoric", historic);
	}
}
