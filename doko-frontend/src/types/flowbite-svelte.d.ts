// Type augmentations for flowbite-svelte so Svelte components can emit click events
// This prevents TS errors like: Argument of type '"click"' is not assignable to parameter of type 'never'.
import type { SvelteComponentTyped } from 'svelte';

declare module 'flowbite-svelte' {
  export interface SpeedDialButtonProps {
    name?: string;
    [key: string]: any;
  }

  // Declare SpeedDialButton to emit a native click event and any custom events
  export class SpeedDialButton extends SvelteComponentTyped<
    SpeedDialButtonProps,
    { click: MouseEvent } & Record<string, CustomEvent<any>>,
    { default: {} }
  > {}

  export class SpeedDial extends SvelteComponentTyped<
    Record<string, any>,
    Record<string, CustomEvent<any>>,
    { default: {} }
  > {}

  export class SpeedDialTrigger extends SvelteComponentTyped<any> {}
}
