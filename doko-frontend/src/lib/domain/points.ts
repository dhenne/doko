import type { ReOrContra } from './reOrContra';

export const calculatePointsFromValue = (value: number | undefined, reOrContra: ReOrContra): number => {
	if (value === undefined) {
		console.log('no value given to calculate points from');
		return 0;
	}

	const isReWinner = value > 120;
	const winnerPoints = isReWinner ? 1 : 2;
	const absoluteValue = Math.abs(value - 120);
	const absagePoints = Math.max(
		0,
		Math.floor(absoluteValue / 30) - (absoluteValue % 30 === 0 && absoluteValue < 120 ? 1 : 0)
	);

	const returnPoints = absagePoints + winnerPoints;

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

export const calculateValueForParty = (value: number | undefined, reOrContra: ReOrContra): number => {
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
