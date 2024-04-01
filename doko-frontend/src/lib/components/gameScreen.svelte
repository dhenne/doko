<script lang="ts">
	import type { Game } from '$lib/domain/game';
	import type { RoundPlayerResult } from '$lib/domain/roundPlayerResult';
	import { calculateRoundsSortedForDisplay } from '$lib/domain/service/gameCalculations';
	import GameScreenRowContent from '$lib/components/gameScreenRowContent.svelte';

	export let game: Game;

	$: roundsSortedForDisplay = calculateRoundsSortedForDisplay(game);
</script>

<table class="table">
	<thead>
		<tr>
			<th scope="col">#</th>
			{#each game.players as player}
				<th scope="col">{player.name}</th>
			{/each}
		</tr>
	</thead>
	<tbody>
		{#each roundsSortedForDisplay as round, ix}
			{#if ix !== roundsSortedForDisplay.length - 1}
				<tr>
					<GameScreenRowContent {round} {ix} />
				</tr>
			{:else}
				<tr>
					<GameScreenRowContent {round} {ix} />
				</tr>
			{/if}
		{/each}
	</tbody>

	
</table>
