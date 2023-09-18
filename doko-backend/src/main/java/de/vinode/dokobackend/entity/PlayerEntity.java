package de.vinode.dokobackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@RegisterReflectionForBinding(UUID[].class)
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(name = "playerOrder")
    private Long order;

    @ManyToOne
    private GameEntity gameEntity;
}
