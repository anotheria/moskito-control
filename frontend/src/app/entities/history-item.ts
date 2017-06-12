import { MoskitoComponent } from "./moskito-component";

/**
 * Contains one element in the history view widget.
 * A history event is a change in the component's health status.
 *
 * @author strel
 */
export class HistoryItem {

  /**
   * Name of the affected component
   */
  componentName: string;

  /**
   * Affected component
   */
  component: MoskitoComponent;

  /**
   * Status resource prior to the change
   */
  oldStatus: string;

  /**
   * Status resource after the change.
   */
  newStatus: string;

  /**
   * Change timestamp
   */
  timestamp: number;

  /**
   * Timestamp of the change as iso-8661 timestamp (human readable)
   */
  isoTimestamp: string;

  /**
   * Messages in old state
   */
  oldMessages: string[];

  /**
   * Messages in new state
   */
  newMessages: string[];

}
