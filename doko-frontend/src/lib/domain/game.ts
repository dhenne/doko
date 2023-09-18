import type { Player } from "./player";
import type { Round } from "./round";

export type Game = {
    players: Player[],
    rounds: Round[]
};
