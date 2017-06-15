import { Component, OnInit } from "@angular/core";
import { HttpService } from "../services/http.service";


@Component({
  selector: 'settings',
  templateUrl: 'settings.component.html',
  styleUrls: ['settings.component.css']
})
export class SettingsComponent implements OnInit{

  configuration: any;


  constructor(private httpService: HttpService) {
  }

  ngOnInit(): void {
    this.httpService.getMoskitoConfiguration().subscribe(( configuration ) => {
      this.configuration = configuration;
    });
  }
}
