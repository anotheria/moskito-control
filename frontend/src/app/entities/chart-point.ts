
/**
 * Moskito chart point containing:
 * point caption, point values for each line, timestamp
 * and special point values array containing timestamp as first element.
 *
 * @author strel
 */
export class ChartPoint {

  /**
   * Points caption (timestamp in human readable form)
   */
  caption: string;

  /**
   * Point values for each line
   */
  values: number[];

  /**
   * Point timestamp
   */
  timestamp: number;

  /**
   * Point values with leading timestamp element.
   * Example: [1497014230432, 314, 321, 234]
   */
  jsonwithNumericTimestamp: string;
}
