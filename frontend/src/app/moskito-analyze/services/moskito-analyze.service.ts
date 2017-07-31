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
   * Hosts array used in requests.
   */
  public hosts: string[];

  /**
   * Array of MoSKito-Analyze chart properties
   * used in requests to retrieve charts data.
   */
  public chartsConfig: MoskitoAnalyzeChart[];

  /**
   * Start date in format "YYYY-MM-DD HH:mm"
   * @type {string}
   */
  public startDate: string = this.getDate() + ' 00:00';

  /**
   * End date in format "YYYY-MM-DD HH:mm"
   * @type {string}
   */
  public endDate: string = this.getFullDate();

  /**
   * Request type, showing which values charts should show,
   * i.e. total, average values and so on.
   *
   * TODO: Remove in future
   */
  public requestType: string;

  /**
   * Values interval used in chart request.
   * TODO: Remove in future
   */
  public interval: string;


  constructor() {
    this.chartsConfig = [];
  }


  /**
   * TODO: REMOVE
   * @returns {string}
   */
  private getDate(): string {
    let current = new Date();
    return current.getFullYear() + '-' + (current.getMonth() + 1) + '-' + current.getDate();
  }

  /**
   * TODO: REMOVE
   * @returns {string}
   */
  private getFullDate(): string {
    let current = new Date();
    return this.getDate() + ' ' + current.getHours() + ':' + current.getMinutes();
  }
}
