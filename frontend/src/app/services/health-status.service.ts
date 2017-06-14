import { Injectable } from "@angular/core";


/**
 * Service is responsible for managing health statuses / colors.
 */
@Injectable()
export class HealthStatusService {

  NONE = "none";
  PURPLE = "purple";
  RED = "red";
  ORANGE = "orange";
  YELLOW = "yellow";
  GREEN = "green";

  colors = [
    this.NONE, this.PURPLE, this.RED,
    this.ORANGE, this.YELLOW, this.GREEN
  ];

  /**
   * Returns health status by specified name.
   */
  resolveColor(name: string): string {
    for (let color of this.colors) {
      if (color == name.toLowerCase())
        return color;
    }

    return this.NONE;
  }
}
