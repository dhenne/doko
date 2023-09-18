package de.vinode.dokobackend.service;

import de.vinode.dokobackend.domain.Game;
import de.vinode.dokobackend.domain.Player;
import de.vinode.dokobackend.entity.GameEntity;
import de.vinode.dokobackend.entity.PlayerEntity;
import de.vinode.dokobackend.mapper.PlayerMapper;
import de.vinode.dokobackend.repository.GameEntityRepository;
import de.vinode.dokobackend.repository.PlayerEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerEntityRepository playerEntityRepository;

    public PlayerEntity createPlayer(String name, Long order, GameEntity gameEntity) {

        return Optional.of(name)
                .filter(playerName -> !playerName.isEmpty())
                .map(playerName -> PlayerEntity.builder()
                        .name(playerName)
                        .gameEntity(gameEntity)
                        .order(order)
                        .build())
                .map(playerEntityRepository::save)
                .orElseThrow();

    }

    public LinkedHashSet<PlayerEntity> getPlayerEntitiesByGameEntity(GameEntity gameEntity) {
        return playerEntityRepository.getPlayerEntitiesByGameEntity(gameEntity);
    }
}