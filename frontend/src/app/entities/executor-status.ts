
/**
 * Status of the thread pool executor service.
 *
 * @author strel
 */
export class ExecutorStatus {
  /**
   * Total tasks.
   */
  taskCount: number;

  /**
   * Active tasks count.
   */
  activeCount: number;

  /**
   * Completed task count.
   */
  completedTaskCount: number;

  /**
   * Thread pool size.
   */
  poolSize: number;
}
