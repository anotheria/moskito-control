import {Component, OnInit} from "@angular/core";
import {MoskitoApplicationService} from "../services/moskito-application.service";
import {Widget} from "../widgets/widget.component";
import {WidgetService} from "../services/widget.service";


@Component({
  selector: 'widgets-toggle',
  templateUrl: 'widgets-toggle.component.html'
})
export class WidgetsToggleComponent implements OnInit {

  widgets: Widget[];


  constructor(private widgetService: WidgetService, private moskitoApplicationService: MoskitoApplicationService) { }

  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.refresh();
  }

  public refresh() {
    this.widgets = [];
    let widgetDictionary = this.widgetService.getWidgets();

    for (let widgetName in widgetDictionary) {
      this.widgets.push(widgetDictionary[widgetName]);
    }
  }

  toggleWidget(widget: Widget) {
    this.widgetService.toggleWidgetEnabled(widget.name);
  }

  isWidgetEnabled(widget: Widget): boolean {
    return this.widgetService.isWidgetEnabled(widget.name);
  }
}
