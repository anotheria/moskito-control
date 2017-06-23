/**
 * Represents a single configured component's connector.
 *
 * @author strel
 */
export interface Connector {

  /**
   * Connector information message to show
   * in component inspection modal.
   */
  info: string;

  /**
   * Whether connector may have information message.
   */
  supportsInfo: boolean;

  /**
   * Indicates whether connector supports thresholds.
   */
  supportsThresholds: boolean;

  /**
   * Indicates whether connector supports accumulators.
   */
  supportsAccumulators: boolean;

}
