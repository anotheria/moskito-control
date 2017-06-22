import { MoskitoComponent } from "./moskito-component";


/**
 * Represents a single category in the view menu.
 *
 * @author strel
 */
export class MoskitoCategory {

  /**
   * Name of the category
   */
  name: string;

  /**
   * Worst health status (between components)
   */
  status: string;

  /**
   * If true this category is actually selected and
   * applied components filter by this category
   */
  active: boolean;

  /**
   * Components which belong to this category
   */
  components: MoskitoComponent[];


  constructor();
  constructor(name: string);
  constructor(name: string, active: boolean);
  constructor(name?: string, active?: boolean, status?:string) {
    this.name = name;
    this.active = active;
    this.status = status;
  }


}
