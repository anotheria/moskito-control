import { Injectable, ElementRef } from "@angular/core";
import { Chart } from "../entities/chart";


declare var chartEngineIniter: any;


interface ChartContent {
  [name: string]: string;
}

@Injectable()
export class ChartService {

  private initializedChartNames: string[];
  private chartsCache: ChartContent;


  constructor() {
    this.initializedChartNames = [];
    this.chartsCache = {};
  }

  public initializeChart(chart: Chart, container: ElementRef) {
    let chartData = [];
    for (let point of chart.points) {
      let jsonWithTimestamp = [point.timestamp];
      for (let value of point.values) {
        // Checking is it really number
        let linePoint = Number(value);
        if (!isNaN(linePoint)) {
          jsonWithTimestamp.push(linePoint);
        }
      }

      chartData.push(jsonWithTimestamp);
    }

    let names = chart.lineNames.map(function (graphNames) {
      return graphNames;
    });

    let chartParams = {
      container: container.nativeElement.id,
      names: names,
      data: chartData,
      colors: [],
      type: 'LineChart',
      title: names,
      dataType: 'datetime',
      previous_chart_params: {},
      options: {
        legendsPerSlice: 5,
        margin: {top: 20, right: 20, bottom: 20, left: 40}
      }
    };

    chartParams.previous_chart_params = {
      width: container.nativeElement.clientWidth,
      height: container.nativeElement.clientHeight
    };

    // Creating chart
    chartEngineIniter.init(chartParams);
  }

  public refreshChart(chart: Chart, container: ElementRef) {
    let chartData = [];
    for (let point of chart.points) {
      let jsonWithTimestamp = [point.timestamp];
      for (let value of point.values) {
        // Checking is it really number
        let linePoint = Number(value);
        if (!isNaN(linePoint)) {
          jsonWithTimestamp.push(linePoint);
        }
      }

      chartData.push(jsonWithTimestamp);
    }

    let names = chart.lineNames.map(function (graphNames) {
      return graphNames;
    });

    let containerSelector = '#' + container.nativeElement.id;

    chartEngineIniter.d3charts.dispatch.refreshLineCharts({
      "containerId": containerSelector,
      "data": chartData,
      "names": names
    });
  }

  public getChartContent(chartName: string) {
    return this.chartsCache[chartName];
  }

  public buildChartBoxID(chartName: string): string {
    // If chart name is empty generate id
    if (!chartName) {
      return "chart-" + (Math.floor(Math.random() * 9999) + 1000);
    }

    return chartName.replace(/ /gi, "_");
  }
}
