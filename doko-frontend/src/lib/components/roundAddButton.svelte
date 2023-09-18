<script lang="ts">
	import type { Game } from '$lib/domain/game';
	import AddRoundMenu  from './addRoundMenu.svelte';
	import type { Points } from '$lib/adapter/backend';
	import { onMount, createEventDispatcher } from 'svelte';

	export let game: Game;
	export let shareId: string;

	const dispatch = createEventDispatcher();

	let isAddRoundMenuDisplayed: Boolean = false;

	const toggleAddRoundMenu = () => {
		isAddRoundMenuDisplayed = !isAddRoundMenuDisplayed;
	};

	const handleRoundAdded = (e : CustomEvent<Points>) => {
		toggleAddRoundMenu();
		dispatch("roundAdded", e.detail)
	};
</script>

<main>
	{#if isAddRoundMenuDisplayed}
	  <AddRoundMenu { game }  { shareId } on:roundAdded={handleRoundAdded}></AddRoundMenu>
	{:else}
		<div class="container-fluid">
			<div class="row">
				<div class="col">
					<button class="btn btn-primary full-width-btn" on:click={toggleAddRoundMenu}>+</button>
				</div>
			</div>
		</div>
	{/if}
</main>

<style scoped>
	/* Custom CSS to make the button span full width */
	.full-width-btn {
		width: 100%;
	}
</style>
