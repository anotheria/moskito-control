
import { Component, Input } from "@angular/core";
import { Widget } from "./widget.component";
import {ComponentHistory} from "../entities/component-history";



@Component({
  selector: 'history-widget',
  templateUrl: './history-widget.component.html'
})
export class HistoryWidget extends Widget {

  @Input()
  history: ComponentHistory[];

}
