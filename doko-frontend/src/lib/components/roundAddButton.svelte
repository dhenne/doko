<script lang="ts">
	import type { Game } from '$lib/domain/game';
	import AddRoundMenu from './addRoundMenu.svelte';
	import type { Points } from '$lib/adapter/backend';
	import { createEventDispatcher } from 'svelte';
	import { SpeedDialTrigger, SpeedDial, SpeedDialButton } from 'flowbite-svelte';
	import { CirclePlusSolid, CloseCircleSolid } from 'flowbite-svelte-icons';

	let { onRoundAdded, onRoundDeleted, game = $bindable() , shareId} = $props();

	let isAddRoundMenuDisplayed: boolean = $state(false);

	const toggleAddRoundMenu = () => {
		isAddRoundMenuDisplayed = !isAddRoundMenuDisplayed;
		console.log('isAddRoundMenuDisplayed:', isAddRoundMenuDisplayed);
	};

	const handleRoundAddedAndToggleMenu = (e: CustomEvent<string>) => {
		toggleAddRoundMenu();
		onRoundAdded(e.detail);
	};

	
</script>

{#if isAddRoundMenuDisplayed}
	<AddRoundMenu {game} {shareId} on:roundAdded={handleRoundAddedAndToggleMenu} />
{:else}
	<SpeedDialTrigger class="absolute end-6 bottom-6" />
	<SpeedDial pill={false} tooltip="none" textOutside>
		<SpeedDialButton name="delete">
			<!-- place a native button inside the component slot so clicks are handled reliably -->
			<button type="button" class="p-1" onclick={onRoundDeleted}>
				<CloseCircleSolid class="h-6 w-6" />
			</button>
		</SpeedDialButton>
		<SpeedDialButton name="add">
			<button type="button" class="p-1" onclick={toggleAddRoundMenu}>
				<CirclePlusSolid class="h-6 w-6" />
			</button>
		</SpeedDialButton>
	</SpeedDial>
{/if}
