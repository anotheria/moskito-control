import { Producer } from "./chart-producer.model";

/**
 * Represents request for MoSKito-Analyze charts.
 * @author strel
 */
export class MoskitoAnalyzeChartsRequest {

  /**
   * Interval used for chart values.
   */
  interval: string;

  /**
   * Hosts.
   */
  hosts: string[];

  /**
   * List of producers, each producer represents
   * one specific line and chart.
   */
  producers: Producer[];

  /**
   * Start date in format "YYYY-MM-DD HH:mm".
   */
  startDate: string;

  /**
   * End date in format "YYYY-MM-DD HH:mm".
   */
  endDate: string;

}
