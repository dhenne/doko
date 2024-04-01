package de.vinode.dokobackend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoundDto {
    UUID id;
    List<RoundPlayerResultDto> roundPlayerResultList;
}
