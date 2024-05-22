package de.vinode.dokobackend.controller;

import de.vinode.dokobackend.controller.dto.CreateGameFormBody;
import de.vinode.dokobackend.controller.dto.CreatedGame;
import de.vinode.dokobackend.controller.dto.GameDto;
import de.vinode.dokobackend.controller.dto.RoundDto;
import de.vinode.dokobackend.mapper.GameMapper;
import de.vinode.dokobackend.mapper.RoundMapper;
import de.vinode.dokobackend.service.GameService;
import de.vinode.dokobackend.service.RoundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = GameController.URL_PATH)
@Slf4j
@RequiredArgsConstructor
public class GameController {

    public static final String URL_PATH = "api/v1/game";

    private final GameService gameService;
    private final RoundService roundService;

    private final GameMapper gameMapper;
    private final RoundMapper roundMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CreatedGame> createGame(@ModelAttribute CreateGameFormBody createGameFormBody) {

        var createdGame = Optional.of(createGameFormBody)
                .map(CreateGameFormBody::getPlayerNames)
                .map(gameService::createGame)
                .map(game -> new CreatedGame(game.shareId()))
                .orElseThrow();

        return ResponseEntity
                .created(
                        URI.create(
                                String.format("/%s/%s", URL_PATH, createdGame.shareId())))
                .body(createdGame);
    }

    @GetMapping("/{shareId}/round/{roundId}")
    public ResponseEntity<RoundDto> getRound(@PathVariable("shareId") String shareId, @PathVariable("roundId") UUID roundId) {

        var round = roundService.getRoundById(shareId, roundId);
        var dto = roundMapper.ModelToDto(round);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{shareId}")
    public ResponseEntity<GameDto> getGame(@PathVariable("shareId") String shareId) {

        var game = gameService.retrieveGameByShareId(shareId);
        var dto = gameMapper.gameToDto(game);

        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/{shareId}/round", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postRound(@PathVariable("shareId") String shareId, @RequestBody RoundDto roundDto) {

        var round = roundService.pushRound(shareId, roundMapper.postDtoToModel(roundDto));

        return ResponseEntity
                .created(
                        URI.create(
                                String.format("/api/v1/game/%s/round/%s", shareId, round.id())))
                .build();

    }

    @DeleteMapping(value = "/{shareId}/round/{roundId}")
    public ResponseEntity<Void> deleteRound(@PathVariable("shareId") String shareId, @PathVariable("roundId") UUID roundId) {

        roundService.deleteRound(shareId, roundId);

        return ResponseEntity.ok().build();

    }
}
