<script lang="ts">
	import GameScreen from '$lib/components/gameScreen.svelte';
	import RoundAddButton from '$lib/components/roundAddButton.svelte';
	import { page } from '$app/stores';
	import { mapToRound, getRoundByUrl, deleteRound, type Points } from '$lib/adapter/backend';
	import type { Game } from '$lib/domain/game';
	import { onMount } from 'svelte';
	import { Alert } from 'flowbite-svelte';
	import type { Round } from '$lib/domain/round'

	let game: Game | undefined = $state(undefined);
	let shareId : string | null = $state(null);

	onMount(async () => {
		const paramsShareId = $page.url.searchParams.get('shareid');
        if (paramsShareId !== null) {
            game = await loadGame(paramsShareId);
        }
        shareId = paramsShareId;
		console.log('Loaded game:', game);
	});

	const onRoundAdded = async (roundUrl: string) => {
		if (game === undefined) {
			return;
		}

		const addedRound : Round = await getRoundByUrl(roundUrl);

		game = { ...game, rounds: [...game.rounds, addedRound] };
	};

	const onRoundDeleted = async () => {
		const lastRound = game?.rounds.at(-1);
		if (shareId === null || lastRound === undefined) {
			return
		}

		const response = await deleteRound(shareId, lastRound);
		if (response.status === 200 && game !== undefined) {
			game = { ...game, rounds: game.rounds.slice(0, -1) };
		}
	};

	const loadGame = async (id: string): Promise<Game> => {
		const apiResponse = await fetch(`/api/v1/game/${id}`).then(
			(response) => response.json()
		);

		return apiResponse as Game;
	};
</script>

<main class={shareId !== null && game !== undefined ? "w-full max-w-md mx-auto px-3 py-4 pb-28 flex-1 flex flex-col" : ""}>
	{#if shareId !== null && game !== undefined}
		<div class="flex-1 flex flex-col gap-4">
			<GameScreen bind:game />
		</div>
		<RoundAddButton bind:game {shareId} {onRoundAdded} {onRoundDeleted}/>
	{:else}
		<div class="landing">
			<a class="cta-button" href="/game/create">Create New Game</a>
		</div>
	{/if}
</main>

<style>
	.landing {
		position: fixed;
		inset: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		background: radial-gradient(ellipse at center, #1a1a2e 0%, #0f0f1a 60%, #080810 100%);
		overflow: hidden;
	}

	.cta-button {
		position: relative;
		display: inline-block;
		padding: 1.1rem 3rem;
		font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif;
		font-size: 1.15rem;
		font-weight: 700;
		letter-spacing: 0.12em;
		text-transform: uppercase;
		text-decoration: none;
		color: #fff;
		background: linear-gradient(135deg, #FE795D 0%, #EB4F27 100%);
		border: none;
		border-radius: 60px;
		cursor: pointer;
		box-shadow:
			0 0 20px rgba(254, 121, 93, 0.35),
			0 0 60px rgba(254, 121, 93, 0.15),
			0 4px 15px rgba(0, 0, 0, 0.3);
		animation: pulseGlow 2.5s ease-in-out infinite;
		transition: transform 0.25s cubic-bezier(0.34, 1.56, 0.64, 1),
					box-shadow 0.25s ease;
	}

	.cta-button:hover {
		transform: scale(1.06);
		box-shadow:
			0 0 30px rgba(254, 121, 93, 0.55),
			0 0 80px rgba(254, 121, 93, 0.25),
			0 8px 25px rgba(0, 0, 0, 0.35);
		animation-play-state: paused;
	}

	.cta-button:active {
		transform: scale(0.97);
		box-shadow:
			0 0 15px rgba(254, 121, 93, 0.4),
			0 0 40px rgba(254, 121, 93, 0.15),
			0 2px 8px rgba(0, 0, 0, 0.3);
	}

	@keyframes pulseGlow {
		0%, 100% {
			box-shadow:
				0 0 20px rgba(254, 121, 93, 0.35),
				0 0 60px rgba(254, 121, 93, 0.15),
				0 4px 15px rgba(0, 0, 0, 0.3);
		}
		50% {
			box-shadow:
				0 0 30px rgba(254, 121, 93, 0.5),
				0 0 80px rgba(254, 121, 93, 0.25),
				0 4px 15px rgba(0, 0, 0, 0.3);
		}
	}
</style>
