import {Component, QueryList, ElementRef, ViewChildren, AfterViewChecked, OnInit} from "@angular/core";
import {Widget} from "./widget.component";
import {Chart} from "../entities/chart";
import {HttpService} from "../services/http.service";
import {MoskitoApplicationService} from "../services/moskito-application.service";
import {ChartService} from "../services/chart.service";


declare var chartEngineIniter: any;


@Component({
  selector: 'charts-widget',
  templateUrl: './charts-widget.component.html'
})
export class ChartsWidget extends Widget implements AfterViewChecked, OnInit {

  charts: Chart[];
  chartsDataLoaded: boolean;
  chartBoxesInitialized: boolean;

  @ViewChildren('chart_box')
  boxes: QueryList<ElementRef>;
  boxesAsArray: ElementRef[];


  constructor(private httpService: HttpService, private moskitoApplicationService: MoskitoApplicationService, private chartService: ChartService) {
    super();

    this.chartsDataLoaded = false;
    this.chartBoxesInitialized = false;
  }

  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());

    // Loading charts
    this.httpService.getApplicationCharts(this.moskitoApplicationService.currentApplication.name).subscribe((charts) => {
      this.charts = charts;
      this.chartsDataLoaded = true;
    });
  }

  ngAfterViewChecked() {
    if (!this.chartBoxesInitialized && this.chartsDataLoaded) {
      // this.buildCharts();
      this.boxesAsArray = this.boxes.toArray();

      this.renderCharts();
      this.chartBoxesInitialized = true;
    }
  }

  public renderCharts() {
    for (let i = 0; i < this.charts.length; i++) {
      this.chartService.renderChart(this.charts[i], this.boxesAsArray[i]);
    }
  }

  onChartClick(event) {
    let target = event.currentTarget;

    var svg = target.querySelector('svg');
    var $parent = target.parentNode;
    $parent.classList.toggle('chart_fullscreen');

    if (!$parent.classList.contains('chart_fullscreen')) {
      svg.setAttribute("width", 800);
      svg.setAttribute("height", 300);
    }

    chartEngineIniter.d3charts.dispatch.refreshLineChart("#" + target.id, true);
  }

  /**
  *
  */
  public refresh() {
    this.httpService.getApplicationCharts(this.moskitoApplicationService.currentApplication.name).subscribe((charts) => {
      this.charts = charts;
      this.chartsDataLoaded = true;

      this.renderCharts();
    });
  }
}
