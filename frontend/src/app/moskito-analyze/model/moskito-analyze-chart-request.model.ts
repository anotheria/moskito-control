import {MoskitoAnalyzeChartDataRequest} from "./moskito-analyze-chart-data-request.model";

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
   * Chart lines.
   */
  lines: MoskitoAnalyzeChartDataRequest[];

  /**
   * Start date in format "YYYY-MM-DD HH:mm".
   */
  startDate: string;

  /**
   * End date in format "YYYY-MM-DD HH:mm".
   */
  endDate: string;

}
