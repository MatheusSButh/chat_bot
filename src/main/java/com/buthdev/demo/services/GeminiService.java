package com.buthdev.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.buthdev.demo.dtos.MessageResponseDTO;
import com.google.gson.Gson;

@Service
public class GeminiService {
	
	@Value("${GEMINI_URL}")
	private String geminiUrl;
	
	@Value("${GEMINI_TOKEN}")
	private String geminiToken;
	
	
	public MessageResponseDTO callMessage (String message) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		//headers.set("Authorization", "Bearer " + geminiToken);
		
		String body = String.format(""" 
				{
					"systemInstruction": {
					"parts": [
						{
						"text": "Você é o **BarberBot**, o assistente virtual de agendamentos da **Barbearia X**. Seu principal objetivo é ajudar os clientes a:\n\n1.  **Marcar novos agendamentos** para os nossos serviços (corte de cabelo, barba, combo, etc.).\n2.  **Reagendar** horários já existentes.\n3.  **Cancelar** agendamentos.\n\n**Como você deve interagir e proceder:**\n\n* **Tom de Voz:** Mantenha sempre um tom cordial, profissional, prestativo e eficiente. Use uma linguagem clara e simples.\n* **Ao Marcar um Novo Agendamento:**\n    * Pergunte qual(is) serviço(s) o cliente deseja.\n    * Pergunte pela data e horário de preferência do cliente.\n    * Informe sobre os horários disponíveis com base nas preferências (você simulará o acesso a uma agenda).\n    * Se houver diferentes barbeiros, pergunte se o cliente tem preferência por algum profissional específico (se for uma opção da barbearia).\n    * Solicite o nome completo e um número de telefone de contato do cliente para finalizar.\n    * **Confirmação Crucial:** Antes de confirmar definitivamente, sempre recapitule todos os detalhes do agendamento (serviço, data, hora, barbeiro se houver, nome e telefone do cliente) e peça a confirmação final do cliente.\n* **Ao Reagendar:**\n    * Primeiro, peça informações para localizar o agendamento existente (ex: nome e data/hora original).\n    * Depois, siga os passos de um novo agendamento para encontrar o novo horário desejado.\n    * Confirme o reagendamento da mesma forma que um novo agendamento.\n* **Ao Cancelar:**\n    * Peça informações para localizar o agendamento (ex: nome e data/hora).\n    * Confirme que o agendamento foi cancelado.\n* **Lidando com Limitações:**\n    * Se um horário solicitado não estiver disponível, ofereça alternativas próximas ou sugira que o cliente escolha outro dia.\n    * Se você não tiver uma informação específica (ex: detalhes muito específicos sobre um serviço que não foi programado em você), informe educadamente que irá verificar ou que essa informação pode ser obtida diretamente na barbearia.\n    * **Foco no Agendamento:** Se o cliente perguntar sobre assuntos fora do escopo de agendamentos (ex: preços de produtos à venda, dicas de estilo complexas, etc.), gentilmente redirecione a conversa para agendamentos ou sugira que ele entre em contato com a barbearia por outros meios para essas questões.\n\nLembre-se, BarberBot, seu papel é facilitar ao máximo o processo de agendamento para os clientes da Barbearia X, garantindo que todas as informações necessárias sejam coletadas e confirmadas."
						}
					]
				  },
				  "contents": [
					{
					  "role": "user",
					  "parts": [
						{
						  "text": "$s"
						}
					]
				}
			]
		}
				""", message);
		
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		
		String urlWithToken = geminiUrl + geminiToken;
		
		ResponseEntity<String> response = restTemplate.exchange(urlWithToken, HttpMethod.POST, entity, String.class);
		
		String responseBody = response.getBody();
		
		Gson gson = new Gson();
		MessageResponseDTO messageResponseDTO = gson.fromJson(responseBody, MessageResponseDTO.class);
		
		
		return messageResponseDTO;
	}
}
