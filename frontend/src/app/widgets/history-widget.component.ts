import { Component, OnInit } from "@angular/core";
import { HttpService } from "../services/http.service";
import { MoskitoApplicationService } from "../services/moskito-application.service";
import { Widget } from "./widget.component";
import { HistoryItem } from "../entities/history-item";
import { CategoriesService } from "../services/categories.service";
import { StatusService } from "../services/status.service";


@Component({
  selector: 'history-widget',
  templateUrl: './history-widget.component.html'
})
export class HistoryWidget extends Widget implements OnInit {

  historyItems: HistoryItem[];


  constructor(private httpService: HttpService,
              private moskitoApplicationService: MoskitoApplicationService,
              public categoriesService: CategoriesService,
              public statusService: StatusService) { super(); }

  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.moskitoApplicationService.applicationChangedEvent.subscribe(() => this.refresh());

    this.refresh();
  }

  public refresh() {
    // Getting list of history items for given application
    this.httpService.getApplicationHistory(this.moskitoApplicationService.currentApplication.name).subscribe((historyItems) => {
      this.historyItems = [];

      for (let historyItem of historyItems) {
        let item = new HistoryItem();
        item.component = this.moskitoApplicationService.getComponent(historyItem.componentName);
        item.oldStatus = historyItem.oldStatus;
        item.newStatus = historyItem.newStatus;
        item.timestamp = historyItem.timestamp;
        item.isoTimestamp = historyItem.isoTimestamp;
        item.oldMessages = historyItem.oldMessages;
        item.newMessages = historyItem.newMessages;

        this.historyItems.push(item);
      }
    });
  }
}
