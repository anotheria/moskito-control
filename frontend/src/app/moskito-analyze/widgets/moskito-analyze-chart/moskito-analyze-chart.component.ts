import {
  Component,
  OnInit,
  AfterViewInit,
  ViewChildren,
  QueryList,
  ElementRef,
  ChangeDetectorRef
} from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { UUID } from "angular2-uuid";
import { IMultiSelectOption, IMultiSelectSettings } from "angular-2-dropdown-multiselect";
import { Widget } from "app/widgets/widget.component";
import { Chart } from "app/entities/chart";
import { MoskitoAnalyzeChart } from "app/moskito-analyze/model/moskito-analyze-chart.model";
import { MoskitoAnalyzeService } from "app/moskito-analyze/services/moskito-analyze.service";
import { MoskitoApplicationService } from "app/services/moskito-application.service";
import { MoskitoAnalyzeRestService } from "app/moskito-analyze/services/moskito-analyze-rest.service";
import { ChartService } from "app/services/chart.service";
import { MoskitoAnalyzeChartConfigurationModalComponent } from "app/moskito-analyze/widgets/moskito-analyze-chart/configuration-modal/ma-chart-configuration-modal.component";
import { ChartPoint } from "app/entities/chart-point";


/**
 * Main function of JavaScript D3 library used for rendering charts.
 */
declare var chartEngineIniter: any;


/**
 * TODO: Update chart modal doesn't redraws chart.
 */
@Component({
  selector: 'app-moskito-analyze-chart',
  templateUrl: './moskito-analyze-chart.component.html',
  styleUrls: ['./moskito-analyze-chart.component.css']
})
export class MoskitoAnalyzeChartComponent extends Widget implements OnInit, AfterViewInit {

  /**
   * List of charts to render.
   */
  charts: Chart[];

  /**
   * List of moskito-analyze chart configurations
   * from 'moskito-analyze.json'
   */
  chartsConfig: MoskitoAnalyzeChart[];

  /**
   * Indicates whether charts data was loaded.
   */
  chartsDataLoaded: boolean;

  /**
   * Indicates whether chart boxes were initialized.
   */
  chartBoxesInitialized: boolean;

  /**
   * Reference to the fullscreen chart.
   */
  fullscreenChart: Chart;

  /**
   * Is data currently loading.
   */
  isLoading: boolean;

  /**
   * Reference to chart boxes.
   */
  @ViewChildren('chart_box')
  boxes: QueryList<ElementRef>;

  availableHosts: IMultiSelectOption[];

  hostsSettings: IMultiSelectSettings;


  constructor(
    public moskitoAnalyze: MoskitoAnalyzeService,
    private application: MoskitoApplicationService,
    private moskitoAnalyzeRestService: MoskitoAnalyzeRestService,
    private chartService: ChartService,
    private cdr: ChangeDetectorRef,
    private modal: NgbModal
  ) {
    super();

    this.chartsDataLoaded = false;
    this.chartBoxesInitialized = false;
    this.isLoading = false;
  }

  ngOnInit() {
    this.application.dataRefreshEvent.subscribe(() => this.refresh());
    this.application.applicationChangedEvent.subscribe(() => this.createBoxes());

    this.availableHosts = [
      { id: 1, name: 'DE1ANI3BURGR201' },
      { id: 2, name: 'DE1ANI3BURGR302' }
    ];

    this.hostsSettings = {
      checkedStyle: 'fontawesome',
      buttonClasses: 'custom-dropdown-block',
      containerClasses: 'dropdown-block',
      dynamicTitleMaxItems: 0
    };

    this.loadChartsConfig();
  }

  ngAfterViewInit(): void {
    this.boxes.changes.subscribe((boxes) => {
      let boxesAsArray = boxes.toArray();
      if (this.chartsDataLoaded) {
        console.log('After View Init');
        this.initializeCharts(this.charts, boxesAsArray);
      }
    });

    this.cdr.detectChanges();
  }

  public initializeCharts(charts: Chart[], chartBoxes: ElementRef[]) {
    if (this.isLoading) {
      return;
    }

    for (let i = 0; i < charts.length; i++) {
      this.chartService.initializeChart(charts[i], chartBoxes[i]);
    }
  }

  public refreshCharts(charts: Chart[], chartBoxes: ElementRef[]) {
    if (this.isLoading) {
      return;
    }

    for (let i = 0; i < charts.length; i++) {
      this.chartService.refreshChart(charts[i], chartBoxes[i]);
    }
  }

  openChartConfigurationModal(action: string)
  openChartConfigurationModal(action: string, chart?: MoskitoAnalyzeChart) {
    const modalRef = this.modal.open(MoskitoAnalyzeChartConfigurationModalComponent, { windowClass: 'in custom-modal' });
    modalRef.componentInstance.chart = chart ? chart : new MoskitoAnalyzeChart();
    modalRef.componentInstance.action = action;

    modalRef.componentInstance.onChartConfigurationUpdate.subscribe((chart) => this.updateChart(chart));
    modalRef.componentInstance.onChartConfigurationCreate.subscribe((chart) => this.createChart(chart));
  }

  /**
   * Creates new chart and reloads config.
   * @param chart {@link MoskitoAnalyzeChart}.
   */
  createChart(chart: MoskitoAnalyzeChart) {
    this.moskitoAnalyzeRestService.createMoskitoAnalyzeChart(chart).subscribe(() => {
      this.chartsConfig.push(chart);
      this.retrieveChartData(chart);
    });
  }

  updateChart(chart: MoskitoAnalyzeChart) {
    this.moskitoAnalyzeRestService.updateMoskitoAnalyzeChart(chart).subscribe(() => {
      let chartIndex = this.chartsConfig.findIndex((c: MoskitoAnalyzeChart) => {
        return c.id === chart.id;
      });

      this.chartsConfig[chartIndex] = chart;
      this.retrieveChartData(chart, () => {
        // TODO: It's not working properly
        this.chartService.refreshChart(this.charts[chartIndex], this.boxes.toArray()[chartIndex]);
      });
    });
  }

  /**
   * Removes chart and reloads config.
   * @param chart {@link MoskitoAnalyzeChart}.
   */
  removeChart(chart: MoskitoAnalyzeChart) {
    this.moskitoAnalyzeRestService.removeMoskitoAnalyzeChart(chart).subscribe(() => {
      let chartIndex = this.chartsConfig.findIndex((c: MoskitoAnalyzeChart) => {
        return c.id === chart.id;
      });

      this.chartsConfig.splice(chartIndex, 1);
    });
  }

  /**
   * On click listener for charts. Toggles 'fullscreen' mode for charts.
   * @param event {@link Event}
   * @param chart Chart reference.
   */
  onChartClick(event, chart) {
    let target = event.currentTarget;

    var body = document.querySelector('body');
    var svg = target.querySelector('svg');

    // Getting first non fullscreen box
    let referenceElement;
    for (let chartBox of this.boxes.toArray()) {
      if (!chartBox.nativeElement.classList.contains('chart_fullscreen')) {
        referenceElement = chartBox.nativeElement.querySelector('svg');
        break;
      }
    }

    body.classList.toggle('fullscreen');
    target.classList.toggle('chart_fullscreen');
    this.fullscreenChart = chart;

    if (!target.classList.contains('chart_fullscreen')) {
      svg.setAttribute("width", referenceElement ? referenceElement.clientWidth : 800);
      svg.setAttribute("height", referenceElement ? referenceElement.clientHeight - 3 : 300);
      this.fullscreenChart = null;
    }

    chartEngineIniter.d3charts.dispatch.refreshLineChart("#" + target.id, true);
  }

  onHostSelect(event: Event, chart: MoskitoAnalyzeChart) {
    chart.hosts = this.resolveHostsByIds(chart.hostIds);
  }

  /**
   * Creates chart boxes and loads data for them.
   */
  public createBoxes() {
    this.retrieveChartsData();
  }

  /**
   * Refreshes Moskito-Analyze charts data.
   */
  public refresh() {
    this.retrieveChartsData(() => {
      this.refreshCharts(this.charts, this.boxes.toArray());
    });
  }

  /**
   * Gets Moskito-Analyze charts configuration (moskito-analyze.json).
   */
  private loadChartsConfig() {
    // Retrieving charts configuration for further request on moskito-analyze
    this.moskitoAnalyzeRestService.getChartsConfig().subscribe((chartsConfig) => {
      let charts = [];

      chartsConfig.forEach((conf) => {
        let chart = new MoskitoAnalyzeChart();

        chart.id = UUID.UUID();
        chart.caption = conf.caption;
        chart.name = conf.name;
        chart.interval = conf.interval;
        chart.type = conf.type;
        chart.hosts = conf.hosts;
        chart.hostIds = this.getHostIdsByName(chart.hosts);
        chart.producer = conf.producer;
        chart.stat = conf.stat;
        chart.value = conf.value;

        charts.push(chart);
      });

      this.moskitoAnalyze.chartsConfig = charts;
      this.chartsConfig = charts;

      this.cdr.detectChanges();

      // After charts parameters retrieved send request to Moskito-Analyze
      // to get chart data itself.
      this.retrieveChartsData();
    });
  }

  private resolveHostsByIds(ids: number[]): string[] {
    let hosts: string[] = [];

    for (let id of ids) {
      hosts.push(this.availableHosts[id - 1].name);
    }

    return hosts;
  }

  private getHostIdsByName(hosts: string[]): number[] {
    let hostIds = [];

    for (let hostName of hosts) {
      for (let hostItem of this.availableHosts) {
        if (hostItem.name === hostName) {
          hostIds.push(hostItem.id);
        }
      }
    }

    return hostIds;
  }

  private retrieveChartData(chartConfig: MoskitoAnalyzeChart, afterLoad = () => {}) {
    this.moskitoAnalyzeRestService.getChartsDataForPeriod(chartConfig.type, this.moskitoAnalyzeRestService.buildChartRequest(chartConfig)).subscribe((data) => {
      let chart = new Chart();
      chart.name = chartConfig.caption;

      // Going through charts data response to get point values
      chart.points = [];
      chart.lineNames = [];

      for (let chartData of data) {
        let chartPoint = new ChartPoint();

        let pointValues = [];
        for (let value of chartData.values) {
          for (let lineName in value) {
            if (value.hasOwnProperty(lineName)) {
              // If no such line in array, add it
              if (chart.lineNames.indexOf(lineName) === -1) {
                // First we should render default line and next baseline
                // Order in array is important
                if (lineName.indexOf('baseline') === -1) {
                  chart.lineNames.unshift(lineName);
                } else {
                  chart.lineNames.push(lineName);
                }
              }

              // Add value in position, where appropriate line name is stored
              pointValues.splice(chart.lineNames.indexOf(lineName), 0, value[lineName]);
            }
          }
        }

        chartPoint.values = pointValues;
        chartPoint.timestamp = chartData.millis;

        chart.points.push(chartPoint);
      }

      // Finding chart config index in array as it is the same as in charts array
      let id = this.chartsConfig.findIndex((c: MoskitoAnalyzeChart) => {
        return c.name === chartConfig.name;
      });

      // Replacing chart
      if (id) this.charts[id] = chart;
      else this.charts.push(chart);

      this.isLoading = false;
      this.chartsDataLoaded = true;

      afterLoad();
    });
  }

  /**
   * Retrieves Moskito-Analyze charts data and calls afterLoad function
   * after successful data load.
   *
   * @param afterLoad Function to call after successful charts data load.
   */
  private retrieveChartsData(afterLoad = () => { }) {
    // Loading charts
    this.charts = [];
    this.chartsDataLoaded = false;
    this.isLoading = true;

    let chartsLoadedCounter = 0;
    for (let i = 0; i < this.chartsConfig.length; i++) {
      let chartConfig = this.chartsConfig[i];
      this.moskitoAnalyzeRestService.getChartsDataForPeriod(chartConfig.type, this.moskitoAnalyzeRestService.buildChartRequest(chartConfig)).subscribe((charts) => {
        let chart = new Chart();
        chart.name = chartConfig.caption;

        // Going through charts data response to get point values and line names
        chart.points = [];
        chart.lineNames = [];
        chart.colors = [];

        charts.forEach((chartData) => {
          let chartPoint = new ChartPoint();

          let pointValues = [];
          for (let value of chartData.values) {
            for (let lineName in value) {
              if (value.hasOwnProperty(lineName)) {
                // If no such line in array, add it
                if (chart.lineNames.indexOf(lineName) === -1) {
                  // First we should render default line and next baseline
                  // Order in array is important
                  if (lineName.indexOf('baseline') === -1) {
                    chart.lineNames.unshift(lineName);
                  } else {
                    chart.lineNames.push(lineName);
                  }
                }

                // Add value in position, where appropriate line name is stored
                pointValues.splice(chart.lineNames.indexOf(lineName), 0, value[lineName]);
              }
            }
          }

          chartPoint.values = pointValues;
          chartPoint.timestamp = chartData.millis;

          chart.points.push(chartPoint);
        });

        this.charts[i] = chart;
        chartsLoadedCounter++;

        this.cdr.detectChanges();

        // If all charts were loaded
        if (chartsLoadedCounter >= this.chartsConfig.length) {
          this.isLoading = false;
          this.chartsDataLoaded = true;

          afterLoad();
        }
      });
    }
  }
}
