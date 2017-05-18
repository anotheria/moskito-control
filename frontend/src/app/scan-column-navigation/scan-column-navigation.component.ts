import { Component, OnInit, Input } from '@angular/core';
import { DataService } from "../services/data.service";

import {Widget} from "../widgets/widget.component";
import {Category} from "../entities/category";
import {StatusStatistics} from "../entities/status-statistic";


@Component({
  selector: 'scan-column',
  templateUrl: './scan-column-navigation.component.html'
})
export class ScanColumnNavigationComponent implements OnInit {
  version: string;
  configToggle: boolean;

  categories: Category[];
  widgets: Widget[];
  statistics: StatusStatistics[];

  constructor(private dataService: DataService) {
  }


  public ngOnInit(): void {
    let jsonData = this.dataService.scan_column_data;

    this.version = jsonData.version;
    this.configToggle = jsonData.configToggle;

    this.categories = jsonData.categories;
    this.widgets = jsonData.widgets;
    this.statistics = jsonData.statistics;
  }
}
