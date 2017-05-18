import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {DataService} from "../services/data.service";
import {WidgetConfigService} from "../services/widget-config.service";
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

  statusToggle: boolean;
  componentHolders: ComponentHolder[];

  chartsToggle: boolean;
  chartBeans: Chart[];

  historyToggle: boolean;
  historyItems: HistoryItem[];

  applications: Application[];

  @ViewChild('dataRefreshTimer')
  timer: TimerComponent;

  temp: string;


  constructor(private dataService: DataService, private widgetConfigService: WidgetConfigService) {
  }


  public ngOnInit(): void {
    let jsonData = this.dataService.content_data;

    this.configToggle = jsonData.configToggle;
    this.lastRefreshTimestamp = jsonData.lastRefreshTimestamp;

    this.configuration = jsonData.configuration;

    this.notificationsMuted = jsonData.notificationsMuted;
    this.notificationsMutingTime = jsonData.notificationsMutingTime;
    this.notificationsRemainingMutingTime = jsonData.notificationsRemainingMutingTime;

    this.statusToggle = this.widgetConfigService.isWidgetEnabled('status');
    this.componentHolders = jsonData.componentHolders;

    this.tvToggle = this.widgetConfigService.isWidgetEnabled('tv');
    this.tvStatus = jsonData.tvStatus;

    this.chartsToggle = this.widgetConfigService.isWidgetEnabled('charts');
    this.chartBeans = jsonData.chartBeans;

    this.historyToggle = this.widgetConfigService.isWidgetEnabled('history');
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
