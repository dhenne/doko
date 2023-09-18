package de.vinode.dokobackend.controller.dto;

import lombok.Builder;

import java.util.LinkedHashSet;
import java.util.LinkedList;

@Builder
public record GameDto(LinkedHashSet<PlayerDto> players, LinkedHashSet<RoundDto> rounds) {
}
