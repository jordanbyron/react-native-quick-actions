declare module 'react-native-quick-actions' {

  export interface ShortcutItem {
    type: string;
    title: string;
    subtitle?: string;
    icon: string;
    userInfo: {
      url: string
    }
  }

  export function popInitialAction(): Promise<ShortcutItem>;

  export function setShortcutItems(shortcutItems: Array<ShortcutItem>): void;

  export function clearShortcutItems(): void;

  export function isSupported(callback: (error: Error | any, supported: boolean) => void): void;
}