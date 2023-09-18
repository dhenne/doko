package de.vinode.dokobackend.controller.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateGameFormBody {
    String[] playerNames;
}
