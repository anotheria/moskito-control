import {Component} from "@angular/core";


@Component({
  selector: 'notifications-config',
  templateUrl: './notifications-config.component.html'
})
export class NotificationsConfigComponent {

  private notificationsMuted: boolean;
  private notificationsMutingTime: number;
  private notificationsRemainingMutingTime: number;


  constructor() {
    this.notificationsMuted = false;
    this.notificationsMutingTime = 60;
    this.notificationsRemainingMutingTime = 0;
  }


  public muteNotifications() {
    if (this.notificationsMuted) {
      return;
    }

    this.notificationsMuted = true;
    this.notificationsMutingTime = 60;
    this.notificationsRemainingMutingTime = 60;

    // Starting timer
    this.timerTick();
  }

  public unmuteNotifications() {
    this.notificationsMuted = false;
    this.notificationsMutingTime = 60;
    this.notificationsRemainingMutingTime = 0;
  }

  private timerTick() {
    setTimeout(() => {
      if (!this.notificationsMuted) { return; }

      this.notificationsRemainingMutingTime--;
      if (this.notificationsRemainingMutingTime > 0) {
        this.timerTick();
      }
      else {
        this.unmuteNotifications();
      }
    }, 60000);
  }
}
