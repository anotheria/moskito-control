import {Injectable} from "@angular/core";


@Injectable()
export class ApplicationColorService {

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

  resolveColor(name: string): string {
    for (let color of this.colors) {
      if (color == name.toLowerCase())
        return color;
    }

    return this.NONE;
  }
}
