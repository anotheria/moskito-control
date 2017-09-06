import { Injectable } from "@angular/core";
import { Http, Response } from "@angular/http";
import { Observable } from "rxjs/Observable";
import { MoskitoApplicationService } from "app/services/moskito-application.service";
import { MoskitoAnalyzeService } from "app/moskito-analyze/services/moskito-analyze.service";
import { MoskitoAnalyzeChart } from "app/moskito-analyze/model/moskito-analyze-chart.model";
import { MoskitoAnalyzeChartsRequest } from "app/moskito-analyze/model/moskito-analyze-chart-request.model";
import { MoskitoAnalyzeChartConfigRequest } from "app/moskito-analyze/model/moskito-analyze-chart-config-request.model";
import { ChartProducer } from "app/moskito-analyze/model/chart-producer.model";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import { Producer } from "../model/producer.model";


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

    const producer = new ChartProducer();
    producer.producerId = chart.producer;
    producer.stat = chart.stat;
    producer.value = chart.value;

    request.producers = [producer];

    let utcStartDate = new Date(chart.startDate.getUTCFullYear(), chart.startDate.getUTCMonth(), chart.startDate.getUTCDate(), chart.startDate.getUTCHours(), chart.startDate.getUTCMinutes());
    request.startDate = this.moskitoAnalyze.formatDate(utcStartDate);

    let utcEndDate = new Date(chart.endDate.getUTCFullYear(), chart.endDate.getUTCMonth(), chart.endDate.getUTCDate(), chart.endDate.getUTCHours(), chart.endDate.getUTCMinutes());
    request.endDate = this.moskitoAnalyze.formatDate(utcEndDate);

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

  /**
   * Adds new MoSKito-Analyze chart configuration bean.
   * @param chart Chart to add
   */
  public createMoskitoAnalyzeChart(chart: MoskitoAnalyzeChart): Observable<void> {
    let requestData = new MoskitoAnalyzeChartConfigRequest();
    requestData.fromAnalyzeChart(chart);

    return this.http.post(this.application.getApplicationContextPath() + 'rest/analyze/chart/create', requestData).map((resp: Response) => {
      return resp.json();
    });
  }

  /**
   * Updates existing MoSKito-Analyze configuration bean.
   * @param chart Updated chart.
   */
  public updateMoskitoAnalyzeChart(chart: MoskitoAnalyzeChart): Observable<void> {
    let requestData = new MoskitoAnalyzeChartConfigRequest();
    requestData.fromAnalyzeChart(chart);

    return this.http.post(this.application.getApplicationContextPath() + 'rest/analyze/chart/' + chart.name + '/update', requestData).map((resp: Response) => {
      return resp.json();
    });
  }

  /**
   * Removes MoSKito-Analyze chart configuration bean.
   * @param chart Chart to remove.
   */
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
   * @returns List of all available producers from moskito-analyze
   */
  public getProducers(): Observable<Producer[]> {
    return this.http.get(this.moskitoAnalyze.url + 'producers/all').map((resp: Response) => {
      return resp.json().results.producers;
    });
  }
}
