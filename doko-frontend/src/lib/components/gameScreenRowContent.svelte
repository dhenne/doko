<script lang="ts">
	export let round: import('$lib/domain/round').Round;
	export let ix: number;
</script>

<th scope="row">{ix + 1}</th>
{#each round.roundPlayerResultList as roundPlayerResult}
	<td>{roundPlayerResult.points}</td>
{/each}
