package de.vinode.dokobackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.util.Set;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@RegisterReflectionForBinding(UUID[].class)
public class RoundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameEntity gameEntity;

    @Column(name = "roundOrder")
    private Long order;

    @OneToMany(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<RoundPlayerResultEntity> roundPlayerResultEntities;
}
