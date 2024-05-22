package de.vinode.dokobackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.util.*;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@RegisterReflectionForBinding(UUID[].class)
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique=true)
    private String shareId;

    @OrderBy("order")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PlayerEntity> playerEntities;

    @OrderBy("order")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<RoundEntity> roundEntities;

}
