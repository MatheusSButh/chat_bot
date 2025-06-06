package com.buthdev.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.buthdev.demo.dtos.MessageResponseDTO;
import com.buthdev.demo.dtos.MessageTurn;
import com.google.gson.Gson;

@Service
public class GeminiService {
	
	@Value("${GEMINI_URL}")
	private String geminiUrl;
	
	@Value("${GOOGLE_API_KEY}")
	private String geminiToken;
	
	RestTemplate restTemplate = new RestTemplate();
	
	private static final String SYSTEM_INSTRUCTION_TEXT = """
            Você é o **BarberBot**, o assistente virtual de agendamentos da **Barbearia X**. Seu principal objetivo é ajudar os clientes a:

            1.  **Marcar novos agendamentos** para os nossos serviços (corte de cabelo, barba, combo, etc.).
            2.  **Reagendar** horários já existentes.
            3.  **Cancelar** agendamentos.

            **Como você deve interagir e proceder:**

            * **Tom de Voz:** Mantenha sempre um tom cordial, profissional, prestativo e eficiente. Use uma linguagem clara e simples.
            * **Ao Marcar um Novo Agendamento:**
                * Pergunte qual(is) serviço(s) o cliente deseja.
                * Pergunte pela data e horário de preferência do cliente.
                * Informe sobre os horários disponíveis com base nas preferências (você simulará o acesso a uma agenda).
                * Se houver diferentes barbeiros, pergunte se o cliente tem preferência por algum profissional específico (se for uma opção da barbearia).
                * Solicite o nome completo e um número de telefone de contato do cliente para finalizar.
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
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		
		
		
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
		
		Gson gson = new Gson();
		String body = gson.toJson(requestPayload);
		
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		
		String urlWithToken = geminiUrl + geminiToken;
		ResponseEntity<String> response = restTemplate.exchange(urlWithToken, HttpMethod.POST, entity, String.class);
		
		String responseBody = response.getBody();
		MessageResponseDTO messageResponseDTO = gson.fromJson(responseBody, MessageResponseDTO.class);
		
		return messageResponseDTO;
	}
}
