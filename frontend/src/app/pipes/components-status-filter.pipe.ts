import { PipeTransform, Pipe } from "@angular/core";
import { MoskitoComponent } from "../entities/moskito-component";


/**
 * Components pipe used to filter components by
 * specified health status. Filter is triggered by scan column
 * item toggles in Statistics section.
 *
 * @author strel
 */
@Pipe({ name: 'componentsByStatusFilter' })
export class ComponentsStatusFilterPipe implements PipeTransform {

  /**
   * Filters Moskito components by specified health status.
   * If status name is empty, filter is bypassed.
   *
   * @param components List of Moskito components to filter
   * @param statuses Health status used as filter
   * @returns List of filtered Moskito components
   */
  transform(components: MoskitoComponent[], statuses?: string[]): MoskitoComponent[] {
    if (!components) {
      return [];
    }

    if (!statuses || statuses.length === 0) {
      return components;
    }

    const filteredComponents = [];

    for (const component of components) {
      for (const selectedStatus of statuses) {
        if (component.color === selectedStatus) {
          filteredComponents.push(component);
          break;
        }
      }
    }

    return filteredComponents;
  }
}
