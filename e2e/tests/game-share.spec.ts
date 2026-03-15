import { test, expect } from '@playwright/test';
import { createGame, getPlayerNames } from './helpers';

test.describe('Game Share/Load', () => {
  test('loads a shared game by shareid', async ({ page }) => {
    const players = ['Alice', 'Bob', 'Charlie', 'Diana'];
    const shareId = await createGame(page, players);

    // Navigate to the shared URL in the same page (simulates opening a shared link)
    await page.goto(`/?shareid=${shareId}`);

    // Wait for the game to load (async fetch in onMount)
    await expect(page.locator('table')).toBeVisible({ timeout: 10_000 });

    // Verify the game loads with correct player names
    const displayedNames = await getPlayerNames(page);
    expect(displayedNames).toEqual(players);
  });

  test('loads a shared game in a new browser context', async ({ browser }) => {
    const context1 = await browser.newContext();
    const page1 = await context1.newPage();

    const players = ['Alice', 'Bob', 'Charlie', 'Diana'];
    const shareId = await createGame(page1, players);

    // Open the shared link in a completely new context (different browser session)
    const context2 = await browser.newContext();
    const page2 = await context2.newPage();
    await page2.goto(`http://doko.localhost/?shareid=${shareId}`);

    // Wait for the game to load (async fetch in onMount)
    await expect(page2.locator('table')).toBeVisible({ timeout: 10_000 });

    // Verify the game loads correctly in the new context
    const displayedNames = await getPlayerNames(page2);
    expect(displayedNames).toEqual(players);

    await context1.close();
    await context2.close();
  });

  test('shows create game link when no shareid is provided', async ({ page }) => {
    await page.goto('/');

    await expect(page.getByRole('link', { name: 'Create new game' })).toBeVisible();
  });
});
