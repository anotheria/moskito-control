import { Component } from "@angular/core";
import { Widget } from "./widget.component";
import { MoskitoApplicationService } from "../services/moskito-application.service";


@Component({
  selector: 'tv-widget',
  templateUrl: 'tv-widget.component.html'
})
export class TvWidget extends Widget {

  status: string;


  constructor(private moskitoApplicationService: MoskitoApplicationService) {
    super();
  }

  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.moskitoApplicationService.applicationChangedEvent.subscribe(() => this.refresh());
    this.refresh();
  }

  public refresh() {
    this.status = this.moskitoApplicationService.currentApplication.applicationColor;
  }
}
