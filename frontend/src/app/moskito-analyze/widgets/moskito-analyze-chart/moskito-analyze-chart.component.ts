import { Component, OnInit, AfterViewInit, ViewChildren, QueryList, ElementRef } from "@angular/core";
import { FormGroup, FormBuilder } from "@angular/forms";
import { Chart } from "../../../entities/chart";
import { MoskitoApplicationService } from "../../../services/moskito-application.service";
import { MoskitoAnalyzeRestService } from "../../services/moskito-analyze-rest.service";
import { ChartService } from "../../../services/chart.service";
import { ChartPoint } from "../../../entities/chart-point";
import { Widget } from "../../../widgets/widget.component";
import { MoskitoAnalyzeService } from "../../services/moskito-analyze.service";
import { MoskitoAnalyzeChart } from "../../model/moskito-analyze-chart.model";
import { MoskitoAnalyzeChartsRequest } from "../../model/moskito-analyze-chart-request.model";
import { Producer } from "../../model/chart-producer.model";


/**
 * Main function of JavaScript D3 library used for rendering charts.
 */
declare var chartEngineIniter: any;


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
   * Indicates whether charts data is loaded.
   */
  chartsDataLoaded: boolean;
  chartBoxesInitialized: boolean;
  fullscreenChart: Chart;

  requestForm: FormGroup;
  isLoading: boolean;

  @ViewChildren('chart_box')
  boxes: QueryList<ElementRef>;


  constructor(
    public moskitoAnalyze: MoskitoAnalyzeService,
    private application: MoskitoApplicationService,
    private moskitoAnalyzeRestService: MoskitoAnalyzeRestService,
    private chartService: ChartService,
    private fb: FormBuilder
  ) {
    super();

    this.chartsDataLoaded = false;
    this.chartBoxesInitialized = false;
    this.isLoading = false;

    this.createRequestForm();
  }

  sendRequest() {
    this.moskitoAnalyze.startDate = this.requestForm.value.startDate;
    this.moskitoAnalyze.endDate = this.requestForm.value.endDate;

    this.application.refreshData();
  }

  private createRequestForm() {
    this.requestForm = this.fb.group({
      interval: '1m',
      startDate: this.moskitoAnalyze.startDate,
      endDate: this.moskitoAnalyze.endDate
    });
  }

  ngOnInit() {
    this.application.dataRefreshEvent.subscribe(() => this.refresh());
    this.application.applicationChangedEvent.subscribe(() => this.createBoxes());

    // Retrieving charts configuration for further request on moskito-analyze
    this.moskitoAnalyzeRestService.getChartsConfig().subscribe((chartsConfig) => {
      let charts = [];

      chartsConfig.forEach((conf) => {
        let chart = new MoskitoAnalyzeChart();
        chart.name = conf.name;
        chart.interval = conf.interval;
        chart.producer = conf.producer;
        chart.stat = conf.stat;
        chart.value = conf.value;
        chart.type = conf.type;
        charts.push(chart);

        // TODO: Remove in future
        this.moskitoAnalyze.requestType = chart.type;
        this.moskitoAnalyze.interval = chart.interval;
      });

      this.moskitoAnalyze.chartsConfig = charts;

      // After charts parameters retrieved send request to Moskito-Analyze
      // to get chart data itself.
      this.retrieveChartsData();
    });


  }

  ngAfterViewInit(): void {
    this.boxes.changes.subscribe((boxes) => {
      let boxesAsArray = boxes.toArray();
      if (this.chartsDataLoaded && !this.isLoading) {
        this.initializeCharts(this.charts, boxesAsArray);
      }
    });
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

  public createBoxes() {
    this.retrieveChartsData();
  }

  /**
   *
   */
  public refresh() {
    this.retrieveChartsData(() => {
      this.refreshCharts(this.charts, this.boxes.toArray());
    });
  }

  private buildChartsRequest(): MoskitoAnalyzeChartsRequest {
    let request = new MoskitoAnalyzeChartsRequest();

    request.interval = '1m';
    request.hosts = this.moskitoAnalyze.hosts;

    let producers = [];
    this.moskitoAnalyze.chartsConfig.forEach((chart) => {
      let producer = new Producer();
      producer.producerId = chart.producer;
      producer.stat = chart.stat;
      producer.value = chart.value;
      producers.push(producer);
    });
    request.producers = producers;

    request.startDate = this.moskitoAnalyze.startDate;
    request.endDate = this.moskitoAnalyze.endDate;

    return request;
  }

  private retrieveChartsData(afterLoad = () => {}) {

    // Loading charts
    // this.isLoading = true;
    this.moskitoAnalyzeRestService.getChartsDataForPeriod(this.moskitoAnalyze.requestType, this.buildChartsRequest()).subscribe((charts) => {
      let parsedCharts: Chart[] = [];
      this.moskitoAnalyze.chartsConfig.forEach((chartConfig: MoskitoAnalyzeChart) => {
        let chart = new Chart();
        chart.name = chartConfig.name;

        // Currently MoSKito-Analyze chart has only one line, which is called in such way 'producer.stat.value'
        chart.lineNames = [chartConfig.producer + '.' + chartConfig.stat + '.' + chartConfig.value];

        // Going through charts data response to get point values
        chart.points = [];
        charts.forEach((chartData) => {
          let chartPoint = new ChartPoint();

          let pointValues = [];
          for (let value of chartData.values) {
            for (let lineName of chart.lineNames) {
              if (value[lineName]) {
                pointValues.push(value[lineName])
              }
            }
          }

          chartPoint.values = pointValues;
          chartPoint.timestamp = chartData.millis;

          chart.points.push(chartPoint);
        });

        parsedCharts.push(chart);
      });

      // this.isLoading = false;
      this.charts = parsedCharts;
      this.chartsDataLoaded = true;

      afterLoad();
    });
  }
}
