/**
 * Represents a single configured component's connector.
 *
 * @author strel
 */
export interface Connector {

  /**
   * Connector information as map to
   * be shown in component inspection.
   */
  info: InformationMap;

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

interface InformationMap {
  [key: string]: string;
}
