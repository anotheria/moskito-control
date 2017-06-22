import { Injectable, EventEmitter } from "@angular/core";
import { MoskitoApplication } from "../entities/moskito-application";
import { MoskitoComponent } from "../entities/moskito-component";


@Injectable()
export class MoskitoApplicationService {

  version: string = "1.1.1-SNAPSHOT";
  configToggle: boolean = false;

  applications: MoskitoApplication[];
  currentApplication: MoskitoApplication;

  dataRefreshEvent: EventEmitter<void>;
  applicationChangedEvent: EventEmitter<void>;

  /**
   * Analog of java {@code pageContext.request.contextPath}.
   * Contains name of tomcat web application.
   */
  private applicationContextPath: string;


  constructor() {
    this.dataRefreshEvent = new EventEmitter<void>();
    this.applicationChangedEvent = new EventEmitter<void>();
  }

  public refreshData() {
    this.dataRefreshEvent.emit();
  }

  public switchApplication(app: MoskitoApplication)  {
    if (!app) return;
    this.currentApplication = app;
    this.applicationChangedEvent.emit();
  }

  public getComponent(componentName: string): MoskitoComponent {
    if (!componentName) return;

    for (let component of this.currentApplication.components) {
      if (component.name == componentName)
        return component;
    }

    return null;
  }

  public setApplicationContextPath(path: string) {
    this.applicationContextPath = path;
  }

  public getApplicationContextPath(): string {
    return this.applicationContextPath;
  }
}
