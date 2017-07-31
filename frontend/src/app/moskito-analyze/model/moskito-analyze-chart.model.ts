/**
 * Contains MoSKito-Analyze properties.
 * @author strel
 */
export class MoskitoAnalyzeChart {

  /**
   * Chart name / caption.
   */
  name: string;

  /**
   * Producer name.
   */
  producer: string;

  /**
   * Statistic name.
   */
  stat: string;

  /**
   * Value name.
   */
  value: string;

  /**
   * Interval name / type.
   */
  interval: string;

  /**
   * Chart type, i.e. description of what actually chart is showing:
   * average values, total values and so on.
   */
  type: string;

}
