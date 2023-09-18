package de.vinode.dokobackend.controller;

import de.vinode.dokobackend.controller.dto.CreateGameFormBody;
import de.vinode.dokobackend.domain.Game;
import de.vinode.dokobackend.domain.Player;
import de.vinode.dokobackend.mapper.GameMapper;
import de.vinode.dokobackend.mapper.RoundMapper;
import de.vinode.dokobackend.service.GameService;
import de.vinode.dokobackend.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.webresources.CachedResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Array;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GameController.class)
class GameControllerTest {

    @Autowired
    private GameController gameController;

    @MockBean
    private GameService gameService;

    @MockBean
    private RoundService roundService;

    @MockBean
    private GameMapper gameMapper;

    @MockBean
    private RoundMapper roundMapper;

    @Test
    void whenCreateGameCreatesGameWithShareId() {

        var givenPlayers = new String[]{"a", "b", "c", "d"};
        var shareId = "shareId";

        var req = CreateGameFormBody.builder()
                .playerNames(givenPlayers)
                .build();

        var mockedGame = Game.builder().shareId(shareId).build();

        when(gameService.createGame(givenPlayers)).thenReturn(mockedGame);

        var result = gameController.createGame(req);

        assertEquals(mockedGame.shareId(), Objects.requireNonNull(result.getBody()).shareId());
        assertTrue(Objects.requireNonNull(result.getHeaders().get("Location")).get(0).contains(mockedGame.shareId()));
    }
}