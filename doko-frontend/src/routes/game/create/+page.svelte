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

<form method="POST" on:submit|preventDefault={handleSubmit} action="/api/v1/game">
	{#each Array(nrOfPlayerInputFields) as _, index (index)}
		<div>
			<label>
				Spieler {index + 1}
				<input name="playerNames[{index}]" type="text" on:input={playerNameChangeHandlers[index]} />
			</label>
		</div>
	{/each}
	<div>
		<button type="button" class="btn btn-secondary" on:click={increment}>+</button>
		<button
			type="button"
			class="btn btn-secondary"
			on:click={decrement}
			disabled={nrOfPlayerInputFields <= 4}>-</button
		>
	</div>
	<div>
		<button
			disabled={!isOnlyValidPlayersPresent}
			class="mb-2 me-2 rounded-lg bg-blue-700 px-5 py-2.5 text-sm font-medium text-white hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
			>Start game</button
		>
	</div>
</form>
