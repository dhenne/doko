import { test, expect } from '@playwright/test';
import { createGame, getPlayerNames } from './helpers';

test.describe('Game Creation', () => {
  test('creates a game with 4 players and redirects to game screen', async ({ page }) => {
    const players = ['Alice', 'Bob', 'Charlie', 'Diana'];
    const shareId = await createGame(page, players);

    expect(shareId).toBeTruthy();

    // Verify all player names appear in the table headers
    const displayedNames = await getPlayerNames(page);
    expect(displayedNames).toEqual(players);
  });

  test('can add a 5th player field', async ({ page }) => {
    await page.goto('/game/create');

    // Initially 4 player inputs
    await expect(page.locator('input[name^="playerNames"]')).toHaveCount(4);

    // Click + to add a 5th
    await page.getByRole('button', { name: '+' }).click();

    await expect(page.locator('input[name^="playerNames"]')).toHaveCount(5);
    await expect(page.locator('input[name="playerNames[4]"]')).toBeVisible();
  });

  test('cannot remove player fields below 4', async ({ page }) => {
    await page.goto('/game/create');

    const removeButton = page.getByRole('button', { name: '-' });
    await expect(removeButton).toBeDisabled();
  });

  test('can remove a player field after adding one', async ({ page }) => {
    await page.goto('/game/create');

    // Add a 5th player
    await page.getByRole('button', { name: '+' }).click();
    await expect(page.locator('input[name^="playerNames"]')).toHaveCount(5);

    // Remove it
    const removeButton = page.getByRole('button', { name: '-' });
    await expect(removeButton).toBeEnabled();
    await removeButton.click();

    await expect(page.locator('input[name^="playerNames"]')).toHaveCount(4);
  });

  test('submit button is disabled when player names are empty', async ({ page }) => {
    await page.goto('/game/create');

    const submitButton = page.getByRole('button', { name: 'Start game' });
    await expect(submitButton).toBeDisabled();

    // Fill only some names
    await page.locator('input[name="playerNames[0]"]').fill('Alice');
    await page.locator('input[name="playerNames[1]"]').fill('Bob');

    // Still disabled because not all names are filled
    await expect(submitButton).toBeDisabled();

    // Fill remaining names
    await page.locator('input[name="playerNames[2]"]').fill('Charlie');
    await page.locator('input[name="playerNames[3]"]').fill('Diana');

    await expect(submitButton).toBeEnabled();
  });

  test('creates a game with 5 players', async ({ page }) => {
    const players = ['Alice', 'Bob', 'Charlie', 'Diana', 'Eve'];
    const shareId = await createGame(page, players);

    expect(shareId).toBeTruthy();

    const displayedNames = await getPlayerNames(page);
    expect(displayedNames).toEqual(players);
  });
});
