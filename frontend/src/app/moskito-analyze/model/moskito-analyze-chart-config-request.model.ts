import { MoskitoAnalyzeChart } from "./moskito-analyze-chart.model";

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
   * Chart type, i.e. description of what actually chart is showing:
   * average values, total values and so on.
   */
  type: string;

  /**
   * List of components.
   * @type {Array}
   */
  components: string[] = [];

  /**
   * Producer name.
   */
  producer: string;

  /**
   * Stat name.
   */
  stat: string;

  /**
   * Value name.
   */
  value: string;

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
      this.type = chart.type;
      this.components = chart.components;
      this.producer = chart.producer;
      this.stat = chart.stat;
      this.value = chart.value;
      this.startDate = chart.startDate.getTime();
      this.endDate = chart.endDate.getTime();
    }
  }
}
