<script lang="ts">
	import type { Game } from '$lib/domain/game';
	import type { ReOrContra, RE, CONTRA, PAUSE } from '$lib/domain/reOrContra';
	import type { Points } from '$lib/adapter/backend';
	import { postNewRoundForGame } from '$lib/adapter/backend';
	import { mapToReOrContra } from '$lib/domain/reOrContra';
	import RangeSlider from 'svelte-range-slider-pips';
	import { onMount, createEventDispatcher } from 'svelte';

	let { game = $bindable(), shareId } = $props<{ game: Game; shareId: string }>();

	let regularValue = $state<Array<number>>([120]);
	let specialPointsRe = $state<Array<number>>([0]);
	let specialPointsContra = $state<Array<number>>([0]);

	type PlayerReOrContra = { [key: string]: ReOrContra };
	let playerReOrContra = $state<PlayerReOrContra>({});
	let cardValueOfPlayerParty = $state<Points>({});
	let points = $state<Points>({});

	const dispatch = createEventDispatcher();

	onMount(() => {
		game.players.forEach((player) => {
			playerReOrContra[player.name] = 'PAUSE';
			points[player.name] = 0;
			cardValueOfPlayerParty[player.name] = 0;
		});
		updatePoints();
	});

	const pointSliderProps = {
		pips: true,
		pipstep: 30,
		min: 0,
		max: 240,
		float: true,
		all: 'label'
	};

	const handleAddRound = async () => {
		const response: Response = await postNewRoundForGame(shareId, points);
		if (response.status === 201) {
			const locationUrl = response.headers.get("Location");
			dispatch('roundAdded', locationUrl);
		}
	};

	const calculatePointsFromValue = (value: number | undefined, reOrContra: ReOrContra) => {
		if (value === undefined) {
			console.log('no value given to calculate points from');
			return 0;
		}

		const isReWinner = value > 120;
		const winnerPoints = isReWinner ? 1 : 2;
		const absoluteValue = Math.abs(value - 120);
		const absagePoints = Math.max(
			0,
			Math.floor(absoluteValue / 30) - (absoluteValue % 30 == 0 ? 1 : 0)
		);

		let returnPoints = absagePoints + winnerPoints;

		switch (reOrContra) {
			case 'RE': {
				return returnPoints * (isReWinner ? 1 : -1);
			}
			case 'CONTRA': {
				return returnPoints * (isReWinner ? -1 : 1);
			}
			default: {
				return 0;
			}
		}
	};

	const calculateValueForParty = (value: number | undefined, reOrContra: ReOrContra) => {
		if (value === undefined) {
			console.log('no value given to calculate points from');
			return 0;
		}

		switch (reOrContra) {
			case 'RE': {
				return value;
			}
			case 'CONTRA': {
				return 240 - value;
			}
			default: {
				return 0;
			}
		}
	};

	const partyAry: Array<ReOrContra> = ['RE', 'CONTRA'];

	const isSolo = () => {
		const nrOfRe = Object.keys(playerReOrContra)
			.map((k) => playerReOrContra[k])
			.filter((reOrContra) => reOrContra === 'RE').length;
		const nrOfContra = Object.keys(playerReOrContra)
			.map((k) => playerReOrContra[k])
			.filter((reOrContra) => reOrContra === 'CONTRA').length;

		return nrOfRe === 1 && nrOfContra === 3;
	};

	const updatePoints = () => {
		partyAry.forEach((party) => {
			game.players
				.filter((player) => playerReOrContra[player.name] === party)
				.forEach((player) => {
					if (
						regularValue[0] === undefined ||
						specialPointsRe[0] === undefined ||
						specialPointsContra[0] === undefined
					) {
						return;
					}

					const specialPoints =
						party === 'RE'
							? specialPointsRe[0] - specialPointsContra[0]
							: specialPointsContra[0] - specialPointsRe[0];

					points[player.name] = calculatePointsFromValue(regularValue[0], party) + specialPoints;
					cardValueOfPlayerParty[player.name] = calculateValueForParty(regularValue[0], party);
				});
		});

		game.players
			.filter((p) => playerReOrContra[p.name] === 'PAUSE')
			.forEach((player) => {
				points[player.name] = 0;
				cardValueOfPlayerParty[player.name] = 0;
			});

		if (isSolo()) {
			const rePlayer = Object.keys(playerReOrContra)
				.map((k) => {
					return { player: k, reOrContra: playerReOrContra[k] };
				})
				.filter((o) => o.reOrContra == 'RE')
				.map((o) => o.player)
				.at(0);

			if (rePlayer) {
				points[rePlayer] = points[rePlayer] * 3;
			}
		}
	};

	const handleReCardValueChange = (event: Event & { currentTarget: EventTarget & HTMLInputElement }) => {
		const val = parseInt(event.currentTarget.value);
		if (!isNaN(val) && val >= 0 && val <= 240) {
			regularValue[0] = val;
			updatePoints();
		}
	};
</script>

<div class="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/60 backdrop-blur-sm p-0 sm:p-4">
	<!-- Modal Sheet container -->
	<div class="w-full sm:max-w-md bg-[#0f0f1a] border-t sm:border border-white/5 rounded-t-3xl sm:rounded-3xl p-5 shadow-2xl flex flex-col max-h-[90vh] overflow-y-auto scrollbar-thin">
		
		<!-- Modal Header -->
		<div class="flex items-center justify-between border-b border-white/5 pb-3 mb-4">
			<h3 class="text-lg font-bold text-white uppercase tracking-wider">New Round</h3>
			<button 
				type="button" 
				onclick={() => dispatch('close')} 
				class="w-8 h-8 flex items-center justify-center rounded-full text-slate-400 hover:text-white hover:bg-white/5 transition-all"
				title="Close"
			>
				<svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
					<path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
				</svg>
			</button>
		</div>

		<!-- Players Roles Selector List -->
		<div class="flex flex-col gap-3.5 mb-5">
			<span class="text-xs font-semibold uppercase tracking-wider text-slate-500">Player Roles</span>
			{#each game.players as player}
				<div class="flex items-center justify-between bg-[#1a1a2e]/35 border border-white/5 rounded-2xl p-3">
					<div class="flex flex-col gap-0.5">
						<span class="font-bold text-white text-sm">{player.name}</span>
						{#if points[player.name] !== undefined}
							<span class="text-xs font-bold {points[player.name] > 0 ? 'text-emerald-400' : points[player.name] < 0 ? 'text-[#FE795D]' : 'text-slate-500'}">
								{points[player.name] > 0 ? '+' : ''}{points[player.name]} pts
							</span>
						{/if}
					</div>
					
					<!-- Segmented Button Toggle -->
					<div class="flex rounded-xl bg-[#080810]/75 p-0.5 border border-slate-800/70">
						<button 
							type="button"
							class="px-3 py-1.5 text-xs font-extrabold uppercase rounded-lg transition-all duration-150 {playerReOrContra[player.name] === 'RE' ? 'bg-gradient-to-r from-[#FE795D] to-[#EB4F27] text-white shadow-md shadow-[#FE795D]/25' : 'text-slate-500 hover:text-slate-300'}"
							onclick={() => { playerReOrContra[player.name] = 'RE'; updatePoints(); }}
						>
							Re
						</button>
						<button 
							type="button"
							class="px-3 py-1.5 text-xs font-extrabold uppercase rounded-lg transition-all duration-150 {playerReOrContra[player.name] === 'PAUSE' ? 'bg-slate-800 text-slate-300' : 'text-slate-500 hover:text-slate-300'}"
							onclick={() => { playerReOrContra[player.name] = 'PAUSE'; updatePoints(); }}
						>
							Pause
						</button>
						<button 
							type="button"
							class="px-3 py-1.5 text-xs font-extrabold uppercase rounded-lg transition-all duration-150 {playerReOrContra[player.name] === 'CONTRA' ? 'bg-blue-600 text-white shadow-md shadow-blue-500/25' : 'text-slate-500 hover:text-slate-300'}"
							onclick={() => { playerReOrContra[player.name] = 'CONTRA'; updatePoints(); }}
						>
							Contra
						</button>
					</div>
				</div>
			{/each}
		</div>

		<!-- Re Card Value Selection -->
		<div class="flex flex-col gap-2.5 bg-[#1a1a2e]/35 border border-white/5 rounded-2xl p-4 mb-4">
			<div class="flex items-center justify-between">
				<span class="text-xs font-semibold uppercase tracking-wider text-slate-500">Re Card Value</span>
				<div class="flex items-center gap-2">
					<input 
						type="number" 
						value={regularValue[0]} 
						oninput={handleReCardValueChange}
						min="0" 
						max="240" 
						class="w-16 bg-[#080810]/70 border border-slate-800 focus:border-[#FE795D] rounded-xl px-2 py-1 text-center text-sm text-white font-bold focus:outline-none"
					/>
					<span class="text-xs text-slate-500">/ 240</span>
				</div>
			</div>
			<div class="mt-1">
				<RangeSlider bind:values={regularValue} {...pointSliderProps} on:change={updatePoints} />
			</div>
		</div>

		<!-- Special Points Steppers -->
		<div class="grid grid-cols-2 gap-3 mb-6">
			<!-- Re Special Points -->
			<div class="flex flex-col gap-2 bg-[#1a1a2e]/35 border border-white/5 rounded-2xl p-3.5 items-center">
				<span class="text-xs font-semibold text-slate-400 text-center">Re Special pts</span>
				<div class="flex items-center gap-3.5 mt-1">
					<button 
						type="button" 
						onclick={() => { if (specialPointsRe[0] > 0) { specialPointsRe[0]--; updatePoints(); } }} 
						class="w-8 h-8 rounded-full bg-slate-900 border border-slate-800 text-slate-400 hover:text-white hover:border-slate-700 active:scale-90 flex items-center justify-center font-bold text-lg"
					>
						-
					</button>
					<span class="w-4 text-center font-bold text-white text-base">{specialPointsRe[0]}</span>
					<button 
						type="button" 
						onclick={() => { if (specialPointsRe[0] < 20) { specialPointsRe[0]++; updatePoints(); } }} 
						class="w-8 h-8 rounded-full bg-[#FE795D]/5 border border-[#FE795D]/25 text-[#FE795D] hover:bg-[#FE795D]/15 hover:border-[#FE795D]/45 active:scale-90 flex items-center justify-center font-bold text-lg"
					>
						+
					</button>
				</div>
			</div>

			<!-- Contra Special Points -->
			<div class="flex flex-col gap-2 bg-[#1a1a2e]/35 border border-white/5 rounded-2xl p-3.5 items-center">
				<span class="text-xs font-semibold text-slate-400 text-center">Contra Special pts</span>
				<div class="flex items-center gap-3.5 mt-1">
					<button 
						type="button" 
						onclick={() => { if (specialPointsContra[0] > 0) { specialPointsContra[0]--; updatePoints(); } }} 
						class="w-8 h-8 rounded-full bg-slate-900 border border-slate-800 text-slate-400 hover:text-white hover:border-slate-700 active:scale-90 flex items-center justify-center font-bold text-lg"
					>
						-
					</button>
					<span class="w-4 text-center font-bold text-white text-base">{specialPointsContra[0]}</span>
					<button 
						type="button" 
						onclick={() => { if (specialPointsContra[0] < 20) { specialPointsContra[0]++; updatePoints(); } }} 
						class="w-8 h-8 rounded-full bg-[#FE795D]/5 border border-[#FE795D]/25 text-[#FE795D] hover:bg-[#FE795D]/15 hover:border-[#FE795D]/45 active:scale-90 flex items-center justify-center font-bold text-lg"
					>
						+
					</button>
				</div>
			</div>
		</div>

		<!-- Action buttons -->
		<div class="flex gap-3 mt-auto">
			<button 
				type="button" 
				onclick={() => dispatch('close')} 
				class="flex-1 py-3 px-4 rounded-2xl font-bold uppercase tracking-wider text-sm bg-slate-900 border border-slate-800 text-slate-400 hover:bg-slate-800 hover:text-white active:scale-98 transition-all"
			>
				Cancel
			</button>
			<button 
				type="button" 
				onclick={handleAddRound}
				class="flex-1 py-3 px-4 rounded-2xl font-bold uppercase tracking-wider text-sm text-white bg-gradient-to-r from-[#FE795D] to-[#EB4F27] hover:scale-[1.02] active:scale-[0.98] transition-all shadow-lg shadow-[#FE795D]/20 hover:shadow-[#FE795D]/35"
			>
				Add Round
			</button>
		</div>

	</div>
</div>
