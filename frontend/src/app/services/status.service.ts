import { Injectable } from "@angular/core";


@Injectable()
export class StatusService {

  private filter: string[];


  constructor() {
    this.filter = [];
  }


  public resetFilter() {
    this.filter = [];
  }

  public setFilter(filter: string[]) {
    this.filter = filter;
  }

  public getFilter(): string[] {
    return this.filter;
  }

  public addFilter(color: string) {
    if (this.filter.indexOf(color) === -1) {
      this.filter.push(color);

      // Make new copy of array and assign to same variable to trigger angular change detection
      this.filter = this.filter.slice();
    }
  }

  public removeFilter(filterColor: string) {
    this.filter = this.filter.filter((color: string) => {
      return color !== filterColor;
    });
  }
}
