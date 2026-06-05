import { describe, it, expect } from 'vitest';
import { calculatePointsFromValue, calculateValueForParty } from './points';

describe('calculatePointsFromValue', () => {
    describe('Re party wins (value > 120)', () => {
        it('calculates 120 (Re tie / Contra wins)', () => {
            expect(calculatePointsFromValue(120, 'RE')).toBe(-2);
            expect(calculatePointsFromValue(120, 'CONTRA')).toBe(2);
        });

        it('calculates 121 (Re win)', () => {
            expect(calculatePointsFromValue(121, 'RE')).toBe(1);
            expect(calculatePointsFromValue(121, 'CONTRA')).toBe(-1);
        });

        it('calculates 150 (Re win, no "keine 90" yet)', () => {
            expect(calculatePointsFromValue(150, 'RE')).toBe(1);
            expect(calculatePointsFromValue(150, 'CONTRA')).toBe(-1);
        });

        it('calculates 151 (Re win, "keine 90")', () => {
            expect(calculatePointsFromValue(151, 'RE')).toBe(2);
            expect(calculatePointsFromValue(151, 'CONTRA')).toBe(-2);
        });

        it('calculates 180 (Re win, no "keine 60" yet)', () => {
            expect(calculatePointsFromValue(180, 'RE')).toBe(2);
            expect(calculatePointsFromValue(180, 'CONTRA')).toBe(-2);
        });

        it('calculates 181 (Re win, "keine 60")', () => {
            expect(calculatePointsFromValue(181, 'RE')).toBe(3);
            expect(calculatePointsFromValue(181, 'CONTRA')).toBe(-3);
        });

        it('calculates 210 (Re win, no "keine 30" yet)', () => {
            expect(calculatePointsFromValue(210, 'RE')).toBe(3);
            expect(calculatePointsFromValue(210, 'CONTRA')).toBe(-3);
        });

        it('calculates 211 (Re win, "keine 30")', () => {
            expect(calculatePointsFromValue(211, 'RE')).toBe(4);
            expect(calculatePointsFromValue(211, 'CONTRA')).toBe(-4);
        });

        it('calculates 240 (Re win, "schwarz" / Contra 0 points)', () => {
            expect(calculatePointsFromValue(240, 'RE')).toBe(5);
            expect(calculatePointsFromValue(240, 'CONTRA')).toBe(-5);
        });
    });

    describe('Contra party wins (value < 120)', () => {
        it('calculates 90 (Contra wins, no "keine 90" yet for Contra)', () => {
            expect(calculatePointsFromValue(90, 'RE')).toBe(-2);
            expect(calculatePointsFromValue(90, 'CONTRA')).toBe(2);
        });

        it('calculates 89 (Contra wins, "keine 90" for Contra)', () => {
            expect(calculatePointsFromValue(89, 'RE')).toBe(-3);
            expect(calculatePointsFromValue(89, 'CONTRA')).toBe(3);
        });

        it('calculates 60 (Contra wins, no "keine 60" yet for Contra)', () => {
            expect(calculatePointsFromValue(60, 'RE')).toBe(-3);
            expect(calculatePointsFromValue(60, 'CONTRA')).toBe(3);
        });

        it('calculates 59 (Contra wins, "keine 60" for Contra)', () => {
            expect(calculatePointsFromValue(59, 'RE')).toBe(-4);
            expect(calculatePointsFromValue(59, 'CONTRA')).toBe(4);
        });

        it('calculates 30 (Contra wins, no "keine 30" yet for Contra)', () => {
            expect(calculatePointsFromValue(30, 'RE')).toBe(-4);
            expect(calculatePointsFromValue(30, 'CONTRA')).toBe(4);
        });

        it('calculates 29 (Contra wins, "keine 30" for Contra)', () => {
            expect(calculatePointsFromValue(29, 'RE')).toBe(-5);
            expect(calculatePointsFromValue(29, 'CONTRA')).toBe(5);
        });

        it('calculates 0 (Contra wins, "schwarz" / Re 0 points)', () => {
            expect(calculatePointsFromValue(0, 'RE')).toBe(-6);
            expect(calculatePointsFromValue(0, 'CONTRA')).toBe(6);
        });
    });
});

describe('calculateValueForParty', () => {
    it('returns value for RE', () => {
        expect(calculateValueForParty(150, 'RE')).toBe(150);
    });

    it('returns 240 - value for CONTRA', () => {
        expect(calculateValueForParty(150, 'CONTRA')).toBe(90);
    });

    it('returns 0 for PAUSE', () => {
        expect(calculateValueForParty(150, 'PAUSE')).toBe(0);
    });
});
