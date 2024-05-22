<script lang="ts">
	import GameScreen from '$lib/components/gameScreen.svelte';
	import RoundAddButton from '$lib/components/roundAddButton.svelte';
	import { page } from '$app/stores';
	import { mapToRound, getRoundByUrl, deleteRound, type Points } from '$lib/adapter/backend';
	import type { Game } from '$lib/domain/game';
	import { onMount } from 'svelte';
	import { Alert } from 'flowbite-svelte';
	import type { Round } from '$lib/domain/round'

	let game: Game;
	let shareId : string | null = null;

	onMount(async () => {
		const paramsShareId = $page.url.searchParams.get('shareid');
        if (paramsShareId !== null) {
            game = await loadGame(paramsShareId);
        }
        shareId = paramsShareId;
	});

	const handleRoundAdded = async (e: CustomEvent<string>) => {

		const addedRound : Round = await getRoundByUrl(e.detail);

		game.rounds.push(addedRound);

		const ix = game.rounds.length;
		game.rounds[ix] = game.rounds[ix]; // make write visible for compiler
	};

	const handleRoundDeleted = async () => {
		const lastRound = game.rounds.at(-1);
		if (shareId === null || lastRound === undefined) {
			return
		}
		
		const response = await deleteRound(shareId, lastRound);
		if (response.status === 200) {
			game.rounds.pop();

			const ix = game.rounds.length;
		    game.rounds[ix] = game.rounds[ix]; // make write visible for compiler
		}
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
		<RoundAddButton bind:game {shareId} on:roundAdded={handleRoundAdded} on:roundDeleted={handleRoundDeleted}/>
	{:else}
		<a class="btn btn-primary" href="/game/create">Create new game</a>
	{/if}
	<div class="p-8">
		<Alert>
		  <span class="font-medium">Info alert!</span>
		  Change a few things up and try submitting again.
		</Alert>
	  </div>
</main>
