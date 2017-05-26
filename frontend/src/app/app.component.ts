import {Component, OnInit} from "@angular/core";
import {HttpService} from "./services/http.service";
import {MoskitoApplicationService} from "./services/moskito-application.service";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  applicationDataLoaded: boolean;


  constructor(private moskitoApplicationService: MoskitoApplicationService, private httpService: HttpService) { }

  public ngOnInit() {
    // Getting list of all aplications
    this.httpService.getMoskitoApplications().subscribe((applications) => {
      this.moskitoApplicationService.applications = applications;
      this.moskitoApplicationService.currentApplication = applications[0];

      this.applicationDataLoaded = true;
    });
  }
}
