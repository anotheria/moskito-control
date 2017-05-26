import {Component, OnInit} from "@angular/core";
import {MoskitoCategory} from "../entities/moskito-category";
import {MoskitoApplicationService} from "../services/moskito-application.service";
import {MoskitoComponentUtils} from "../shared/moskito-component-utils";


@Component({
  selector: 'categories',
  templateUrl: 'categories.component.html'
})
export class CategoriesComponent implements OnInit {

  categories: MoskitoCategory[];


  constructor(private moskitoApplicationService: MoskitoApplicationService) { }

  public ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.refresh();
  }

  public refresh() {
    // Getting list of current application components
    let components = this.moskitoApplicationService.currentApplication.components;

    let categoriesDictionary = {
      "All Categories": {
        name: "All Categories",
        status: MoskitoComponentUtils.getWorthComponentStatus(components),
        active: true,
        all: true,
        components: components
      }
    };

    // Building categories for all components
    for (let component of components) {
      let category = categoriesDictionary[component.category];
      if (!category) {
        category = new MoskitoCategory();
        category.name = component.category;
        category.status = component.color;
        category.active = false;
        category.all = false;
        category.components = [];
      }

      // Changing category status to worth
      category.status = MoskitoComponentUtils.getWorthStatus([component.color, category.status])
      category.components.push(component);

      categoriesDictionary[component.category] = category;
    }

    // Moving categories from dictionary to array
    this.categories = [];
    for (let categoryName in categoriesDictionary) {
      this.categories.push(categoriesDictionary[categoryName]);
    }
  }
}
