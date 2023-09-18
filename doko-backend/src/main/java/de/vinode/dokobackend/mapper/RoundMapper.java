package de.vinode.dokobackend.mapper;

import de.vinode.dokobackend.controller.dto.RoundDto;
import de.vinode.dokobackend.domain.Round;
import de.vinode.dokobackend.entity.RoundEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(uses = {PlayerMapper.class, RoundPlayerResultMapper.class})
public interface RoundMapper {

    @Mappings(@Mapping(target = "roundPlayerResultEntities", source = "roundPlayerResultList"))
    RoundEntity modelToEntity(Round round);

    @Mappings(@Mapping(target = "roundPlayerResultList", source = "roundPlayerResultEntities"))
    Round entityToModel(RoundEntity roundEntity);

    RoundDto ModelToDto(Round round);

    Round postDtoToModel(RoundDto roundDto);
}