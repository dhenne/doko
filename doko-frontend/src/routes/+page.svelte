<script lang="ts">
	import GameScreen from '$lib/components/gameScreen.svelte';
	import RoundAddButton from '$lib/components/roundAddButton.svelte';
	import { page } from '$app/stores';
	import { mapToRound, type Points } from '$lib/adapter/backend';
	import type { Game } from '$lib/domain/game';
	import { onMount } from 'svelte';

	let game: Game;
	let shareId : string | null = null;

	onMount(async () => {
		const paramsShareId = $page.url.searchParams.get('shareid');
        if (paramsShareId !== null) {
            game = await loadGame(paramsShareId);
        }
        shareId = paramsShareId;
	});

	const handleRoundAdded = (e: CustomEvent<Points>) => {
		game.rounds.push(mapToRound(e.detail));

		const ix = game.rounds.length;
		game.rounds[ix] = game.rounds[ix]; // make write visible for compiler
	};

	const loadGame = async (id: string): Promise<Game> => {
		const apiResponse = await fetch(`/api/v1/game/${id}`).then(
			(response) => response.json()
		);

		return apiResponse as Game;
	};
</script>

<main>
	{#if shareId !== null && game !== undefined}
		<GameScreen bind:game />
		<RoundAddButton bind:game {shareId} on:roundAdded={handleRoundAdded} />
	{:else}
		<a class="btn btn-primary" href="/game/create">Create new game</a>
	{/if}
</main>
