import { Injectable, signal } from '@angular/core';

export type NotificationType = 'success' | 'error' | 'warning' | 'info';

export interface Notification {
  id: number;
  message: string;
  type: NotificationType;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private nextId = 0;
  notifications = signal<Notification[]>([]);

  show(message: string, type: NotificationType = 'success', duration = 3000) {
    const id = this.nextId++;
    const notification: Notification = { id, message, type };
    
    this.notifications.update(prev => [...prev, notification]);

    setTimeout(() => {
      this.remove(id);
    }, duration);
  }

  remove(id: number) {
    this.notifications.update(prev => prev.filter(n => n.id !== id));
  }

  success(message: string) {
    this.show(message, 'success');
  }

  error(message: string) {
    this.show(message, 'error');
  }

  warning(message: string) {
    this.show(message, 'warning');
  }

  info(message: string) {
    this.show(message, 'info');
  }
}
