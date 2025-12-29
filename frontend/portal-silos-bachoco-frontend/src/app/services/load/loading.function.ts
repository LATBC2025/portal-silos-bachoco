import { BehaviorSubject } from 'rxjs';

export const loadingState = new BehaviorSubject<boolean>(false);

export function showLoader(): void {
  loadingState.next(true);
}

export function hideLoader(): void {
  loadingState.next(false);
}