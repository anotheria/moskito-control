
/**
 * Represents an application status.
 *
 * @author strel
 */
export class MoskitoApplicationStatus {

  /**
   * Application name.
   */
  name: string;

  /**
   * Timestamp of last status updater run.
   */
  lastStatusUpdaterRun: number;

  /**
   * Timestamp of successful last status updater run.
   */
  lastStatusUpdaterSuccess: number;

  /**
   * Timestamp of last chart data updater run.
   */
  lastChartUpdaterRun: number;

  /**
   * Timestamp of last chart data updater success.
   */
  lastChartUpdaterSuccess: number;

  /**
   * Application status.
   */
  color: string;

  /**
   * Number of status updater runs.
   */
  statusUpdaterRunCount: number;

  /**
   * Number of successful status updater runs.
   */
  statusUpdaterSuccessCount: number;

  /**
   * Number of successful chart data updater runs.
   */
  chartUpdaterSuccessCount: number;

  /**
   * Number of chart data updater runs.
   */
  chartUpdaterRunCount: number;
}
