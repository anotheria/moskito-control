import { Component, OnInit } from "@angular/core";
import { HttpService } from "../services/http.service";
import { MoskitoApplicationService } from "../services/moskito-application.service";


@Component({
  selector: 'home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  applicationDataLoaded: boolean;


  constructor(private moskitoApplicationService: MoskitoApplicationService, private httpService: HttpService) { }

  public ngOnInit() {
    // Getting list of all applications
    this.httpService.getMoskitoApplications().subscribe((applications) => {
      this.moskitoApplicationService.applications = applications;
      this.moskitoApplicationService.currentApplication = applications[0];

      this.applicationDataLoaded = true;
    });
  }
}
