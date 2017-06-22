import { ExecutorStatus } from "./executor-status";


/**
 * This class represents the current status of an updater.
 *
 * @author strel
 */
export class UpdaterStatus {

  /**
   * True if the update is triggered right now. However, this represents the update triggerer not the update process
   * that follows the trigger.
   */
  updateInProgress: boolean;

  /**
   * Status of the updater thread pool.
   */
  updaterStatus: ExecutorStatus;

  /**
   * Status of the connector thread pool.
   */
  connectorStatus: ExecutorStatus;

}
