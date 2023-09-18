package de.vinode.dokobackend.mapper;

import de.vinode.dokobackend.controller.dto.GameDto;
import de.vinode.dokobackend.controller.dto.PlayerDto;
import de.vinode.dokobackend.domain.Game;
import de.vinode.dokobackend.domain.Player;
import de.vinode.dokobackend.entity.GameEntity;
import de.vinode.dokobackend.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;


@Mapper(uses = {PlayerMapper.class, RoundMapper.class})
public interface GameMapper {

    GameEntity modelToEntity(Game player);

    @Mappings({
            @Mapping(target = "players", source = "playerEntities"),
            @Mapping(target = "rounds", source = "roundEntities")
    })
    Game entityToModel(GameEntity gameEntity);

    GameDto gameToDto(Game game);
}