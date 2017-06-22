import { MoskitoComponent } from "./moskito-component";


/**
 * Represents an application in the view.
 * @author strel
 */
export class MoskitoApplication {

  /**
   * Application name
   */
  name: string;

  /**
   * Application health status
   */
  applicationColor: string;

  /**
   * List of application components
   */
  components: MoskitoComponent[];
}
