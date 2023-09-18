import type { Game } from "../game";
import type { Round } from "../round";
import type { RoundPlayerResult } from "../roundPlayerResult";

const comparatorResultsAlignedWithPlayerOrder = (game: Game) => {
    return (
        a: RoundPlayerResult,
        b: RoundPlayerResult
    ): number => {
        const orderA = game.players.find((p) => p.name === a.name)?.order;
        const orderB = game.players.find((p) => p.name === b.name)?.order;

        if (orderA === undefined || orderB === undefined) {
            return 0;
        }

        return orderA - orderB;
    }
};

const toSumWithPreviousValues = (round: Round, index: number, rounds: Round[]): Round => {

    if (index === 0) {
        return round;
    }

    const previousRound = rounds[index - 1];

    round.roundPlayerResultList.forEach(r => {
        const prevPoints = previousRound.roundPlayerResultList.find(prevItem => r.name === prevItem.name)?.points;
        r.points += prevPoints || 0;
    });

    return round;
};

const deepCopyOfResult = (result: RoundPlayerResult): RoundPlayerResult => { return { name: result.name, points: result.points }; };

const createDeepCopyOfRound = (prevRound: Round): Round => {

    const round: Round = { roundPlayerResultList: [] };

    prevRound.roundPlayerResultList
        .map(deepCopyOfResult)
        .forEach(result => round.roundPlayerResultList.push(result));

    return round;
};

const paddingOfPausedPlayers = (game: Game) => {
    return (round: Round): Round => {
        if (round.roundPlayerResultList.length !== game.players.length) {
            game.players
                .filter(
                    (p) => round.roundPlayerResultList.find((result) => p.name === result.name) === undefined
                )
                .forEach((player) => round.roundPlayerResultList.push({ name: player.name, points: 0 }));
        }
        return round;
    }
};

const sortRoundResultList = (game: Game) => {
    return (round: Round): Round => {
        round.roundPlayerResultList.sort(comparatorResultsAlignedWithPlayerOrder(game));
        return round;
    }
};

export const calculateRoundsSortedForDisplay = (game: Game) => game.rounds
    .map(createDeepCopyOfRound)
    .map(paddingOfPausedPlayers(game))
    .map(sortRoundResultList(game))
    .map(toSumWithPreviousValues); 