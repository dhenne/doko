package de.vinode.dokobackend.repository;


import de.vinode.dokobackend.entity.RoundEntity;
import de.vinode.dokobackend.entity.RoundPlayerResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoundPlayerResultEntityRepository extends JpaRepository<RoundPlayerResultEntity, UUID> {
}
