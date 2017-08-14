import { MoskitoAnalyzeProducer } from "./moskito-analyze-producer.model";


/**
 * Contains MoSKito-Analyze chart properties.
 * @author strel
 */
export class MoskitoAnalyzeChart {

  /**
   * Chart unique identifier.
   */
  id: string;

  /**
   * Chart name / caption.
   */
  name: string;

  /**
   * Interval name / type.
   */
  interval: string;

  /**
   * Chart type, i.e. description of what actually chart is showing:
   * average values, total values and so on.
   */
  type: string;

  hosts: string[];

  producers: MoskitoAnalyzeProducer[];


  /**
   * TODO: Another portion of bad code, change method for field, maybe.
   */
  hasBaseline(): boolean {
    return this.type && this.type.indexOf('baseline') !== -1;
  }
}
