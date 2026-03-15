import { type Page, expect } from '@playwright/test';

/**
 * Creates a new game with the given player names via the UI.
 * Returns the shareId extracted from the resulting URL.
 */
export async function createGame(page: Page, playerNames: string[]): Promise<string> {
  await page.goto('/game/create');

  // Add extra player fields if more than 4 players
  const addButton = page.getByRole('button', { name: '+' });
  for (let i = 4; i < playerNames.length; i++) {
    await addButton.click();
  }

  // Fill in player names
  for (let i = 0; i < playerNames.length; i++) {
    await page.locator(`input[name="playerNames[${i}]"]`).fill(playerNames[i]);
  }

  // Submit the form
  await page.getByRole('button', { name: 'Start game' }).click();

  // Wait for navigation to game page
  await page.waitForURL(/\/\?shareid=.+/);

  const url = new URL(page.url());
  const shareId = url.searchParams.get('shareid');
  if (!shareId) {
    throw new Error('No shareid found in URL after game creation');
  }

  return shareId;
}

/**
 * Reads player names from the game screen table headers.
 */
export async function getPlayerNames(page: Page): Promise<string[]> {
  const headers = page.locator('table thead th');
  const allHeaders = await headers.allTextContents();
  // First header is "#", rest are player names
  return allHeaders.slice(1);
}

/**
 * Reads all score rows from the game screen table.
 * Returns an array of rows, each row being an array of point values.
 */
export async function getScoreRows(page: Page): Promise<number[][]> {
  const rows = page.locator('table tbody tr');
  const count = await rows.count();
  const result: number[][] = [];

  for (let i = 0; i < count; i++) {
    const cells = rows.nth(i).locator('td');
    const cellTexts = await cells.allTextContents();
    result.push(cellTexts.map((t) => parseInt(t, 10)));
  }

  return result;
}
