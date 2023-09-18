package de.vinode.dokobackend.mapper;

import de.vinode.dokobackend.controller.dto.RoundDto;
import de.vinode.dokobackend.controller.dto.RoundPlayerResultDto;
import de.vinode.dokobackend.domain.Round;
import de.vinode.dokobackend.domain.RoundPlayerResult;
import de.vinode.dokobackend.entity.RoundEntity;
import de.vinode.dokobackend.entity.RoundPlayerResultEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface RoundPlayerResultMapper {

    RoundPlayerResultEntity modelToEntity(RoundPlayerResult roundPlayerResult);

    @Mappings(@Mapping(target = "name", source = "player.name"))
    RoundPlayerResult entityToModel(RoundPlayerResultEntity roundPlayerResultEntity);

    RoundPlayerResultDto ModelToDto(RoundPlayerResult roundPlayerResult);

    RoundPlayerResult dtoToModel(RoundPlayerResultDto roundPlayerResultDto);
}