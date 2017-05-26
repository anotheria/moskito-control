import {Injectable, ElementRef} from "@angular/core";
import {Chart} from "../entities/chart";


declare var chartEngineIniter: any;


@Injectable()
export class ChartService {

  private initializedChartNames: string[];


  constructor() {
    this.initializedChartNames = [];
  }

  public renderChart(chart: Chart, container: ElementRef) {
    let chartData = [];
    for (let point of chart.points) {
      let jsonWithTimestamp = [point.timestamp];
      for (let value of point.values) {
        jsonWithTimestamp.push(JSON.parse(value));
      }

      chartData.push(JSON.parse(point.jsonwithNumericTimestamp));
    }

    let names = chart.lineNames.map(function(graphNames) {
      return graphNames;
    });

    if (this.initializedChartNames.includes(chart.name)) {
      console.log("Refresh " + chart.name);
      let containerSelector = '#' + container.nativeElement.id;

      chartEngineIniter.d3charts.dispatch.refreshLineCharts({
        "containerId": containerSelector,
        "data": chartData,
        "names": names
      });
    }
    else {
      console.log("Init " + chart.name);
      let containerSelector = container.nativeElement.id;

      let chartParams = {
        container: containerSelector,
        names: names,
        data: chartData,
        colors: [],
        type: 'LineChart',
        title: names,
        dataType: 'datetime',
        previous_chart_params: {},
        options: {
          legendsPerSlice: 5,
          margin: { top: 20, right: 20, bottom: 20, left: 40 }
        }
      };

      chartParams.previous_chart_params = {
        width: container.nativeElement.clientWidth,
        height: container.nativeElement.clientHeight
      };

      // Creating chart
      chartEngineIniter.init(chartParams);

      this.initializedChartNames.push(chart.name);
    }
  }

  private buildChartBoxID(chartName: string): string {
    // If chart name is empty generate id
    if (!chartName) {
      return "chart-" + (Math.floor(Math.random() * 9999) + 1000);
    }

    return chartName.replace(/ /gi, "_");
  }
}
