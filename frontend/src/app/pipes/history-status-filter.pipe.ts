import { PipeTransform, Pipe } from "@angular/core";
import { HistoryItem } from "../entities/history-item";


/**
 * History items pipe used to filter history items by
 * specified health status. Filter is triggered by scan column
 * item toggles in Statistics section.
 *
 * @author strel
 */
@Pipe({ name: 'historyByStatusFilter' })
export class HistoryStatusFilterPipe implements PipeTransform {

  /**
   * Filters history items by specified health status.
   * If status name is empty, filter is bypassed.
   *
   * @param historyItems List of history items to filter
   * @param status Health status used as filter
   * @returns List of filtered history items
   */
  transform(historyItems: HistoryItem[], status?: string): HistoryItem[] {
    if (!status) {
      return historyItems;
    }

    let filteredHistoryItems = [];

    for (let item of historyItems) {
      if (item.component && item.component.color == status) {
        filteredHistoryItems.push(item);
      }
    }

    return filteredHistoryItems;
  }
}
