<script lang="ts">
	import type { Game } from '$lib/domain/game';
	import AddRoundMenu from './addRoundMenu.svelte';
	import { onDestroy } from 'svelte';

	let { onRoundAdded, onRoundDeleted, game = $bindable() , shareId} = $props();

	let isAddRoundMenuDisplayed: boolean = $state(false);
	let isConfirming: boolean = $state(false);
	let confirmTimeout: ReturnType<typeof setTimeout> | undefined;

	const toggleAddRoundMenu = () => {
		isAddRoundMenuDisplayed = !isAddRoundMenuDisplayed;
		console.log('isAddRoundMenuDisplayed:', isAddRoundMenuDisplayed);
	};

	const handleRoundAddedAndToggleMenu = (e: CustomEvent<string>) => {
		toggleAddRoundMenu();
		onRoundAdded(e.detail);
	};

	const handleUndoClick = () => {
		if (isConfirming) {
			if (confirmTimeout) {
				clearTimeout(confirmTimeout);
				confirmTimeout = undefined;
			}
			isConfirming = false;
			onRoundDeleted();
		} else {
			isConfirming = true;
			confirmTimeout = setTimeout(() => {
				isConfirming = false;
			}, 3000);
		}
	};

	onDestroy(() => {
		if (confirmTimeout) {
			clearTimeout(confirmTimeout);
		}
	});
</script>

{#if isAddRoundMenuDisplayed}
	<AddRoundMenu {game} {shareId} on:roundAdded={handleRoundAddedAndToggleMenu} on:close={toggleAddRoundMenu} />
{:else}
	<div class="fixed bottom-6 right-6 flex items-center gap-3 z-30">
		<!-- Undo Last Round -->
		<button 
			type="button" 
			onclick={handleUndoClick}
			title={isConfirming ? "Confirm undo last round" : "Undo last round"}
			class="w-12 h-12 flex items-center justify-center rounded-full backdrop-blur-md border transition-all duration-200 shadow-xl active:scale-90 {
				isConfirming 
				? 'bg-red-600 border-red-500 text-white shadow-red-600/30 scale-110 animate-pulse' 
				: 'bg-[#1a1a2e]/85 border-red-500/20 text-red-400 hover:bg-red-500/10 hover:border-red-500/40 shadow-black/40'
			}"
		>
			{#if isConfirming}
				<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-5 h-5">
					<path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5" />
				</svg>
			{:else}
				<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5">
					<path stroke-linecap="round" stroke-linejoin="round" d="M9 15 3 9m0 0 6-6M3 9h12a6 6 0 0 1 0 12h-3" />
				</svg>
			{/if}
		</button>

		<!-- Add Round -->
		<button 
			type="button" 
			onclick={toggleAddRoundMenu}
			title="Add round"
			class="w-14 h-14 flex items-center justify-center rounded-full bg-gradient-to-r from-[#FE795D] to-[#EB4F27] text-white hover:scale-105 active:scale-95 transition-all duration-200 shadow-xl shadow-[#FE795D]/25 hover:shadow-[#FE795D]/45"
		>
			<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-6 h-6">
				<path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
			</svg>
		</button>
	</div>
{/if}
