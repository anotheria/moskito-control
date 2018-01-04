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

  /**
   * Chart type, i.e. description of what actually chart is showing:
   * average values, total values and so on.
   */
  type: string;

  /**
   * List of components used for chart data request.
   * @type {Array}
   */
  components: string[] = [];

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

  /**
   * Producer name / id.
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

}
