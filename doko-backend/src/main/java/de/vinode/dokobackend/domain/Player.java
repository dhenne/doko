package de.vinode.dokobackend.domain;

import lombok.Builder;

@Builder
public record Player(String name, Long order) {
}
