import { Injectable } from "@angular/core";
import { MoskitoApplicationService } from "./moskito-application.service";
import { MoskitoComponentUtils } from "../shared/moskito-component-utils";
import { MoskitoCategory } from "../entities/moskito-category";


/**
 * Manages {MoskitoComponent} categories. Used to retrieve possible
 * categories from components and stores currently applied category filter.
 *
 * @author strel
 */
@Injectable()
export class CategoriesService {

  /**
   * Default category, indicating that component belongs to
   * all possible categories.
   */
  public defaultCategory = new MoskitoCategory("All Categories", true);

  /**
   * Moskito component category used as filter for
   * components and history items.
   */
  private _filter: MoskitoCategory;


  constructor(private moskitoApplicationService: MoskitoApplicationService) {
    this._filter = JSON.parse(sessionStorage.getItem('category'));

    if (!this._filter)
      this._filter = this.defaultCategory;
  }


  /**
   * Builds list of all possible categories from
   * current application components.
   *
   * @returns {Array} current application categories
   */
  public getCategories(): MoskitoCategory[] {
    // Getting list of current application components
    let components = this.moskitoApplicationService.currentApplication.components;

    let categoriesDictionary = {};

    // Adding default category to dictionary
    this.defaultCategory.status = MoskitoComponentUtils.getWorthComponentStatus(components);
    this.defaultCategory.components = components;
    categoriesDictionary[this.defaultCategory.name] = this.defaultCategory;

    // Building categories for all components
    for (let component of components) {
      let category = categoriesDictionary[component.category];
      if (!category) {
        category = new MoskitoCategory();
        category.name = component.category;
        category.status = component.color;
        category.active = this.filter.name === category.name;
        category.all = false;
        category.components = [];
      }

      // Changing category status to worth
      category.status = MoskitoComponentUtils.getWorthStatus([component.color, category.status]);
      category.components.push(component);

      categoriesDictionary[component.category] = category;
    }

    // Moving categories from dictionary to array
    let categories = [];
    for (let categoryName in categoriesDictionary) {
      categories.push(categoriesDictionary[categoryName]);
    }

    return categories;
  }

  public resetFilter() {
    this._filter = this.defaultCategory;
    sessionStorage.setItem('category', JSON.stringify(this.defaultCategory));
  }

  set filter(filter: MoskitoCategory) {
    this._filter = filter;
    sessionStorage.setItem('category', JSON.stringify(this._filter));
  }

  get filter(): MoskitoCategory {
    if (!this._filter)
      this._filter = JSON.parse(sessionStorage.getItem('category'));

    return this._filter;
  }

  get filterString(): string {
    if (!this._filter)
      this._filter = JSON.parse(sessionStorage.getItem('category'));

    return this._filter == this.defaultCategory ? "" : this._filter.name;
  }
}
