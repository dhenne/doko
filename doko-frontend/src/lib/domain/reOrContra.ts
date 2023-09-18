export type RE = "RE";
export type CONTRA = "CONTRA";
export type PAUSE = "PAUSE";

export type ReOrContra = RE | CONTRA | PAUSE;

export const mapToReOrContra = (value: number) : ReOrContra => {
    switch (value) {
        case -1: {
            return "RE";
        }
        case 1: {
            return "CONTRA";
        }
        default: {
            return "PAUSE";
        }
    }
};