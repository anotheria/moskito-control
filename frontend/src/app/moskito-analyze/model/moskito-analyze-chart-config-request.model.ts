import { MoskitoAnalyzeChart } from "./moskito-analyze-chart.model";
import { MoskitoAnalyzeChartLine } from "./moskito-analyze-chart-line.model";

/**
 * Contains MoSKito-Analyze chart properties used for
 * requests to MoSKito-Control REST endpoint.
 *
 * @author strel
 */
export class MoskitoAnalyzeChartConfigRequest {

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

  /**
   * Chart lines.
   */
  lines: MoskitoAnalyzeChartLine[];

  /**
   * Start date.
   */
  startDate: number;

  /**
   * End date.
   */
  endDate: number;


  /**
   * Initializes chart properties from {@link MoskitoAnalyzeChart}.
   * @param chart
   */
  fromAnalyzeChart(chart: MoskitoAnalyzeChart) {
    if (chart) {
      this.name = chart.name;
      this.caption = chart.caption;
      this.interval = chart.interval;
      this.lines = chart.lines;
      this.startDate = chart.startDate.getTime();
      this.endDate = chart.endDate.getTime();
    }
  }
}
