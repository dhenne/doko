<script lang="ts">
	import type { Game } from '$lib/domain/game';
	import AddRoundMenu from './addRoundMenu.svelte';
	import type { Points } from '$lib/adapter/backend';
	import { onMount, createEventDispatcher } from 'svelte';
	import { SpeedDial, SpeedDialButton } from 'flowbite-svelte';
	import {
		CirclePlusSolid,
		CloseCircleSolid
	} from 'flowbite-svelte-icons';

	export let game: Game;
	export let shareId: string;

	const dispatch = createEventDispatcher();

	let isAddRoundMenuDisplayed: Boolean = false;

	const toggleAddRoundMenu = () => {
		isAddRoundMenuDisplayed = !isAddRoundMenuDisplayed;
	};

	const handleRoundAdded = (e: CustomEvent<string>) => {
		toggleAddRoundMenu();
		dispatch('roundAdded', e.detail);
	};

	const handleDeleteRound = () => {
		dispatch('roundDeleted');
	};
</script>

<main>
	{#if isAddRoundMenuDisplayed}
		<AddRoundMenu {game} {shareId} on:roundAdded={handleRoundAdded}></AddRoundMenu>
	{:else}
		<SpeedDial defaultClass="absolute end-6 bottom-6" pill={false} tooltip="none" textOutside>
			<SpeedDialButton name="delete" on:click={handleDeleteRound}>
				<CloseCircleSolid class="h-6 w-6" />
			</SpeedDialButton>
			<SpeedDialButton name="add" on:click={toggleAddRoundMenu}>
				<CirclePlusSolid class="h-6 w-6" />
			</SpeedDialButton>
			
		</SpeedDial>
	{/if}
</main>

<style scoped>
	/* Custom CSS to make the button span full width */
	.full-width-btn {
		width: 100%;
	}
</style>
