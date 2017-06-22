import { Component, OnInit, AfterViewInit, ViewChildren, ElementRef, QueryList } from "@angular/core";
import { Widget } from "./widget.component";
import { MoskitoComponent } from "../entities/moskito-component";
import { MoskitoComponentUtils } from "../shared/moskito-component-utils";
import { MoskitoApplicationService } from "../services/moskito-application.service";
import { HttpService } from "../services/http.service";
import { StatusService } from "../services/status.service";
import { CategoriesService } from "../services/categories.service";
import { MoskitoApplication } from "../entities/moskito-application";
import { ChartService } from "../services/chart.service";
import { Threshold } from "../entities/threshold";
import { Chart } from "../entities/chart";

declare var SetupComponentsView: any;


class ComponentInspectionModalData {
  show: boolean;
  application: MoskitoApplication;
  component: MoskitoComponent;
}

interface ComponentMap {
  [component: string]: any;
}

@Component({
  selector: 'components-widget',
  templateUrl: 'moskito-components-widget.component.html'
})
export class MoskitoComponentsWidget extends Widget implements OnInit, AfterViewInit {

  components: MoskitoComponent[];
  categories: any;

  componentUtils: MoskitoComponentUtils;

  thresholds: Threshold[];
  accumulatorNames: string[];
  accumulatorCharts: Chart[];

  private checkedAccumulatorsMap: ComponentMap;
  private accumulatorChartsMap: ComponentMap;
  private accumulatorChartsDataLoaded: boolean;

  @ViewChildren('chart_box')
  chartBoxes: QueryList<ElementRef>;


  constructor(
    private httpService: HttpService,
    private moskitoApplicationService: MoskitoApplicationService,
    private categoriesService: CategoriesService,
    private statusService: StatusService,
    private chartService: ChartService
  ) {
    super();
    this.componentUtils = MoskitoComponentUtils;
    this.resetComponentInspectionData();
  }


  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.moskitoApplicationService.applicationChangedEvent.subscribe(() => {
      this.refresh();
      this.resetComponentInspectionData();
    });

    this.refresh();
  }

  ngAfterViewInit(): void {
    this.chartBoxes.changes.subscribe(( boxes ) => {
      if (this.accumulatorChartsDataLoaded) {
        this.initializeCharts(this.accumulatorCharts);
      }
    });
  }

  getComponentInspectionModalData( componentName: string ) {
    let currentApp = this.moskitoApplicationService.currentApplication;
    if (!currentApp) {
      return;
    }

    // Getting list of thresholds
    this.httpService.getThresholds( currentApp.name, componentName ).subscribe(( thresholds ) => {
      this.thresholds = thresholds;
    });

    // Getting list of accumulator names
    this.httpService.getAccumulatorNames( currentApp.name, componentName ).subscribe(( names ) => {
      this.accumulatorNames = names;
    });

    // Getting checked accumulator charts
    this.accumulatorCharts = this.accumulatorChartsMap[componentName];
  }

  public toggleAccumulatorChart( event, componentName: string, accumulatorName: string ) {
    let currentApp = this.moskitoApplicationService.currentApplication;
    if (!currentApp) {
      return;
    }

    // Toggling accumulator charts
    let showChart = event.target.checked;

    // Initializing accumulator names array
    if (!this.checkedAccumulatorsMap[componentName]) {
      this.checkedAccumulatorsMap[componentName] = [];
    }

    if (showChart) {
      // If checkbox is checked and there is no accumulator in list, add it
      if (this.checkedAccumulatorsMap[componentName].indexOf(accumulatorName, 0) == -1) {
        this.checkedAccumulatorsMap[componentName].push(accumulatorName);
      }
    }
    else {
      // Removing accumulator name from list if checkbox is unchecked
      let index = this.checkedAccumulatorsMap[componentName].indexOf(accumulatorName, 0);
      if (index > -1) {
        this.checkedAccumulatorsMap[componentName].splice(index, 1);
      }
    }

    this.httpService.getAccumulatorCharts( currentApp.name, componentName, this.checkedAccumulatorsMap[componentName] ).subscribe(( charts ) => {
      this.accumulatorCharts = charts;
      this.accumulatorChartsMap[componentName] = charts;

      this.accumulatorChartsDataLoaded = true;
    });
  }

  public initializeCharts(charts: Chart[]) {
    if (!charts) {
      return;
    }

    for (let chart of charts) {
      let chartBox = this.chartBoxes.find((element) => {
        return element.nativeElement.id == chart.divId;
      });

      this.chartService.initializeChart(chart, chartBox);
    }
  }

  public resetComponentInspectionData() {
    this.checkedAccumulatorsMap = {};
    this.accumulatorChartsMap = {};
    this.accumulatorChartsDataLoaded = false;
  }

  public refresh() {
    this.components = this.moskitoApplicationService.currentApplication.components;
    this.categories = MoskitoComponentUtils.orderComponentsByCategories(this.components);

    // Initialize drag-n-drop and tooltips for components
    setTimeout(() => {
      SetupComponentsView();
    }, 1000);
  }
}
