<script lang="ts">
	import { goto } from '$app/navigation';

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

	let nrOfPlayers: number = 4;

	const increment = () => {
		nrOfPlayers += 1;
	};

	const decrement = () => {
		nrOfPlayers -= 1;
	};
</script>

<form method="POST" on:submit|preventDefault={handleSubmit} action="/api/v1/game">
	{#each Array(nrOfPlayers) as _, index (index)}
		<div>
			<label>
				Spieler {index + 1}
				<input name="playerNames[{index}]" type="text" />
			</label>
		</div>
	{/each}
	<div>
		<button type="button" class="btn btn-secondary" on:click={increment}>+</button>
		<button type="button" class="btn btn-secondary" on:click={decrement} disabled={nrOfPlayers <= 4}
			>-</button
		>
	</div>
	<div>
		<button>Start game</button>
	</div>
</form>
