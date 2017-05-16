
import { Component, Input, AfterContentChecked, ViewChild, ElementRef, AfterViewInit } from "@angular/core";
import { Widget } from "./widget.component";
import {ComponentHolder} from "../entities/component-holder";


@Component({
  selector: 'components-widget',
  templateUrl: 'moskito-components-widget.component.html'
})
export class MoskitoComponentsWidget extends Widget implements AfterViewInit {

  @Input()
  componentHolders: ComponentHolder[];


  ngAfterViewInit() {
  }
}
