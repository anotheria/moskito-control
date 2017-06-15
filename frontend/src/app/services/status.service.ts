import { Injectable } from "@angular/core";


@Injectable()
export class StatusService {

  private filter: string;


  constructor() {
    this.filter = '';
  }


  public resetFilter() {
    this.filter = '';
  }

  public setFilter(filter: string) {
    this.filter = filter;
  }

  public getFilter(): string {
    return this.filter;
  }
}
