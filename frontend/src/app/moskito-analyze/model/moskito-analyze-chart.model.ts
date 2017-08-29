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
   * List of hosts used for chart data request.
   * @type {Array}
   */
  hosts: string[] = [];

  startDate: Date;

  endDate: Date;

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
