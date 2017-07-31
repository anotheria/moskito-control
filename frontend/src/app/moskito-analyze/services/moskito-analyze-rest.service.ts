import { Injectable } from "@angular/core";
import { Http, Response } from "@angular/http";
import { Observable } from "rxjs/Observable";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import { MoskitoApplicationService } from "../../services/moskito-application.service";
import { MoskitoAnalyzeChartsRequest } from "../model/moskito-analyze-chart-request.model";
import { MoskitoAnalyzeService } from "./moskito-analyze.service";


/**
 * Service responsible for communicating with Moskito-Analyze REST services.
 * @author strel
 */
@Injectable()
export class MoskitoAnalyzeRestService {

  constructor(
    private http: Http,
    private application: MoskitoApplicationService,
    private moskitoAnalyze: MoskitoAnalyzeService
  ) {
  }

  /**
   * @returns General MoSKito-Analyze configuration, particularly analyze application URL and hosts list.
   */
  public getMoskitoAnalyzeConfig(): Observable<any> {
    return this.http.get(this.application.getApplicationContextPath() + 'rest/analyze/configuration').map((resp: Response) => {
      return resp.json();
    });
  }

  /**
   * @returns Chart properties used as request parameters for MoSKito-Analyze chart REST resource.
   */
  public getChartsConfig(): Observable<any> {
    return this.http.get(this.application.getApplicationContextPath() + 'rest/analyze/charts').map((resp: Response) => {
      return resp.json().charts;
    });
  }

  /**
   * Request to Moskito-Analyze REST endpoint for getting chart data for given period.
   * @param requestType Request type, i.e. type of chart values to show (average, total).
   * @param requestData Request parameters.
   * @returns Charts data.
   */
  public getChartsDataForPeriod(requestType: string, requestData: MoskitoAnalyzeChartsRequest): Observable<any> {
    return this.http.post(this.moskitoAnalyze.url + 'charts/' + requestType, requestData).map((resp: Response) => {
      return resp.json().results.charts;
    });
  }
}
