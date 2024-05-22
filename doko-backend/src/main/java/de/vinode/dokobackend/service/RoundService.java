package de.vinode.dokobackend.service;

import de.vinode.dokobackend.domain.Round;
import de.vinode.dokobackend.domain.RoundPlayerResult;
import de.vinode.dokobackend.entity.PlayerEntity;
import de.vinode.dokobackend.entity.RoundPlayerResultEntity;
import de.vinode.dokobackend.mapper.RoundMapper;
import de.vinode.dokobackend.repository.GameEntityRepository;
import de.vinode.dokobackend.repository.RoundEntityRepository;
import de.vinode.dokobackend.repository.RoundPlayerResultEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final GameEntityRepository gameEntityRepository;
    private final PlayerService playerService;


    private final RoundEntityRepository roundEntityRepository;
    private final RoundPlayerResultEntityRepository roundPlayerResultEntityRepository;

    private final RoundMapper roundMapper;

    @Transactional
    public Round pushRound(String shareId, Round round) {

        var gameEntity = gameEntityRepository.getByShareId(shareId);
        var nextIndex = gameEntity.getRoundEntities().size();
        var playerEntities = playerService.getPlayerEntitiesByGameEntity(gameEntity);
        var unmanagedEntity = roundMapper.modelToEntity(round);

        var resultList = round.roundPlayerResultList().stream()
                .map(toRoundPlayerResultEntity(playerEntities))
                .map(roundPlayerResultEntityRepository::save)
                .collect(Collectors.toSet());

        unmanagedEntity.setRoundPlayerResultEntities(resultList);

        var roundEntity = roundEntityRepository.save(unmanagedEntity);
        roundEntity.getRoundPlayerResultEntities()
                .forEach(roundPlayerResult -> roundPlayerResult.setRound(roundEntity));
        roundEntity.setGameEntity(gameEntity);
        roundEntity.setOrder((long) nextIndex);

        gameEntity.getRoundEntities().add(roundEntity);

        return roundMapper.entityToModel(roundEntity);
    }

    private Function<RoundPlayerResult, RoundPlayerResultEntity> toRoundPlayerResultEntity(LinkedHashSet<PlayerEntity> playerEntities) {
        return roundPlayerResult -> RoundPlayerResultEntity.builder()
                .player(playerEntities.stream().filter(playerEntity -> playerEntity.getName().equals(roundPlayerResult.name())).findFirst().orElseThrow())
                .points(roundPlayerResult.points())
                .build();
    }

    public void deleteRound(String shareId, UUID roundId) {
        var gameEntity = gameEntityRepository.getByShareId(shareId);

        gameEntity.getRoundEntities()
                .removeIf(roundEntity -> roundEntity.getId().equals(roundId));

        gameEntityRepository.save(gameEntity);
    }

    public Round getRoundById(String shareId, UUID roundId) {
        return roundEntityRepository
                .findById(roundId)
                .filter(roundEntity -> roundEntity.getGameEntity().getShareId().equals(shareId))
                .map(roundMapper::entityToModel)
                .orElseThrow();
    }
}
