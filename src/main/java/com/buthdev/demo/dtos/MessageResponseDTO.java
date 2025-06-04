package com.buthdev.demo.dtos;

import java.util.List;

public record MessageResponseDTO(List<CandidateDTO> candidates, ContentDTO content, PartDTO parts) {

}
