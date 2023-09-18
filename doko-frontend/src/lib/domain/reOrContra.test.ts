import { describe, it, expect } from 'vitest';
import { mapToReOrContra } from './reOrContra';

describe('mapToReOrContra', () => {
    it('maps -1 to RE', () => {
        expect(mapToReOrContra(-1)).toBe("RE");
    });
    it('maps 1 to CONTRA', () => {
        expect(mapToReOrContra(1)).toBe("CONTRA");
    });
    it('maps others to PAUSE', () => {

        [-2, 0, 2].forEach(nr => {
            expect(mapToReOrContra(nr)).toBe("PAUSE");
        });

    });
});
