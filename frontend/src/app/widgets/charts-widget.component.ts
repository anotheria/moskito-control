
import { Component, Input, OnInit, QueryList, ElementRef, ViewChildren, AfterViewInit } from "@angular/core";
import { Widget } from "./widget.component";
import { Chart } from "../entities/chart";

declare var chartEngineIniter: any;


@Component({
  selector: 'charts-widget',
  templateUrl: './charts-widget.component.html'
})
export class ChartsWidget extends Widget implements AfterViewInit {

  @Input()
  charts: Chart[];

  multipleGraphData: any[];
  multipleGraphNames: any[];


  /**
   * TODO: Should be rewritten
   */
  ngAfterViewInit(): void {
    this.multipleGraphData = [];
    this.multipleGraphNames = [];
    let chartBoxes = document.getElementsByClassName('chart-box');

    for (let chart of this.charts) {
      this.multipleGraphData.push(chart.points);

      let lineNames = [];
      for (let line of chart.lines) {
        lineNames.push(line.name);
      }
      this.multipleGraphNames.push(lineNames);
    }

    let names = this.multipleGraphNames.map(function(graphNames) {
      return graphNames;
    });

    for (let index = 0; index < this.multipleGraphData.length; index++) {
      let containerSelector = chartBoxes.item(index).id;

      let chartParams = {
        container: containerSelector,
        names: names[index],
        data: this.multipleGraphData[index],
        colors: [],
        type: 'LineChart',
        title: names[index],
        dataType: 'datetime',
        previous_chart_params: {},
        options: {
          legendsPerSlice: 5,
          margin: { top: 20, right: 20, bottom: 20, left: 40 }
        }
      };

      // Setting fullscreen buttons and handlers for chart
      let container: Element;
      for (let i = 0; i < chartBoxes.length; i++) {
        if (containerSelector == chartBoxes.item(i).id) {
          container = chartBoxes.item(i);
        }
      }

      chartParams.previous_chart_params = {
        width: container.clientWidth,
        height: container.clientHeight
      };

      // Creating chart
      chartEngineIniter.init(chartParams);
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
}
