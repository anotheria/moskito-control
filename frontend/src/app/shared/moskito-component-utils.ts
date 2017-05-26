import {MoskitoComponent} from "../entities/moskito-component";


enum Status {
  NONE, PURPLE, RED, ORANGE, YELLOW, GREEN
}

export class MoskitoComponentUtils {

  static getWorthComponentStatus(components: MoskitoComponent[]): string {
    let worthStatus = Status.GREEN;

    for (let component of components) {
      let componentStatus = Status[component.color];
      if (componentStatus < worthStatus) {
        worthStatus = componentStatus;
      }
    }

    return Status[worthStatus];
  }

  static getWorthStatus(statuses: string[]): string {
    let worthStatus = Status.GREEN;

    for (let status of statuses) {
      if (Status[status] < worthStatus) {
        worthStatus = Status[status];
      }
    }

    return Status[worthStatus];
  }

  static orderComponentsByCategories(components: MoskitoComponent[]): any {
    let categories = {};

    for (let component of components) {
      if (!categories[component.category]) {
        categories[component.category] = [];
      }

      categories[component.category].push(component);
    }

    return categories;
  }
}
