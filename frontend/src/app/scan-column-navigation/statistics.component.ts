import { Component, OnInit } from "@angular/core";
import { MoskitoApplicationService } from "../services/moskito-application.service";
import { StatusStatistics } from "../entities/status-statistic";
import { StatusService } from "../services/status.service";


@Component({
  selector: 'statistics',
  templateUrl: 'statistics.component.html'
})
export class StatisticsComponent implements OnInit {

  statistics: StatusStatistics[];


  constructor(private moskitoApplicationService: MoskitoApplicationService, private statusService: StatusService) { }

  public ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.moskitoApplicationService.applicationChangedEvent.subscribe(() => this.refresh());
    this.refresh();
  }

  public refresh() {
    this.statistics = [];

    let statsDictionary = StatusStatistics.getDefaultStatistics();
    for (let component of this.moskitoApplicationService.currentApplication.components) {
      if (!statsDictionary[component.color]) {
        statsDictionary[component.color] = 0;
      }

      statsDictionary[component.color] += 1;
    }

    // Transfer status dictionary to array of statistics objects
    for (let status in statsDictionary) {
      this.statistics.push(new StatusStatistics(status, statsDictionary[status]));
    }
  }

  public setStatusFilter(status: string) {
    this.statusService.setFilter(status);
  }
}
