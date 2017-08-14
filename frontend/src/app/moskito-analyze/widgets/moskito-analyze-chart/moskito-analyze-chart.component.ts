import { MoskitoAnalyzeProducer } from "./../../model/moskito-analyze-producer.model";
import { MoskitoAnalyzeChart } from "./../../model/moskito-analyze-chart.model";
import { Component, OnInit, AfterViewInit, ViewChildren, QueryList, ElementRef } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Chart } from "../../../entities/chart";
import { MoskitoApplicationService } from "../../../services/moskito-application.service";
import { MoskitoAnalyzeRestService } from "../../services/moskito-analyze-rest.service";
import { ChartService } from "../../../services/chart.service";
import { ChartPoint } from "../../../entities/chart-point";
import { Widget } from "../../../widgets/widget.component";
import { MoskitoAnalyzeService } from "../../services/moskito-analyze.service";
import { MoskitoAnalyzeChartsRequest } from "../../model/moskito-analyze-chart-request.model";
import { Producer } from "../../model/chart-producer.model";
import { MoskitoAnalyzeProducerConfigurationModalComponent } from "./configuration-modal/ma-producer-configuration-modal.component";
import { UUID } from "angular2-uuid";


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
   * List of moskito-analyze chart configurations
   * from 'moskito-analyze.json'
   */
  chartsConfig: MoskitoAnalyzeChart[];

  /**
   * Indicates whether charts data is loaded.
   */
  chartsDataLoaded: boolean;
  chartBoxesInitialized: boolean;
  fullscreenChart: Chart;

  isLoading: boolean;

  @ViewChildren('chart_box')
  boxes: QueryList<ElementRef>;


  constructor(
    public moskitoAnalyze: MoskitoAnalyzeService,
    private application: MoskitoApplicationService,
    private moskitoAnalyzeRestService: MoskitoAnalyzeRestService,
    private chartService: ChartService,
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

    // Retrieving charts configuration for further request on moskito-analyze
    this.moskitoAnalyzeRestService.getChartsConfig().subscribe((chartsConfig) => {
      let charts = [];

      chartsConfig.forEach((conf) => {
        let chart = new MoskitoAnalyzeChart();
        chart.id = UUID.UUID();
        chart.name = conf.name;
        chart.interval = conf.interval;
        chart.type = conf.type;
        chart.hosts = conf.hosts;

        let producers = [];
        for (let producerConfig of conf.producers) {
          let producer = new MoskitoAnalyzeProducer();
          producer.id = UUID.UUID();
          producer.caption = producerConfig.caption;
          producer.producer = producerConfig.producer;
          producer.stat = producerConfig.stat;
          producer.value = producerConfig.value;
          producers.push(producer);
        }
        chart.producers = producers;

        charts.push(chart);
      });

      this.moskitoAnalyze.chartsConfig = charts;
      this.chartsConfig = charts;

      // After charts parameters retrieved send request to Moskito-Analyze
      // to get chart data itself.
      this.retrieveChartsData();
    });
  }

  ngAfterViewInit(): void {
    this.boxes.changes.subscribe((boxes) => {
      let boxesAsArray = boxes.toArray();
      if (this.chartsDataLoaded) {
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

  openProducerConfigurationModal( action: string )
  openProducerConfigurationModal( action: string, chart?: MoskitoAnalyzeChart, producer?: MoskitoAnalyzeProducer ) {
    const modalRef = this.modal.open(MoskitoAnalyzeProducerConfigurationModalComponent, { windowClass: 'in custom-modal' });
    modalRef.componentInstance.producer = producer ? producer : new MoskitoAnalyzeProducer();
    modalRef.componentInstance.action = action;

    modalRef.componentInstance.onProducerConfigurationUpdate.subscribe((producer) => this.updateProducer(chart, producer));
    modalRef.componentInstance.onProducerConfigurationCreate.subscribe((producer) => this.createProducer(chart, producer));
  }

  updateProducer(chart: MoskitoAnalyzeChart, producer: MoskitoAnalyzeProducer) {
    let producerId = chart.producers.findIndex((p: MoskitoAnalyzeProducer) => p.id === producer.id);
    chart.producers[producerId] = producer;

    let chartId = this.chartsConfig.findIndex((c: MoskitoAnalyzeChart) => c.id === chart.id);
    this.moskitoAnalyze.chartsConfig[chartId] = chart;

    this.refresh();
  }

  createProducer(chart: MoskitoAnalyzeChart, producer: MoskitoAnalyzeProducer) {
    // Todo: Kostil. New producer is always added to first chart
    // let chartId = this.chartsConfig.findIndex((c: MoskitoAnalyzeChart) => c.id === chart.id);
    let chartId = 0;

    producer.id = UUID.UUID();
    this.moskitoAnalyze.chartsConfig[chartId].producers.push(producer);

    this.refresh();
  }

  removeProducer(chart: MoskitoAnalyzeChart, producer: MoskitoAnalyzeProducer) {
    let producerId = chart.producers.findIndex((p: MoskitoAnalyzeProducer) => p.id === producer.id);
    let chartId = this.chartsConfig.findIndex((c: MoskitoAnalyzeChart) => c.id === chart.id);

    this.moskitoAnalyze.chartsConfig[chartId].producers.splice(producerId, 1);

    this.refresh();
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

  private buildChartRequest(chart: MoskitoAnalyzeChart): MoskitoAnalyzeChartsRequest {
    let request = new MoskitoAnalyzeChartsRequest();

    request.interval = chart.interval;
    request.hosts = chart.hosts;

    let producers = [];
    for (let producerConfig of chart.producers) {
      let producer = new Producer();
      producer.producerId = producerConfig.producer;
      producer.stat = producerConfig.stat;
      producer.value = producerConfig.value;
      producers.push(producer);
    }
    request.producers = producers;

    request.startDate = this.moskitoAnalyze.getUTCStartDate();
    request.endDate = this.moskitoAnalyze.getUTCEndDate();

    return request;
  }

  private retrieveChartsData(afterLoad = () => {}) {
    // Loading charts
    this.isLoading = true;
    for (let chartConfig of this.chartsConfig) {
      let parsedCharts: Chart[] = [];
      this.moskitoAnalyzeRestService.getChartsDataForPeriod(chartConfig.type, this.buildChartRequest(chartConfig)).subscribe((charts) => {
        for (let producerConfig of chartConfig.producers) {
          let chart = new Chart();
          chart.name = chartConfig.name + ' ' + producerConfig.caption;

          // Currently MoSKito-Analyze chart has one line, which is called in such way '[producer].[stat].[value]'
          // and may have baseline called 'baseline.[producer].[stat].[value]'
          // TODO: Creating chart line names here?! Really... Rewrite this in future!
          chart.lineNames = [];
          chart.lineNames.push(producerConfig.producer + '.' + producerConfig.stat + '.' + producerConfig.value);

          // TODO: Holy shit!
          if (chartConfig.hasBaseline()) {
            chart.lineNames.push('baseline.' + producerConfig.producer + '.' + producerConfig.stat + '.' + producerConfig.value);
          }

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
        }

        this.isLoading = false;
        this.charts = parsedCharts;
        this.chartsDataLoaded = true;

        afterLoad();
      });
    }
  }
}
