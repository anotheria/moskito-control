import { Component, OnInit } from "@angular/core";
import { MoskitoCategory } from "../entities/moskito-category";
import { CategoriesService } from "../services/categories.service";
import { MoskitoApplicationService } from "../services/moskito-application.service";


@Component({
  selector: 'categories',
  templateUrl: 'categories.component.html'
})
export class CategoriesComponent implements OnInit {

  categories: MoskitoCategory[];


  constructor(private moskitoApplicationService: MoskitoApplicationService, private categoriesService: CategoriesService) {
  }


  public ngOnInit() {
    this.moskitoApplicationService.dataRefreshEvent.subscribe(() => this.refresh());
    this.moskitoApplicationService.applicationChangedEvent.subscribe(() => this.refresh());
    this.refresh();
  }

  public refresh() {
    this.categories = this.categoriesService.getCategories();
  }

  public setFilter(category: MoskitoCategory) {
    this.categoriesService.setFilter(category);
  }
}
