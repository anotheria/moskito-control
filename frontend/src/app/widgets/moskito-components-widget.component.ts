import {Component, OnInit} from "@angular/core";
import {Widget} from "./widget.component";
import {MoskitoComponent} from "../entities/moskito-component";
import {MoskitoComponentUtils} from "../shared/moskito-component-utils";
import {MoskitoApplicationService} from "../services/moskito-application.service";
import {HttpService} from "../services/http.service";

declare var showThresholds: any;


@Component({
  selector: 'components-widget',
  templateUrl: 'moskito-components-widget.component.html'
})
export class MoskitoComponentsWidget extends Widget implements OnInit {

  components: MoskitoComponent[];
  categories: any;

  componentUtils: MoskitoComponentUtils;


  constructor(private httpService: HttpService, private moskitoApplicationService: MoskitoApplicationService) {
    super();
    this.componentUtils = MoskitoComponentUtils;
  }

  ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.refresh();
  }

  thresholds(context: string, componentName: string, m: number, n: number) {
    showThresholds(context, componentName, m, n);
  }

  public refresh() {
    this.components = this.moskitoApplicationService.currentApplication.components;
    this.categories = MoskitoComponentUtils.orderComponentsByCategories(this.components);
  }
}
