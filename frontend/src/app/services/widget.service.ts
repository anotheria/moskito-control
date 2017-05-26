import {Injectable} from "@angular/core";
import {TvWidget} from "../widgets/tv-widget.component";
import {MoskitoComponentsWidget} from "../widgets/moskito-components-widget.component";
import {ChartsWidget} from "../widgets/charts-widget.component";
import {HistoryWidget} from "../widgets/history-widget.component";


@Injectable()
export class WidgetService {

  private widgets = {
    "status": {
      name: "status",
      component: "MoskitoComponentsWidget",
      displayName: "Status",
      className: "statuses",
      icon: "fa fa-adjust",
      enabled: true
    },
    "tv": {
      name: "tv",
      component: "TvWidget",
      displayName: "TV",
      className: "tv",
      icon: "fa fa-smile-o",
      enabled: false
    },
    "charts": {
      name: "charts",
      component: "ChartsWidget",
      displayName: "Charts",
      className: "charts",
      icon: "fa fa-bar-chart-o",
      enabled: true
    },
    "history": {
      name: "history",
      component: "HistoryWidget",
      displayName: "History",
      className: "history",
      icon: "fa fa-bars",
      enabled: true
    }
  };


  constructor() { }

  public isWidgetEnabled(widget: string): boolean {
    return this.widgets[widget] && this.widgets[widget].enabled;
  }

  public setWidgetEnabled(widget: string, enabled: boolean) {
    if (this.widgets[widget])
      this.widgets[widget].enabled = enabled;
  }

  public toggleWidgetEnabled(widget: string) {
    if (this.widgets[widget])
      this.widgets[widget].enabled = !this.widgets[widget].enabled;
  }

  public getWidgets() {
    return this.widgets;
  }
}
