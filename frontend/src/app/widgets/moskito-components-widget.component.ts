import { Component, OnInit } from "@angular/core";
import { Widget } from "./widget.component";
import { MoskitoComponent } from "../entities/moskito-component";
import { MoskitoComponentUtils } from "../shared/moskito-component-utils";
import { MoskitoApplicationService } from "../services/moskito-application.service";
import { HttpService } from "../services/http.service";
import { StatusService } from "../services/status.service";
import { CategoriesService } from "../services/categories.service";

declare var SetupComponentsView: any;


@Component({
  selector: 'components-widget',
  templateUrl: 'moskito-components-widget.component.html'
})
export class MoskitoComponentsWidget extends Widget implements OnInit {

  components: MoskitoComponent[];
  categories: any;

  componentUtils: MoskitoComponentUtils;

  thresholds: string;
  accumulators: string;


  constructor(
    private httpService: HttpService,
    private moskitoApplicationService: MoskitoApplicationService,
    private categoriesService: CategoriesService,
    private statusService: StatusService
  ) {
    super();
    this.componentUtils = MoskitoComponentUtils;
  }


  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.moskitoApplicationService.applicationChangedEvent.subscribe(() => this.refresh());
    this.refresh();
  }

  getComponentInspectionModalData( componentName: string ) {
    let currentApp = this.moskitoApplicationService.currentApplication;
    if (!currentApp)
      return;

    // Obtaining threshold templates
    this.httpService.getThresholds( currentApp.name, componentName ).subscribe(( thresholds ) => {
      this.thresholds = thresholds;
    });

    // Obtaining accumulator templates
    this.httpService.getAccumulatorsList( currentApp.name, componentName ).subscribe(( accumulators ) => {
      this.accumulators = accumulators;
    });
  }

  public refresh() {
    this.components = this.moskitoApplicationService.currentApplication.components;
    this.categories = MoskitoComponentUtils.orderComponentsByCategories(this.components);

    // Initialize drag-n-drop and tooltips for components
    setTimeout(() => {
      SetupComponentsView();
    }, 1000);
  }
}
