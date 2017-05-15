import {Component, OnInit, ViewChild} from "@angular/core";
import {DataService} from "../services/data.service";
import {ComponentHolder} from "../entities/component-holder";
import {Configuration} from "../entities/configuration";
import {Chart} from "../entities/chart";
import {HistoryItem} from "../entities/history-item";
import {Application} from "../entities/moskito-application";
import {Widget} from "../widgets/widget.component";
import {TimerComponent} from "../shared/timer/timer.component";


@Component({
  selector: 'content',
  templateUrl: './content.component.html'
})
export class ContentComponent implements OnInit {

  configToggle: boolean;

  configuration: Configuration;

  lastRefreshTimestamp: string;

  notificationsMuted: boolean;
  notificationsMutingTime: number;
  notificationsRemainingMutingTime: number;

  tvToggle: boolean;
  tvStatus: string;

  widgets: Widget[];

  statusToggle: boolean;
  componentHolders: ComponentHolder[];

  chartsToggle: boolean;
  chartBeans: Chart[];

  historyItems: HistoryItem[];
  applications: Application[];

  @ViewChild('dataRefreshTimer')
  timer: TimerComponent;


  constructor(private dataService: DataService) {
  }


  public ngOnInit(): void {
    let jsonData = this.dataService.content_data;

    this.configToggle = jsonData.configToggle;
    this.lastRefreshTimestamp = jsonData.lastRefreshTimestamp;

    this.configuration = jsonData.configuration;

    this.notificationsMuted = jsonData.notificationsMuted;
    this.notificationsMutingTime = jsonData.notificationsMutingTime;
    this.notificationsRemainingMutingTime = jsonData.notificationsRemainingMutingTime;

    this.widgets = this.dataService.scan_column_data.widgets;

    this.tvToggle = jsonData.tvToggle;
    this.tvStatus = jsonData.tvStatus;

    this.statusToggle = jsonData.statusToggle;
    this.componentHolders = jsonData.componentHolders;

    this.chartsToggle = jsonData.chartsToggle;
    this.chartBeans = jsonData.chartBeans;

    this.historyItems = jsonData.historyItems;
    this.applications = jsonData.applications;

    // Initializing timer
    setTimeout(() => {
      this.timer.startTimer();
    }, 1000)
  }

  /**
   * Handler is called by data refresh timer each 60 seconds.
   * It refreshes all Moskito Control data without reload.
   */
  public onDataRefresh() {
    console.log('[Refresh Handler]: %s. There will be data refresh soon!', new Date());
  }
}
