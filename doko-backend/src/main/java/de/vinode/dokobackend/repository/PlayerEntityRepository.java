package de.vinode.dokobackend.repository;


import de.vinode.dokobackend.entity.GameEntity;
import de.vinode.dokobackend.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerEntityRepository extends JpaRepository<PlayerEntity, UUID> {

    LinkedHashSet<PlayerEntity> getPlayerEntitiesByGameEntity(GameEntity gameEntity);
}
