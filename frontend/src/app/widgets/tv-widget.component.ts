
import { Component, Input } from "@angular/core";
import { Widget } from "./widget.component";



@Component({
  selector: 'tv-widget',
  templateUrl: 'tv-widget.component.html'
})
export class TvWidget extends Widget {

  @Input()
  status: string;

}
