import { Component } from "@angular/core";
import { HttpService } from "../../services/http.service";


@Component({
  selector: 'notifications-config',
  templateUrl: './notifications-config.component.html'
})
export class NotificationsConfigComponent {

  notificationsMuted: boolean;
  notificationsMutingTime: number;
  notificationsRemainingMutingTime: number;


  constructor(private httpService: HttpService) {
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

    this.httpService.muteNotifications();

    // Starting timer
    this.timerTick();
  }

  public unmuteNotifications() {
    this.notificationsMuted = false;
    this.notificationsMutingTime = 60;
    this.notificationsRemainingMutingTime = 0;

    this.httpService.unmuteNotifications();
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
