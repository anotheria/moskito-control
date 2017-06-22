import { PipeTransform, Pipe } from "@angular/core";
import { HistoryItem } from "../entities/history-item";


/**
 * History items pipe used to filter history items by
 * specified component category. Filter is triggered by scan column
 * item toggles in Category section.
 *
 * @author strel
 */
@Pipe({ name: 'historyByCategoryFilter' })
export class HistoryCategoryFilterPipe implements PipeTransform {

  /**
   * Filters list of history items by specified component category.
   * If category name is empty, filter is bypassed.
   *
   * @param historyItems List of history items to filter
   * @param category Affected component category name used as filter
   * @returns List of filtered history items
   */
  transform(historyItems: HistoryItem[], category?: string): HistoryItem[] {
    if (!category) {
      return historyItems;
    }

    let filteredHistoryItems = [];

    for (let item of historyItems) {
      if (item.component && item.component.category == category) {
        filteredHistoryItems.push(item);
      }
    }

    return filteredHistoryItems;
  }
}
