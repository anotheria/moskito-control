
import { Component, Input, AfterContentChecked, ViewChild, ElementRef, AfterViewInit } from "@angular/core";
import { Widget } from "./widget.component";
import {ComponentHolder} from "../entities/component-holder";

declare var showThresholds: any;


@Component({
  selector: 'components-widget',
  templateUrl: 'moskito-components-widget.component.html'
})
export class MoskitoComponentsWidget extends Widget implements AfterViewInit {

  @Input()
  componentHolders: ComponentHolder[];


  ngAfterViewInit() {
  }

  thresholds(context: string, componentName: string, m: number, n: number) {
    showThresholds(context, componentName, m, n);
  }
}
