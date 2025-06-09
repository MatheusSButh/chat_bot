package com.buthdev.demo.dtos.response;

import java.util.List;

import com.buthdev.demo.dtos.request.CandidateDTO;
import com.buthdev.demo.dtos.request.ContentDTO;
import com.buthdev.demo.dtos.request.PartDTO;

public record MessageResponseDTO(List<CandidateDTO> candidates, ContentDTO content, PartDTO parts) {
}