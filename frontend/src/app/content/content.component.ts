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
   * Switches settings mode for Moskito Control, i.e.
   * switches view between settings and widgets pages.
   */
  settingsToggle: boolean;

  configurationToggle: boolean;

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
   * Special flag, indicating whether MoSKito-Analyze
   * tab was pressed.
   *
   * TODO: it's bad solution, should be rewritten in future
   */
  moskitoAnalyzeMode: boolean;

  /**
   * Reference to timer component.
   */
  @ViewChild('dataRefreshTimer')
  timer: TimerComponent;


  constructor(
    public widgetService: WidgetService,
    public moskitoApplicationService: MoskitoApplicationService,
    private httpService: HttpService,
    private categoriesService: CategoriesService
  ) {
    this.applicationDataLoaded = false;
    this.moskitoAnalyzeMode = true;    
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
   * Sets Moskito-Control settings mode.
   * @param mode settings mode indicator
   */
  public setSettingsMode(mode: boolean) {
    this.settingsToggle = mode;

    // Not
    if (!mode)
      this.initTimer();
    else
      this.timer.pauseTimer();
  }

  public setConfigurationMode(mode: boolean) {
    this.configurationToggle = mode;
  }

  public setApplication(app: MoskitoApplication) {
    this.categoriesService.resetFilter();
    this.moskitoApplicationService.switchApplication(app);

    this.moskitoAnalyzeMode = false;
    this.moskitoApplicationService.moskitoAnalyzeMode = false;
  }

  public toggleMoskitoAnalyze() {
    this.moskitoAnalyzeMode = true;
    this.moskitoApplicationService.moskitoAnalyzeMode = true;
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
