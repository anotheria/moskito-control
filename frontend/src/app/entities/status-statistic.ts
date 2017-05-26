enum Status {
  NONE, PURPLE, RED, ORANGE, YELLOW, GREEN
}

export class StatusStatistics {

  status: string;
  numberOfComponents: number;

  constructor(status: string, componentsCount: number) {
    this.status = status;
    this.numberOfComponents = componentsCount;
  }

  static getDefaultStatistics() {
    return {
      PURPLE: 0,
      RED: 0,
      ORANGE: 0,
      YELLOW: 0,
      GREEN: 0
    }
  }
}
