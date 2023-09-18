package de.vinode.dokobackend.domain;

import lombok.Builder;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.UUID;

@Builder
public record Game(UUID id, String shareId, LinkedHashSet<Player> players, LinkedHashSet<Round> rounds) {
}
