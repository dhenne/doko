import { test, expect } from '@playwright/test';
import { createGame, getScoreRows, getPlayerNames } from './helpers';

test.describe('Round Management', () => {
  test('adds a round with default scores and displays it', async ({ page }) => {
    const players = ['Alice', 'Bob', 'Charlie', 'Diana'];
    await createGame(page, players);

    // Open the SpeedDial menu
    await page.getByRole('button', { name: 'Open actions menu' }).click();
    // Click the add round button (CirclePlusSolid inside SpeedDialButton name="add")
    await page.getByRole('button', { name: 'add', exact: true }).click();

    // The AddRoundMenu should now be visible
    // Set player parties using the range sliders
    // Each player has a slider with min=-1 (CONTRA), 0 (PAUSE), max=1 (RE)
    const playerContainers = page.locator('.playerContainer');

    // Set first two players to RE (slide right)
    for (let i = 0; i < 2; i++) {
      const slider = playerContainers.nth(i).locator('.rangeSlider');
      const box = await slider.boundingBox();
      if (box) {
        await page.mouse.click(box.x + box.width * 0.9, box.y + box.height / 2);
      }
    }

    // Set last two players to CONTRA (slide left)
    for (let i = 2; i < 4; i++) {
      const slider = playerContainers.nth(i).locator('.rangeSlider');
      const box = await slider.boundingBox();
      if (box) {
        await page.mouse.click(box.x + box.width * 0.1, box.y + box.height / 2);
      }
    }

    // Click the "add round" button
    await page.getByRole('button', { name: 'add round' }).click();

    // After adding, the menu closes and we should see the round in the table
    await expect(page.locator('table tbody tr')).toHaveCount(1, { timeout: 5000 });

    // Verify scores exist in the row
    const rows = await getScoreRows(page);
    expect(rows).toHaveLength(1);
    expect(rows[0]).toHaveLength(4); // one score per player
  });

  test('deletes the last round', async ({ page }) => {
    const players = ['Alice', 'Bob', 'Charlie', 'Diana'];
    await createGame(page, players);

    // Add a round first
    await page.getByRole('button', { name: 'Open actions menu' }).click();
    await page.getByRole('button', { name: 'add', exact: true }).click();

    // Set parties
    const playerContainers = page.locator('.playerContainer');
    for (let i = 0; i < 2; i++) {
      const slider = playerContainers.nth(i).locator('.rangeSlider');
      const box = await slider.boundingBox();
      if (box) {
        await page.mouse.click(box.x + box.width * 0.9, box.y + box.height / 2);
      }
    }
    for (let i = 2; i < 4; i++) {
      const slider = playerContainers.nth(i).locator('.rangeSlider');
      const box = await slider.boundingBox();
      if (box) {
        await page.mouse.click(box.x + box.width * 0.1, box.y + box.height / 2);
      }
    }

    await page.getByRole('button', { name: 'add round' }).click();

    // Wait for the round to appear
    await expect(page.locator('table tbody tr')).toHaveCount(1, { timeout: 5000 });

    // Now delete the round via SpeedDial
    await page.getByRole('button', { name: 'Open actions menu' }).click();
    await page.getByRole('button', { name: 'delete', exact: true }).click();

    // The round should be removed
    await expect(page.locator('table tbody tr')).toHaveCount(0, { timeout: 5000 });
  });
});
