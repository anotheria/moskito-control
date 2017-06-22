/**
 * Contains single threshold of component.
 * @author strel
 */
export interface Threshold {

  /**
   * Threshold name.
   */
  name: string;

  /**
   * String representation of threshold status.
   */
  status: string;

  /**
   * Last threshold value.
   */
  lastValue: string;

  /**
   * String representation of last threshold update timestamp.
   */
  statusChangeTimestamp: string;

}
