package de.vinode.dokobackend.repository;


import de.vinode.dokobackend.entity.GameEntity;
import de.vinode.dokobackend.entity.RoundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoundEntityRepository extends JpaRepository<RoundEntity, UUID> {
}
