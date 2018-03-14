import { Injectable } from "@angular/core";


@Injectable()
export class StatusService {

  private _filter: string[];


  constructor() {
    this._filter = JSON.parse(sessionStorage.getItem('status'));

    if (!this._filter)
      this._filter = [];
  }


  public resetFilter() {
    this._filter = [];
    sessionStorage.setItem('status', JSON.stringify([]));
  }

  set filter(filter: string[]) {
    this._filter = filter;
    sessionStorage.setItem('status', JSON.stringify(filter));
  }

  get filter(): string[] {
    if (!this._filter)
      this._filter = JSON.parse(sessionStorage.getItem('status'));

    return this._filter;
  }

  public addFilter(color: string) {
    if (this._filter.indexOf(color) === -1) {
      this._filter.push(color);

      // Make new copy of array and assign to same variable to trigger angular change detection
      this._filter = this._filter.slice();

      sessionStorage.setItem('status', JSON.stringify(this._filter));
    }
  }

  public removeFilter(filterColor: string) {
    this._filter = this._filter.filter((color: string) => {
      return color !== filterColor;
    });

    sessionStorage.setItem('status', JSON.stringify(this._filter));
  }
}
