
import { Injectable } from "@angular/core";
import { IWidgetConfig } from "../entities/widget-config.interface";


@Injectable()
export class WidgetConfigService {

  private widgetConfig: IWidgetConfig = {
    "status": true,
    "tv": false,
    "charts": true,
    "history": true
  };

  conf: string;


  public isWidgetEnabled(widget: string): boolean {
    return this.widgetConfig[widget];
  }

  public setWidgetEnabled(widget: string, enabled: boolean) {
    this.widgetConfig[widget] = enabled;
  }

  public toggleWidgetEnabled(widget: string) {
    this.widgetConfig[widget] = this.widgetConfig[widget] ? false : true;
  }
}
