import type { Round } from '$lib/domain/round'

export type Points = { [key: string]: number };

export const mapToRound = (points: Points): Round => {
    return {
        roundPlayerResultList: Object.keys(points).map(k => { return { name: k, points: points[k]} })
    };
};

export const postNewRoundForGame = (gameShareId: string, points: Points) => {

    const round: Round = mapToRound(points);

    const url = `/api/v1/game/${gameShareId}/round`;

    const headers: HeadersInit = new Headers();
    headers.set('Content-Type', 'application/json');

    const body: BodyInit = JSON.stringify(round);

    return fetch(url, {
        method: "POST",
        headers,
        body
    } as RequestInit);
};

export const deleteRound = (gameShareId: string, round : Round) => {
    const url = `/api/v1/game/${gameShareId}/round/${round.id}`;

    return fetch(url, {method: "DELETE"});
};

export const getRoundByUrl = async (roundUrl: string) : Promise<Round> => {
    const headers: HeadersInit = new Headers();
    headers.set('Content-Type', 'application/json');

    const response = await fetch(roundUrl, {headers}).then(r => r.json())

    return response as Round;
}
