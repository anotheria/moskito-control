/**
 * Describes specific producer, value, stat used in request
 * to MoSKito-Analyze.
 *
 * @author strel
 */
export class Producer {

  /**
   * Producer name (identifier).
   */
  producerId: string;

  /**
   * Statistic name.
   */
  stat: string;

  /**
   * Value name.
   */
  value: string;

}
