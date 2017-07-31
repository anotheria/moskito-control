import { Component, OnInit } from "@angular/core";
import { MoskitoAnalyzeRestService } from "./services/moskito-analyze-rest.service";
import { WidgetService } from "../services/widget.service";
import { MoskitoAnalyzeService } from "./services/moskito-analyze.service";

@Component({
  selector: 'app-moskito-analyze',
  templateUrl: './moskito-analyze.component.html',
  styleUrls: ['./moskito-analyze.component.css']
})
export class MoskitoAnalyzeComponent implements OnInit {

  /**
   * Indicates whether general MoSKito-Analyze configuration is loaded.
   */
  initialized: boolean;


  constructor(
    public widgetService: WidgetService,
    private moskitoAnalyze: MoskitoAnalyzeService,
    private moskitoAnalyzeRest: MoskitoAnalyzeRestService
  ) {
    this.initialized = false;
  }

  ngOnInit() {
    // Initializing basic moskito-analyze properties
    this.moskitoAnalyzeRest.getMoskitoAnalyzeConfig().subscribe((conf) => {
      this.moskitoAnalyze.url = conf.url;
      this.moskitoAnalyze.hosts = conf.hosts;
      this.initialized = true;
    });
  }
}
