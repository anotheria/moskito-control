import { Component, OnInit, ViewChild } from "@angular/core";
import { HttpService } from "../services/http.service";
import { MoskitoApplicationService } from "../services/moskito-application.service";
import { TimerComponent } from "../shared/timer/timer.component";
import { MoskitoApplication } from "../entities/moskito-application";
import { CategoriesService } from "../services/categories.service";
import { WidgetService } from "../services/widget.service";


/**
 * Responsible for general view: component widgets, charts, tv widget, history items
 * and header rendering. Loads moskito applications list and initializes refresh timer component.
 *
 * @author strel
 */
@Component({
  selector: 'content',
  templateUrl: './content.component.html'
})
export class ContentComponent implements OnInit {

  /**
   * Switches configuration mode for Moskito Control, i.e.
   * switches view between settings and widgets pages.
   */
  configToggle: boolean;

  /**
   * List of Moskito applications to be rendered.
   */
  applications: MoskitoApplication[];

  /**
   * Data loading guard. Indicates whether component
   * has retrieved applications data from REST service.
   */
  applicationDataLoaded: boolean;

  /**
   * Reference to timer component.
   */
  @ViewChild('dataRefreshTimer')
  timer: TimerComponent;


  constructor(private widgetService: WidgetService, private httpService: HttpService, private moskitoApplicationService: MoskitoApplicationService, private categoriesService: CategoriesService) {
    this.applicationDataLoaded = false;
  }

  public ngOnInit(): void {

    // Getting list of all applications
    this.httpService.getMoskitoApplications().subscribe((applications) => {
      this.applications = applications;
      this.moskitoApplicationService.currentApplication = applications[0];

      this.applicationDataLoaded = true;
    });

    this.initTimer();
  }

  /**
   * Handler is called by data refresh timer each 60 seconds.
   * It refreshes all Moskito Control data without reload.
   */
  public onDataRefresh() {
    this.moskitoApplicationService.refreshData();
  }

  /**
   * Sets Moskito-Control configuration mode.
   * @param mode configuration mode indicator
   */
  public setConfigurationMode(mode: boolean) {
    this.configToggle = mode;

    // Not
    if (!mode)
      this.initTimer();
    else
      this.timer.pauseTimer();
  }

  public setApplication(app: MoskitoApplication) {
    this.categoriesService.resetFilter();
    this.moskitoApplicationService.switchApplication(app);
  }

  private initTimer() {
    setTimeout(() => {
      this.timer.callback = this.onDataRefresh.bind(this);
      this.timer.startTimer();
    }, 1000);
  }

  keys(): Array<any> {
    return Object.keys(this.applications);
  }
}
