package com.buthdev.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.buthdev.demo.builders.GeminiRequestBuilder;
import com.buthdev.demo.dtos.request.MessageTurn;
import com.buthdev.demo.dtos.response.MessageResponseDTO;
import com.buthdev.demo.exceptions.AICommunicationException;
import com.google.gson.Gson;

@Service
public class GeminiService {
	
	@Value("${GEMINI_URL}")
	private String geminiUrl;
	
	@Value("${GEMINI_TOKEN}")
	private String geminiToken;
	
	@Autowired
	GeminiRequestBuilder geminiRequestBuilder;
	
	RestTemplate restTemplate = new RestTemplate();
	Gson gson = new Gson();
	
	private static final String SYSTEM_INSTRUCTION_TEXT = """
            Você é o **BarberBot**, o assistente virtual de agendamentos da **Barbearia X**. Seu principal objetivo é ajudar os clientes a:

            1.  **Marcar novos agendamentos** para os nossos serviços (corte de cabelo, barba, combo, etc.).
            2.  **Reagendar** horários já existentes.
            3.  **Cancelar** agendamentos.

            **Como você deve interagir e proceder:**

            * **Tom de Voz:** Mantenha sempre um tom cordial, profissional, prestativo e eficiente. Use uma linguagem clara e simples.
            * **Ao Marcar um Novo Agendamento:**
                * Pergunte quais serviços o cliente deseja.
                * Pergunte pela data e horário de preferência do cliente.
                * Informe sobre os horários disponíveis com base nas preferências (você simulará o acesso a uma agenda).
                * Solicite o nome completo para finalizar.
                * **Confirmação Crucial:** Antes de confirmar definitivamente, sempre recapitule todos os detalhes do agendamento (serviço, data, hora, barbeiro se houver, nome e telefone do cliente) e peça a confirmação final do cliente.
            * **Ao Reagendar:**
                * Primeiro, peça informações para localizar o agendamento existente (ex: nome e data/hora original).
                * Depois, siga os passos de um novo agendamento para encontrar o novo horário desejado.
                * Confirme o reagendamento da mesma forma que um novo agendamento.
            * **Ao Cancelar:**
                * Peça informações para localizar o agendamento (ex: nome e data/hora).
                * Confirme que o agendamento foi cancelado.
            * **Lidando com Limitações:**
                * Se um horário solicitado não estiver disponível, ofereça alternativas próximas ou sugira que o cliente escolha outro dia.
                * Se você não tiver uma informação específica (ex: detalhes muito específicos sobre um serviço que não foi programado em você), informe educadamente que irá verificar ou que essa informação pode ser obtida diretamente na barbearia.
                * **Foco no Agendamento:** Se o cliente perguntar sobre assuntos fora do escopo de agendamentos (ex: preços de produtos à venda, dicas de estilo complexas, etc.), gentilmente redirecione a conversa para agendamentos ou sugira que ele entre em contato com a barbearia por outros meios para essas questões.

            Lembre-se, BarberBot, seu papel é facilitar ao máximo o processo de agendamento para os clientes da Barbearia X, garantindo que todas as informações necessárias sejam coletadas e confirmadas.
            """;
	
	
	public MessageResponseDTO callMessage (String message, List<MessageTurn> historic) {
		
		try {
			
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		
		String body = geminiRequestBuilder.buildRequest(historic, message, SYSTEM_INSTRUCTION_TEXT);
		
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		
		String urlWithToken = geminiUrl + geminiToken;
		ResponseEntity<String> response = restTemplate.exchange(urlWithToken, HttpMethod.POST, entity, String.class);
		
		String responseBody = response.getBody();
		MessageResponseDTO messageResponseDTO = gson.fromJson(responseBody, MessageResponseDTO.class);
		
		return messageResponseDTO;
		}
		
		catch(HttpClientErrorException e) {
			throw new AICommunicationException();
		}
	}
}