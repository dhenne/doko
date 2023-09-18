<script lang="ts">
	import type { Game } from '$lib/domain/game';
	import type { RoundPlayerResult } from '$lib/domain/roundPlayerResult';
	import { calculateRoundsSortedForDisplay } from '$lib/domain/service/gameCalculations';

	export let game: Game;

	$: roundsSortedForDisplay = calculateRoundsSortedForDisplay(game);
</script>

<main>
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
				<tr>
					<th scope="row">{ix + 1}</th>
					{#each round.roundPlayerResultList as roundPlayerResult}
						<td>{roundPlayerResult.points}</td>
					{/each}
				</tr>
			{/each}
		</tbody>
	</table>
</main>
