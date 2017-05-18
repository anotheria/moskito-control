import { Component, OnInit } from '@angular/core';
import { DataService } from "./services/data.service";
import { Widget } from "./widgets/widget.component";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  widgets: Widget[];


  constructor(private dataService: DataService) { }

  public ngOnInit() {
    this.widgets = this.dataService.scan_column_data.widgets;
  }
}
