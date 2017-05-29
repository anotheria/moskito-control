import { Component, OnInit } from "@angular/core";
import { HttpService } from "../services/http.service";
import { SystemStatus } from "../entities/system-status";


@Component({
  selector: 'settings',
  templateUrl: 'settings.component.html',
  styleUrls: ['settings.component.css']
})
export class SettingsComponent implements OnInit{

  moskitoStatus: SystemStatus;


  constructor(private httpService: HttpService) {
  }

  ngOnInit(): void {
    this.httpService.getMoskitoStatus().subscribe((moskitoStatus: SystemStatus) => {
      this.moskitoStatus = moskitoStatus;
    });
  }
}
