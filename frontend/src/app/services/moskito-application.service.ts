import {Injectable, EventEmitter} from "@angular/core";
import {MoskitoApplication} from "../entities/moskito-application";


@Injectable()
export class MoskitoApplicationService {

  version: string = "1.1.1-SNAPSHOT";
  configToggle: boolean = false;

  applications: MoskitoApplication[];
  currentApplication: MoskitoApplication;

  dataRefreshEvent: EventEmitter<void>;


  constructor() {
    this.dataRefreshEvent = new EventEmitter<void>();
  }

  public refreshData() {
    this.dataRefreshEvent.emit();
  }
}
