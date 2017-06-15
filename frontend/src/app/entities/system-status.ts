import { MoskitoApplicationStatus } from "./moskito-application-status";
import { UpdaterStatus } from "./updater-status";


/**
 * Shows general system configuration and status.
 * It includes:
 *
 * <ul>
 *     <li>Applications:
 *      <ul>
 *         <li>Components settings: name, category, connector type, location</li>
 *         <li>Charts data description: components and accumulators to be visualized, chart limit</li>
 *      </ul>
 *     </li>
 *     <li>Used connectors</li>
 *     <li>Status updater configuration</li>
 *     <li>Charts updater configuration</li>
 * </ul>
 *
 * @author strel
 */
export class SystemStatus {

  /**
   * Moskito application settings including
   * configuration for components.
   */
  applicationStatuses: MoskitoApplicationStatus[];

  /**
   * Status updater configuration.
   */
  statusUpdater: UpdaterStatus;

  /**
   * Charts updater configuration.
   */
  chartsUpdater: UpdaterStatus;


  constructor() {
    this.applicationStatuses = [];
  }
}
