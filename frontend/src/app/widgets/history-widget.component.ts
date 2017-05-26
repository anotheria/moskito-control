import {Component, OnInit} from "@angular/core";
import {HttpService} from "../services/http.service";
import {MoskitoApplicationService} from "../services/moskito-application.service";
import {Widget} from "./widget.component";
import {HistoryItem} from "../entities/history-item";


@Component({
  selector: 'history-widget',
  templateUrl: './history-widget.component.html'
})
export class HistoryWidget extends Widget implements OnInit {

  historyItems: HistoryItem[];


  constructor(private httpService: HttpService, private moskitoApplicationService: MoskitoApplicationService) { super(); }

  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.refresh();
  }

  public refresh() {
    // Getting list of history items for given application
    this.httpService.getApplicationHistory(this.moskitoApplicationService.currentApplication.name).subscribe((historyItems) => {
      this.historyItems = historyItems;
    });
  }
}
