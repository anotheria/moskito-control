
import {Component, Input, OnInit} from "@angular/core";
import {Widget} from "../widgets/widget.component";
import {WidgetConfigService} from "../services/widget-config.service";


@Component({
  selector: 'widgets-toggle',
  templateUrl: 'widgets-toggle.component.html'
})
export class WidgetsToggleComponent implements OnInit {

  @Input()
  widgets: Widget[];


  constructor(private widgetConfigService: WidgetConfigService) { }

  ngOnInit() {
  }

  toggleWidget(widget: Widget) {
    this.widgetConfigService.toggleWidgetEnabled(widget.name);
  }

  isWidgetEnabled(widget: Widget): boolean {
    return this.widgetConfigService.isWidgetEnabled(widget.name);
  }
}
