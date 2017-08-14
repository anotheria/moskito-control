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

  public getDateFromTimestamp( timestamp: number ): string {
    let current = new Date( timestamp );
    return current.getFullYear() + '-' + (current.getMonth() + 1) + '-' + current.getDate() + ' ' + current.getHours() + ':' + current.getMinutes();
  }

  public getUTCStartDate(): string {
    let current = new Date();    
    return current.getUTCFullYear() + '-' + (current.getUTCMonth() + 1) + '-' + current.getUTCDate() + ' 00:00';
  }

  public getUTCEndDate(): string {
    let current = new Date();    
    return current.getUTCFullYear() + '-' + (current.getUTCMonth() + 1) + '-' + current.getUTCDate() + 
              ' ' + current.getUTCHours() + ':' + current.getUTCMinutes();
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
