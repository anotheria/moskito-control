import { MoskitoAnalyzeChartLine } from "./moskito-analyze-chart-line.model";

/**
 * Contains MoSKito-Analyze chart properties.
 * @author strel
 */
export class MoskitoAnalyzeChart {

  /**
   * Chart unique identifier.
   */
  id: string;

  /**
   * Chart name.
   */
  name: string;

  /**
   * Chart caption.
   */
  caption: string;

  /**
   * Interval name / type.
   */
  interval: string;

  lines: MoskitoAnalyzeChartLine[] = [];

  /**
   * Start date.
   */
  startDate: Date;

  /**
   * End date.
   */
  endDate: Date;

  /**
   * Whether chart data is currently loading.
   */
  loading: boolean;

}
