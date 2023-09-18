package de.vinode.dokobackend.repository;


import de.vinode.dokobackend.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameEntityRepository extends JpaRepository<GameEntity, UUID> {

    GameEntity getByShareId(String shareId);
}
