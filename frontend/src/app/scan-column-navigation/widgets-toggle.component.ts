
import {Component, Input} from "@angular/core";
import {Widget} from "../widgets/widget.component";


@Component({
  selector: 'widgets-toggle',
  templateUrl: 'widgets-toggle.component.html'
})
export class WidgetsToggleComponent {

  @Input()
  widgets: Widget[];

  toggleWidget( widget: Widget ) {
    widget.enabled = !widget.enabled;
  }
}
