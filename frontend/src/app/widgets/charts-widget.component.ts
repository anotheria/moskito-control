import { Component, QueryList, ElementRef, ViewChildren, AfterViewInit, OnInit } from "@angular/core";
import { Widget } from "./widget.component";
import { Chart } from "../entities/chart";
import { HttpService } from "../services/http.service";
import { MoskitoApplicationService } from "../services/moskito-application.service";
import { ChartService } from "../services/chart.service";


declare var chartEngineIniter: any;


@Component({
  selector: 'charts-widget',
  templateUrl: './charts-widget.component.html'
})
export class ChartsWidget extends Widget implements AfterViewInit, OnInit {

  charts: Chart[];
  chartsDataLoaded: boolean;
  chartBoxesInitialized: boolean;
  fullscreenChart: Chart;

  @ViewChildren('chart_box')
  boxes: QueryList<ElementRef>;


  constructor(private httpService: HttpService, private moskitoApplicationService: MoskitoApplicationService, private chartService: ChartService) {
    super();

    this.chartsDataLoaded = false;
    this.chartBoxesInitialized = false;
  }

  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.moskitoApplicationService.applicationChangedEvent.subscribe(() => this.createBoxes());

    // Loading charts
    this.httpService.getApplicationCharts(this.moskitoApplicationService.currentApplication.name).subscribe((charts) => {
      this.charts = charts;
      this.chartsDataLoaded = true;
    });
  }

  ngAfterViewInit(): void {
    this.boxes.changes.subscribe(( boxes ) => {
      let boxesAsArray = boxes.toArray();
      if (this.chartsDataLoaded) {
        this.initializeCharts(this.charts, boxesAsArray);
      }
    });
  }

  public initializeCharts(charts: Chart[], chartBoxes: ElementRef[]) {
    for (let i = 0; i < charts.length; i++) {
      this.chartService.initializeChart(charts[i], chartBoxes[i]);
    }
  }

  public refreshCharts(charts: Chart[], chartBoxes: ElementRef[]) {
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
    this.httpService.getApplicationCharts(this.moskitoApplicationService.currentApplication.name).subscribe((charts) => {
      this.charts = charts;
    });
  }

  /**
  *
  */
  public refresh() {
    this.httpService.getApplicationCharts(this.moskitoApplicationService.currentApplication.name).subscribe((charts) => {
      this.refreshCharts(charts, this.boxes.toArray());
    });
  }
}
