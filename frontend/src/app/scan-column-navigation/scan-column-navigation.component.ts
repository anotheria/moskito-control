import {Component, OnInit} from "@angular/core";
import {MoskitoApplicationService} from "../services/moskito-application.service";


@Component({
  selector: 'scan-column',
  templateUrl: './scan-column-navigation.component.html'
})
export class ScanColumnNavigationComponent implements OnInit {

  version: string;
  configToggle: boolean;


  constructor(private moskitoApplicationService: MoskitoApplicationService) {
  }


  public ngOnInit(): void {
    this.version = this.moskitoApplicationService.version;
    this.configToggle = this.moskitoApplicationService.configToggle;
  }
}
