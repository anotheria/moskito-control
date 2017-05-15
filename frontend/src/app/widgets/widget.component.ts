
import { Component, Input } from "@angular/core";


@Component({
  selector: 'widget',
  template: ``
})
export class Widget {

  name: string;
  displayName: string;
  className: string;
  icon: string;

  @Input()
  enabled: boolean;

}
