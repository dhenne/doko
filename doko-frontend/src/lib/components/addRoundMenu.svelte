<script lang="ts">
	import type { Game } from '$lib/domain/game';
	import type { ReOrContra, RE, CONTRA, PAUSE } from '$lib/domain/reOrContra';
	import type { Points } from '$lib/adapter/backend';
	import { postNewRoundForGame } from '$lib/adapter/backend';
	import { mapToReOrContra } from '$lib/domain/reOrContra';
	import RangeSlider from 'svelte-range-slider-pips';
	import { onMount, createEventDispatcher } from 'svelte';

	export let game: Game;
	export let shareId: string;

	type PlayerPartySliderChangeHandler = (event: any) => void;
	type PlayerPartySliderChangeHandlers = { [key: string]: PlayerPartySliderChangeHandler };
	let handlePlayerPartySliderChangers: PlayerPartySliderChangeHandlers = {};

	type ChangedPlayerCardValueHandler = (
		event: Event & { currentTarget: EventTarget & HTMLInputElement }
	) => void;
	type ChangedPlayerCardValueHandlers = { [key: string]: ChangedPlayerCardValueHandler };
	let handleChangedPlayerCardValue: ChangedPlayerCardValueHandlers = {};

	let regularValue: Array<number> = [120];
	let specialPointsRe: Array<number> = [0];
	let specialPointsContra: Array<number> = [0];

	type PlayerReOrContra = { [key: string]: ReOrContra };

	let playerReOrContra: PlayerReOrContra = {};

	const cardValueOfPlayerParty: Points = {};
	const points: Points = {};

	const dispatch = createEventDispatcher();

	onMount(() => {
		game.players.forEach((player) => {
			handlePlayerPartySliderChangers[player.name] = (e: CustomEvent) => {
				playerReOrContra[player.name] = mapToReOrContra(e.detail.value);
			};
			handleChangedPlayerCardValue[player.name] = (
				event: Event & { currentTarget: EventTarget & HTMLInputElement }
			) => {
				
				const value = parseInt(event.currentTarget.value)
				console.log(`changed value to: ${value}`)
				switch (playerReOrContra[player.name]) {
					case 'RE': {
						regularValue[0] = value;
						break;
					}
					case 'CONTRA': {
						regularValue[0] = 240 - value;
						break;
					}
				}
				updatePoints();
			};
		});
	});

	const pointSliderProps = {
		pips: true,
		pipstep: 30,
		min: 0,
		max: 240,
		float: true,
		all: 'label'
	};

	const specialPointSliderProps = {
		pips: true,
		pipstep: 30,
		min: 0,
		max: 20,
		float: true,
		all: 'label'
	};

	const playerPartySliderProps = {
		min: -1,
		max: 1,
		float: true,
		formatter: mapToReOrContra
	};

	const handleAddRound = async () => {
		const responese: Response = await postNewRoundForGame(shareId, points);
		if (responese.status === 201) {
			dispatch('roundAdded', points);
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
						regularValue.at(0) === undefined ||
						specialPointsRe.at(0) === undefined ||
						specialPointsContra.at(0) === undefined
					) {
						return;
					}

					const specialPoints =
						party === 'RE'
							? specialPointsRe.at(0)! - specialPointsContra.at(0)!
							: specialPointsContra.at(0)! - specialPointsRe.at(0)!;

					points[player.name] = calculatePointsFromValue(regularValue.at(0), party) + specialPoints;
					cardValueOfPlayerParty[player.name] = calculateValueForParty(regularValue.at(0), party);
				});
		});

		game.players
			.filter((p) => playerReOrContra[p.name] === 'PAUSE')
			.forEach((player) => {
				points[player.name] = 0;
			});

		if (isSolo()) {
			const rePlayer = Object.keys(playerReOrContra)
				.map((k) => {
					return { player: k, reOrContra: playerReOrContra[k] };
				})
				.filter((o) => o.reOrContra == 'RE')
				.map((o) => o.player)
				.at(0);

			points[rePlayer!] = points[rePlayer!] * 3;
		}

		console.log(points);
	};
</script>

<main>
	<div class="playerPartyContainer">
		{#each game.players as player}
			<div class="playerContainer">
				<div class="playerPartyContainerElement">
					<p>{player.name}</p>
				</div>
				<div class="playerPartyContainerElement playerSlider">
					<RangeSlider
						{...playerPartySliderProps}
						on:change={handlePlayerPartySliderChangers[player.name]}
						on:stop={updatePoints}
					/>
				</div>
				{#if points[player.name] !== undefined}
					<div class="playerPartyContainerElement pointDisplay">
						<input class="playerCardsValueInput"
							type="number"
							bind:value={cardValueOfPlayerParty[player.name]}
							on:change={handleChangedPlayerCardValue[player.name]}
							min="0"
							max="240"
						/>
					</div>
					<div class="playerPartyContainerElement pointDisplay">
						<p>points:</p>
					</div>
					<div class="playerPartyContainerElement pointDisplay">
						<p>{points[player.name]}</p>
					</div>
				{/if}
			</div>
		{/each}
	</div>
	<div>
		<p>card value re</p>
		<RangeSlider bind:values={regularValue} {...pointSliderProps} on:change={updatePoints} />
	</div>
	<div>
		<p>special points re</p>
		<RangeSlider
			bind:values={specialPointsRe}
			{...specialPointSliderProps}
			on:stop={updatePoints}
		/>
	</div>
	<div>
		<p>special points contra</p>
		<RangeSlider
			bind:values={specialPointsContra}
			{...specialPointSliderProps}
			on:stop={updatePoints}
		/>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col">
				<button class="btn btn-primary full-width-btn" on:click={handleAddRound}>add round</button>
			</div>
		</div>
	</div>
</main>

<style scoped>
	.playerSlider {
		width: 100px;
	}

	.playerPartyContainerElement {
		float: left;
		padding: 10px;
	}

	.playerContainer {
		width: 100%;
		display: inline-block;
	}

	.playerContainer:last-child {
		clear: both;
	}

	.full-width-btn {
		width: 100%;
	}

	.playerCardsValueInput {
		max-width: 6ch;
	}
</style>
