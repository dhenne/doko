package de.vinode.dokobackend.service;

import de.vinode.dokobackend.mapper.GameMapper;
import de.vinode.dokobackend.repository.GameEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GameService.class)
class GameServiceTest {

    @MockBean
    private GameEntityRepository gameEntityRepository;

    @MockBean
    private PlayerService playerService;
    @MockBean
    private GameMapper gameMapper;

    @Test
    void createGame() {
    }
}