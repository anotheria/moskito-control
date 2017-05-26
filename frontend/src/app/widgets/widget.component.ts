import {Component} from "@angular/core";


@Component({
  selector: 'widget',
  template: ``
})
export abstract class Widget {

  name: string;
  displayName: string;
  className: string;
  icon: string;
  enabled: boolean;


  constructor() {
    this.name = "";
    this.displayName = "";
    this.className = "";
    this.icon = "";
    this.enabled = false;
  }

  abstract refresh();

}
