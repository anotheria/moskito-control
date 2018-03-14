
/**
 * Possible moskito health statuses varying from none and very bad
 * to good or healthy.
 */
enum HealthStatus {
  NONE, PURPLE, RED, ORANGE, YELLOW, GREEN
}

/**
 * Represents health status statistics used in statistics view.
 * Stores number of components for each health status.
 */
export class StatusStatistics {

  /**
   * Health status
   */
  status: string;

  /**
   * Number of components for given status
   */
  numberOfComponents: number;

  /**
   * Whether current status filter is active
   */
  selected: boolean;

  constructor(status: string, componentsCount: number, selected?: boolean) {
    this.status = status;
    this.numberOfComponents = componentsCount;
    this.selected = selected || false;
  }

  /**
   * Returns empty statistics, i.e. all possible statuses with no components in it.
   *
   * @returns {{PURPLE: number, RED: number, ORANGE: number, YELLOW: number, GREEN: number}}
   */
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
