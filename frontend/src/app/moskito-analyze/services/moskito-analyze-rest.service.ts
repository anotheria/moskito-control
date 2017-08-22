import { Injectable } from "@angular/core";
import { Http, Response } from "@angular/http";
import { Observable } from "rxjs/Observable";
import { MoskitoApplicationService } from "app/services/moskito-application.service";
import { MoskitoAnalyzeService } from "app/moskito-analyze/services/moskito-analyze.service";
import { MoskitoAnalyzeChart } from "app/moskito-analyze/model/moskito-analyze-chart.model";
import { MoskitoAnalyzeChartsRequest } from "app/moskito-analyze/model/moskito-analyze-chart-request.model";
import { Producer } from "app/moskito-analyze/model/chart-producer.model";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";


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
   * Builds chart data request to Moskito-Analyze.
   *
   * @param chart Chart configuration / parameters.
   * @returns {MoskitoAnalyzeChartsRequest} JSON request to Moskito-Analyze.
   */
  public buildChartRequest(chart: MoskitoAnalyzeChart): MoskitoAnalyzeChartsRequest {
    let request = new MoskitoAnalyzeChartsRequest();

    request.interval = chart.interval;
    request.hosts = chart.hosts;

    const producer = new Producer();
    producer.producerId = chart.producer;
    producer.stat = chart.stat;
    producer.value = chart.value;

    request.producers = [producer];

    request.startDate = this.moskitoAnalyze.getUTCStartDate();
    request.endDate = this.moskitoAnalyze.getUTCEndDate();

    return request;
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
   * @returns Moskito-Analyze configuration as it's defined in JSON configuration file
   */
  public getPrettyMoskitoAnalyzeConfig(): Observable<string> {
    return this.http.get(this.application.getApplicationContextPath() + 'rest/analyze/configuration/pretty').map((resp: Response) => {
      return resp.json();
    });
  }

  public createMoskitoAnalyzeChart(chart: MoskitoAnalyzeChart): Observable<void> {
    return this.http.post(this.application.getApplicationContextPath() + 'rest/analyze/chart/create', chart).map((resp: Response) => {
      return resp.json();
    });
  }

  public updateMoskitoAnalyzeChart(chart: MoskitoAnalyzeChart): Observable<void> {
    return this.http.post(this.application.getApplicationContextPath() + 'rest/analyze/chart/' + chart.name + '/update', chart).map((resp: Response) => {
      return resp.json();
    });
  }

  public removeMoskitoAnalyzeChart(chart: MoskitoAnalyzeChart): Observable<void> {
    return this.http.get(this.application.getApplicationContextPath() + 'rest/analyze/chart/' + chart.name + '/remove').map((resp: Response) => {
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

  /**
   * @returns List of all available producer names from moskito-analyze
   */
  public getProducerNames(): Observable<string[]> {
    return this.http.get(this.moskitoAnalyze.url + 'producers/list').map((resp: Response) => {
      return resp.json().results.producersIds;
    });
  }
}
