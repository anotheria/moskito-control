import { Injectable } from "@angular/core";
import { Http, Response } from "@angular/http";
import { Observable } from "rxjs/Observable";
import "rxjs/add/operator/map";
import { MoskitoApplication } from "../entities/moskito-application";
import { MoskitoComponent } from "../entities/moskito-component";
import { HistoryItem } from "../entities/history-item";
import { Chart } from "../entities/chart";
import { SystemStatus } from "../entities/system-status";


@Injectable()
export class HttpService {

  private url = "https://moskito-control.thecasuallounge.com/moskito-control/";// "http://localhost:8088/";

  constructor(private http: Http) { }

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

  getMoskitoStatus(): Observable<any> {
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
}
