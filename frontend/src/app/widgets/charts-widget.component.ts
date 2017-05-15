
import { Component, Input } from "@angular/core";
import { Widget } from "./widget.component";
import { Chart } from "../entities/chart";



@Component({
  selector: 'charts-widget',
  templateUrl: './charts-widget.component.html'
})
export class ChartsWidget extends Widget {

  @Input()
  charts: Chart[];

}
