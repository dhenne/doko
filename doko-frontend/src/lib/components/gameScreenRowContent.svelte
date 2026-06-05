<script lang="ts">
	export let round: import('$lib/domain/round').Round;
	export let ix: number;
	export let isFooter: boolean = false;
</script>

<th scope="row" class="px-3 py-3 text-center text-xs font-bold border-r border-white/5 min-w-[50px] {isFooter ? 'text-[#FE795D] font-extrabold uppercase' : 'text-slate-500'}">
	{#if isFooter}
		Total
	{:else}
		#{ix + 1}
	{/if}
</th>
{#each round.roundPlayerResultList as roundPlayerResult}
	<td class="px-3 py-3 text-center text-sm font-medium border-r border-white/5 last:border-r-0 {isFooter ? 'text-white font-extrabold bg-[#FE795D]/5' : 'text-slate-300'}">
		{roundPlayerResult.points}
	</td>
{/each}
