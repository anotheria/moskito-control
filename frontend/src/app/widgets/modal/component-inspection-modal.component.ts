import { Component, Input, OnInit, AfterViewInit, ElementRef, ViewChildren, QueryList } from "@angular/core";
import { MoskitoApplication } from "../../entities/moskito-application";
import { MoskitoComponent } from "../../entities/moskito-component";
import { Threshold } from "../../entities/threshold";
import { Chart } from "../../entities/chart";
import { HttpService } from "../../services/http.service";
import { ChartService } from "../../services/chart.service";


@Component({
  selector: 'component-inspection-modal',
  templateUrl: 'component-inspection-modal.component.html'
})
export class ComponentInspectionModalComponent implements OnInit, AfterViewInit {

  @Input()
  application: MoskitoApplication;

  @Input()
  component: MoskitoComponent;

  thresholds: Threshold[];
  accumulatorNames: string[];
  accumulatorCharts: Chart[];
  checkedAccumulators: string[];

  accumulatorChartsDataLoaded: boolean;

  @ViewChildren('chart_box')
  chartBoxes: QueryList<ElementRef>;


  constructor(
    private httpService: HttpService,
    private chartService: ChartService
  ) {
  }

  ngOnInit() {
    // Getting list of thresholds
    this.httpService.getThresholds( this.application.name, this.component.name ).subscribe(( thresholds ) => {
      this.thresholds = thresholds;
    });

    // Getting list of accumulator names
    this.httpService.getAccumulatorNames( this.application.name, this.component.name ).subscribe(( names ) => {
      this.accumulatorNames = names;
    });

    this.accumulatorCharts = [];
    this.checkedAccumulators = [];
  }

  ngAfterViewInit(): void {
    this.chartBoxes.changes.subscribe(( boxes ) => {
      if (this.accumulatorChartsDataLoaded) {
        this.initializeCharts(this.accumulatorCharts, boxes.toArray());
      }
    });
  }

  public toggleAccumulatorChart( event, accumulatorName: string ) {
    // Toggling accumulator charts
    let showChart = event.target.checked;

    if (showChart) {
      // If checkbox is checked and there is no accumulator in list, add it
      if (this.checkedAccumulators.indexOf(accumulatorName, 0) == -1) {
        this.checkedAccumulators.push(accumulatorName);
      }
    }
    else {
      // Removing accumulator name from list if checkbox is unchecked
      let index = this.checkedAccumulators.indexOf(accumulatorName, 0);
      if (index > -1) {
        this.checkedAccumulators.splice(index, 1);
      }
    }

    this.httpService.getAccumulatorCharts( this.application.name, this.component.name, this.checkedAccumulators ).subscribe(( charts ) => {
      this.accumulatorCharts = charts;
      this.accumulatorChartsDataLoaded = true;
    });
  }

  public initializeCharts(charts: Chart[], chartBoxes: ElementRef[]) {
    if (!charts || !chartBoxes) {
      return;
    }

    for (let i = 0; i < charts.length; i++) {
      this.chartService.initializeChart(charts[i], chartBoxes[i]);
    }
  }
}
