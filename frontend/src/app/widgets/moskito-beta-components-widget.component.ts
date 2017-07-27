import { Component, OnInit, AfterViewInit, ElementRef, ViewChildren, QueryList } from "@angular/core";
import { Widget } from "./widget.component";
import { MoskitoComponent } from "../entities/moskito-component";
import { MoskitoComponentUtils } from "../shared/moskito-component-utils";
import { MoskitoApplicationService } from "../services/moskito-application.service";
import { HttpService } from "../services/http.service";
import { CategoriesService } from "../services/categories.service";
import { StatusService } from "../services/status.service";
import { Threshold } from "../entities/threshold";
import { Chart } from "../entities/chart";
import { ChartService } from "../services/chart.service";
import { Connector } from "../entities/connector";
import { MoskitoApplication } from "../entities/moskito-application";

declare var SetupComponentsView: any;


interface ComponentMap {
  [component: string]: any;
}

@Component({
  selector: 'beta-components-widget',
  templateUrl: 'moskito-beta-components-widget.component.html'
})
export class MoskitoBetaComponentsWidget extends Widget implements OnInit, AfterViewInit {

  currentApplication: MoskitoApplication;

  components: MoskitoComponent[];
  categories: any;

  componentUtils: MoskitoComponentUtils;

  connector: Connector;
  thresholds: Threshold[];
  accumulatorNames: string[];
  accumulatorCharts: Chart[];

  private checkedAccumulatorsMap: ComponentMap;
  private accumulatorChartsMap: ComponentMap;
  private accumulatorChartsDataLoaded: boolean;

  @ViewChildren('chart_box')
  chartBoxes: QueryList<ElementRef>;

  @ViewChildren('componentInspectionModal')
  inspectionModals: QueryList<ElementRef>;


  constructor(
    private httpService: HttpService,
    private moskitoApplicationService: MoskitoApplicationService,
    public categoriesService: CategoriesService,
    public statusService: StatusService,
    private chartService: ChartService
  ) {
    super();
    this.componentUtils = MoskitoComponentUtils;
    this.resetAccumulatorsData();
  }

  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.moskitoApplicationService.applicationChangedEvent.subscribe(() => {
      this.refresh();
      this.resetAccumulatorsData();
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
     this.resetComponentInspectionData();

    // Getting component's connector information
    this.httpService.getConnectorConfiguration( this.currentApplication.name, componentName ).subscribe(
      ( connector ) => {
        this.connector = connector;

        // Loading data for the first available tab
        if (connector) {
          if (connector.supportsThresholds) {
            this.loadThresholdsData( componentName );
          }
          else if (connector.supportsAccumulators) {
            this.loadAccumulatorsData( componentName );
          }
          else if (connector.supportsInfo) {
            this.loadConnectorInformation( componentName );
          }
        }
      },
      ( error ) => {
        console.error("Can't obtain connector for component %s: %s", componentName, error);
      }
    );
  }

  public loadThresholdsData( componentName ) {
    if (this.connector.supportsThresholds) {
      this.httpService.getThresholds(this.currentApplication.name, componentName).subscribe((thresholds) => {
        this.thresholds = thresholds;
      });
    }
  }

  public loadAccumulatorsData( componentName ) {
    if (this.connector.supportsAccumulators) {
      this.httpService.getAccumulatorNames(this.currentApplication.name, componentName).subscribe((names) => {
        this.accumulatorNames = names;
      });

      // Getting checked accumulator charts
      this.accumulatorCharts = this.accumulatorChartsMap[componentName];
    }
  }

  public loadConnectorInformation( componentName ) {
    if (this.connector.supportsInfo) {
      this.httpService.getConnectorInformation(this.currentApplication.name, componentName).subscribe((connector) => {
        this.connector.info = connector.info;
      });
    }
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

    // Scroll top
    this.inspectionModals.forEach((modal: ElementRef) => {
      let modalContent = modal.nativeElement.querySelector('.modal-body');
      if (modalContent) modalContent.scrollTop = 0;
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
    this.connector = null;
    this.thresholds = [];
    this.accumulatorNames = [];
    this.accumulatorCharts = [];
  }

  public resetAccumulatorsData() {
    this.checkedAccumulatorsMap = {};
    this.accumulatorChartsMap = {};
    this.accumulatorChartsDataLoaded = false;
  }

  public refresh() {
    this.currentApplication = this.moskitoApplicationService.currentApplication;
    this.components = this.moskitoApplicationService.currentApplication.components;
    this.categories = MoskitoComponentUtils.orderComponentsByCategories(this.components);

    // Initialize drag-n-drop and tooltips for components
    setTimeout(() => {
      SetupComponentsView();
    }, 1000);
  }
}
