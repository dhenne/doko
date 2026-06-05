<script lang="ts">
	import type { Game } from '$lib/domain/game';
	import type { RoundPlayerResult } from '$lib/domain/roundPlayerResult';
	import { calculateRoundsSortedForDisplay } from '$lib/domain/service/gameCalculations';
	import GameScreenRowContent from '$lib/components/gameScreenRowContent.svelte';

	let { game = $bindable() } = $props<{ game: Game }>();

	let roundsSortedForDisplay = $derived(calculateRoundsSortedForDisplay(game));
</script>

<div class="w-full border border-white/5 bg-[#1a1a2e]/25 backdrop-blur-md rounded-3xl overflow-hidden shadow-2xl">
	<div class="overflow-x-auto overflow-y-auto max-h-[55vh] relative scrollbar-thin">
		<table class="w-full border-collapse table-fixed min-w-[280px]">
			<thead>
				<tr class="border-b border-white/5">
					<th scope="col" class="sticky top-0 z-20 bg-[#0f0f1a] px-3 py-3 text-center text-xs font-bold uppercase tracking-wider text-slate-400 border-r border-white/5 w-12">
						#
					</th>
					{#each game.players as player}
						<th scope="col" class="sticky top-0 z-20 bg-[#0f0f1a] px-3 py-3 text-center text-xs font-bold uppercase tracking-wider text-slate-300 border-r border-white/5 last:border-r-0 truncate">
							{player.name}
						</th>
					{/each}
				</tr>
			</thead>
			<tbody>
				{#each roundsSortedForDisplay as round, ix}
					<tr class="border-b border-white/5 hover:bg-white/[0.01] odd:bg-white/[0.01] transition-colors">
						<GameScreenRowContent {round} {ix} />
					</tr>
				{/each}
			</tbody>
			{#if roundsSortedForDisplay.length > 0}
				<tfoot class="sticky bottom-0 z-20 bg-[#0f0f1a]/95 border-t border-[#FE795D]/30 shadow-[0_-4px_10px_rgba(0,0,0,0.3)]">
					<tr class="font-extrabold text-white">
						<GameScreenRowContent round={roundsSortedForDisplay[roundsSortedForDisplay.length - 1]} ix={roundsSortedForDisplay.length - 1} isFooter={true} />
					</tr>
				</tfoot>
			{/if}
		</table>
	</div>
</div>
