<script lang="ts">
	import { goto } from '$app/navigation';
	import { onMount } from 'svelte';

	let nrOfPlayerInputFields: number = 4;
	let playerNameChangeHandlers: Array<any> = [];
	let playerNameLengths: Array<number> = [];
	let isOnlyValidPlayersPresent = false;

	const addPlayerNameChangeHandler = (index: number) => {
		playerNameLengths.push(0);
		updateOnlyValidPlayersPresent();
		return (e: any) => {
			playerNameLengths[index] = e.target!.value.length;
			updateOnlyValidPlayersPresent();
		};
	};

	const removePlayerNamesChangeHandler = () => {
		playerNameChangeHandlers.pop();
		playerNameLengths.pop();
		nrOfPlayerInputFields -= 1;
		updateOnlyValidPlayersPresent();
	};

	onMount(() => {
		for (let ix = 0; ix < nrOfPlayerInputFields; ix++) {
			playerNameChangeHandlers.push(addPlayerNameChangeHandler(ix));
		}
	});

	const increment = () => {
		playerNameChangeHandlers.push(addPlayerNameChangeHandler(nrOfPlayerInputFields++));
	};

	const decrement = removePlayerNamesChangeHandler;

	const updateOnlyValidPlayersPresent = () => {
		isOnlyValidPlayersPresent = playerNameLengths.every((length) => length !== 0);
		console.log('Player name lengths:', playerNameLengths, 'isOnlyValidPlayersPresent:', isOnlyValidPlayersPresent);
	};

	async function handleSubmit(event: Event) {
		const formElement = event.target as HTMLFormElement;
		const method = formElement.method;
		const body = new FormData(formElement);

		const response = await fetch(formElement.action, {
			method,
			body
		});

		const game = await response.json();

		await goto(`/?shareid=${game.shareId}`);
	}
</script>

<div class="flex-1 flex flex-col justify-center items-center px-4 py-8 min-h-[calc(100vh-60px)]">
	<div class="w-full max-w-sm bg-[#1a1a2e]/40 backdrop-blur-md border border-[#FE795D]/15 rounded-3xl p-6 shadow-2xl shadow-[#FE795D]/5">
		
		<div class="text-center mb-6">
			<h2 class="text-2xl font-extrabold tracking-tight text-white uppercase">New Game</h2>
			<p class="text-slate-400 text-sm mt-1">Enter player names to begin scorekeeping</p>
		</div>

		<form method="POST" on:submit|preventDefault={handleSubmit} action="/api/v1/game" class="flex flex-col gap-4">
			<div class="flex flex-col gap-3">
				{#each Array(nrOfPlayerInputFields) as _, index (index)}
					<div class="flex flex-col gap-1.5">
						<label for="player-input-{index}" class="text-xs font-semibold uppercase tracking-wider text-[#FE795D] opacity-90 pl-1">
							Player {index + 1}
						</label>
						<input 
							id="player-input-{index}"
							name="playerNames[{index}]" 
							type="text" 
							placeholder="Enter name"
							on:input={playerNameChangeHandlers[index]}
							class="w-full bg-[#080810]/70 border border-slate-800 focus:border-[#FE795D] rounded-2xl px-4 py-3 text-white text-sm placeholder-slate-600 focus:outline-none focus:ring-1 focus:ring-[#FE795D]/50 transition-all duration-250"
						/>
					</div>
				{/each}
			</div>

			<div class="flex items-center justify-between mt-2 px-1">
				<span class="text-xs font-semibold text-slate-400">Total: {nrOfPlayerInputFields} Players</span>
				<div class="flex gap-2">
					<button 
						type="button" 
						on:click={decrement}
						disabled={nrOfPlayerInputFields <= 4}
						class="w-9 h-9 flex items-center justify-center rounded-full border border-slate-800 text-slate-400 bg-slate-900/40 hover:bg-[#FE795D]/10 hover:text-[#FE795D] hover:border-[#FE795D]/30 active:scale-95 disabled:opacity-40 disabled:hover:bg-transparent disabled:hover:text-slate-400 disabled:hover:border-slate-800 disabled:active:scale-100 transition-all font-bold text-lg"
					>
						-
					</button>
					<button 
						type="button" 
						on:click={increment}
						class="w-9 h-9 flex items-center justify-center rounded-full border border-[#FE795D]/25 text-[#FE795D] bg-[#FE795D]/5 hover:bg-[#FE795D]/20 hover:border-[#FE795D]/45 active:scale-95 transition-all font-bold text-lg"
					>
						+
					</button>
				</div>
			</div>

			<div class="mt-4">
				<button
					disabled={!isOnlyValidPlayersPresent}
					class="w-full relative py-3.5 px-6 font-bold tracking-widest uppercase text-white bg-gradient-to-r from-[#FE795D] to-[#EB4F27] border-none rounded-2xl cursor-pointer transition-all duration-200 disabled:from-slate-800 disabled:to-slate-900 disabled:text-slate-500 disabled:cursor-not-allowed disabled:shadow-none shadow-lg shadow-[#FE795D]/25 hover:shadow-[#FE795D]/45 active:scale-[0.98] focus:outline-none focus:ring-2 focus:ring-[#FE795D]/50 text-sm"
				>
					Start Game
				</button>
			</div>
		</form>
	</div>
</div>
