
/**
 * Application component.
 *
 * @author strel
 */
export class MoskitoComponent {

  /**
   * Component name
   */
  name: string;

  /**
   * Component category
   */
  category: string;

  /**
   * Component health status
   */
  color: string;

  /**
   * Messages associated with given component
   */
  messages: string[];

  /**
   * Component last update timestamp
   */
  lastUpdateTimestamp: string;

  /**
   * Last update timestamp in iso-8661 format (human readable timestamp)
   */
  ISO8601Timestamp: string;
}
