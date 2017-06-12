import { ChartPoint } from "./chart-point";

/**
 * Describes moskito control chart data
 * as application receives after 'rest/charts/points/{app}' ajax call.
 *
 * @author strel
 */
export class Chart {

  /**
   * Chart name
   */
  name: string;

  /**
   * Points for this charts
   */
  points: ChartPoint[];

  /**
   * Line names
   */
  lineNames: string[];

  /**
   * Legend that explains a bit about this chart
   */
  legend: string;
}
