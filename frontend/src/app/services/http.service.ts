import { Injectable } from "@angular/core";
import { Http, Response, Headers } from "@angular/http";
import { Observable } from "rxjs/Observable";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import { MoskitoApplication } from "../entities/moskito-application";
import { MoskitoComponent } from "../entities/moskito-component";
import { HistoryItem } from "../entities/history-item";
import { Chart } from "../entities/chart";
import { SystemStatus } from "../entities/system-status";
import { MoskitoApplicationService } from "./moskito-application.service";
import { Threshold } from "../entities/threshold";
import { Connector } from "../entities/connector";


class AccumulatorChartParameters {
  application: string;
  component: string;
  accumulators: string[];

  constructor(application: string, component: string, accumulators: string[]) {
    this.application = application;
    this.component = component;
    this.accumulators = accumulators;
  }
}

/**
 * Service responsible for communicating with Moskito-control REST services.
 * @author strel
 */
@Injectable()
export class HttpService {

  private url;
  private x_www_form_urlendoed_header = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' });
  private json_header = new Headers({ 'Content-Type': 'application/json' });


  constructor(private http: Http, private moskitoApplicationService: MoskitoApplicationService) {

    // Subtracting path to root of application
    let href = window.location.href;
    let javaAppPathIndex = href.indexOf('beta');
    this.url = href.substring(0, javaAppPathIndex == -1 ? href.length : javaAppPathIndex);
    this.url = this.url.endsWith('/') ? this.url : this.url + '/';

    this.moskitoApplicationService.setApplicationContextPath(window.location.pathname.replace('beta', ''));
    // this.changeServer("http://burgershop-control.demo.moskito.org/moskito-control/");
    // this.changeServer("http://localhost:8088/");
  }


  changeServer( url: string ) {
    this.url = url;
    this.moskitoApplicationService.setApplicationContextPath(window.location.pathname.replace('beta', ''));
    this.moskitoApplicationService.refreshData();
  }

  public getUrl(): string {
    return this.url;
  }

  getMoskitoApplications(): Observable<MoskitoApplication[]> {
    return this.http.get(this.url + 'rest/control').map((resp: Response) => {
      return resp.json().applications;
    });
  }

  getApplicationComponents(appName: string): Observable<MoskitoComponent[]> {
    return this.http.get(this.url + 'rest/control').map((resp: Response) => {
      let applications: MoskitoApplication[] = resp.json().applications;

      for (let app of applications) {
        if (app.name == appName) {
          return app.components;
        }
      }

      return [];
    });
  }

  getMoskitoStatus(): Observable<SystemStatus> {
    return this.http.get(this.url + 'rest/status').map((resp: Response) => {
      let response = resp.json();
      let moskitoStatus = new SystemStatus();

      // Getting application statuses
      for (let appName in response.statuses) {
        moskitoStatus.applicationStatuses.push( response.statuses[appName] );
      }

      // Getting updater statuses
      moskitoStatus.chartsUpdater = response.updaterStatuses.charts;
      moskitoStatus.statusUpdater = response.updaterStatuses.status;

      return moskitoStatus;
    });
  }

  getMoskitoConfiguration(): Observable<any> {
    return this.http.get(this.url + 'rest/configuration').map((resp: Response) => {
      return resp.json();
    });
  }

  getApplicationHistory(application: string): Observable<HistoryItem[]> {
    return this.http.get(this.url + 'rest/history/' + application).map((resp: Response) => {
      return resp.json().historyItems;
    });
  }

  getApplicationCharts(application: string): Observable<Chart[]> {
    return this.http.get(this.url + 'rest/charts/points/' + application).map((resp: Response) => {
      return resp.json().charts;
    });
  }

  getThresholds(application: string, component: string): Observable<Threshold[]> {
    return this.http.get(this.url + 'rest/thresholds/' + application + '/' + component).map((resp: Response) => {
      return resp.json().thresholds;
    });
  }

  getAccumulatorNames(application: string, component: string): Observable<string[]> {
    return this.http.get(this.url + 'rest/accumulators/' + application + '/' + component).map((resp: Response) => {
      return resp.json().names;
    });
  }

  getAccumulatorCharts(application: string, component: string, accumulators: string[]): Observable<Chart[]> {
    let params = new AccumulatorChartParameters(application, component, accumulators);
    return this.http.post(this.url + 'rest/accumulators/charts', params, { headers: this.json_header }).map((resp: Response) => {
      return resp.json().charts;
    });
  }

  getConnectorConfiguration(application: string, component: string): Observable<Connector> {
    return this.http.get(this.url + 'rest/connectors/configuration/' + application + '/' + component).map((resp: Response) => {
      return resp.json().connectorConfiguration;
    });
  }

  getConnectorInformation(application: string, component: string): Observable<any> {
    return this.http.get(this.url + 'rest/connectors/information/' + application + '/' + component).map((resp: Response) => {
      return resp.json().connectorInformation;
    });
  }

  muteNotifications() {
    this.http.get(this.url + 'rest/notifications/mute').subscribe();
  }

  unmuteNotifications() {
    this.http.get(this.url + 'rest/notifications/unmute').subscribe();
  }
}
