package de.vinode.dokobackend.service;

import de.vinode.dokobackend.controller.dto.CreateGameFormBody;
import de.vinode.dokobackend.domain.Game;
import de.vinode.dokobackend.domain.Player;
import de.vinode.dokobackend.entity.GameEntity;
import de.vinode.dokobackend.entity.PlayerEntity;
import de.vinode.dokobackend.mapper.GameMapper;
import de.vinode.dokobackend.mapper.PlayerMapper;
import de.vinode.dokobackend.repository.GameEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameEntityRepository gameEntityRepository;
    private final PlayerService playerService;
    private final GameMapper gameMapper;

    @Transactional
    public Game createGame(String[] playerNames) {

        var gameEntity = gameEntityRepository.save(
                GameEntity.builder()
                        .shareId(RandomStringUtils.randomAlphanumeric(10))
                        .build());

        var playerTuples = new LinkedHashSet<Pair<Long, String>>();
        for (var ix = 0 ; ix < playerNames.length ; ix++) {
            playerTuples.add(Pair.of((long) ix, playerNames[ix]));
        }

        var players = playerTuples.stream()
                .map(createNewPlayer(gameEntity))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        gameEntity.setPlayerEntities(players);

        return gameMapper.entityToModel(gameEntity);
    }

    private Function<Pair<Long, String>, PlayerEntity> createNewPlayer(GameEntity gameEntity) {
        return pair -> playerService.createPlayer(pair.getRight(), pair.getLeft(), gameEntity);
    }

    public Game retrieveGameByShareId(String shareId) {
        var game = gameEntityRepository.getByShareId(shareId);

        return gameMapper.entityToModel(game);
    }
}