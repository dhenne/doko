package de.vinode.dokobackend.domain;

import de.vinode.dokobackend.controller.dto.PlayerDto;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public record Round(
                    LinkedList<RoundPlayerResult> roundPlayerResultList,
                    Long order
) {
}
