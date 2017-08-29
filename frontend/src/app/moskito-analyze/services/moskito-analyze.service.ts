import { Injectable } from "@angular/core";
import { MoskitoAnalyzeChart } from "../model/moskito-analyze-chart.model";


/**
 * Service containing configuration for MoSKito-Analyze.
 * @author strel
 */
@Injectable()
export class MoskitoAnalyzeService {

  /**
   * MoSKito-Analyze application URL.
   */
  public url: string;

  /**
   * Array of MoSKito-Analyze chart properties
   * used in requests to retrieve charts data.
   */
  public chartsConfig: MoskitoAnalyzeChart[];


  constructor() {
    this.chartsConfig = [];
  }


  /**
   * Formats date in format suitable for MoSKito-Analyze chart data request.
   * @param d Date
   * @returns {string} String in MoSKito-Analyze format
   */
  public formatDate(d: Date): string {
    let year = d.getFullYear();
    let month = (d.getMonth() + 1) < 10 ? '0' + (d.getMonth() + 1) : (d.getMonth() + 1);
    let day = d.getDate() < 10 ? '0' + d.getDate() : d.getDate();

    let hours = d.getHours() < 10 ? '0' + d.getHours() : d.getHours();
    let minutes = d.getMinutes() < 10 ? '0' + d.getMinutes() : d.getMinutes();

    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
  }
}
