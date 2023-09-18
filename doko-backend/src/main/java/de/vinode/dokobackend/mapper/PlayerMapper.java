package de.vinode.dokobackend.mapper;

import de.vinode.dokobackend.controller.dto.PlayerDto;
import de.vinode.dokobackend.domain.Player;
import de.vinode.dokobackend.entity.PlayerEntity;
import org.mapstruct.Mapper;


@Mapper
public interface PlayerMapper {

    PlayerEntity modelToEntity(Player player);

    Player entityToModel(PlayerEntity playerEntity);

    PlayerDto playerToDto(Player player);
}