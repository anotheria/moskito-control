import { MoskitoAnalyzeRestService } from "./../moskito-analyze/services/moskito-analyze-rest.service";
import { Component, OnInit, Input } from "@angular/core";
import { HttpService } from "../services/http.service";


@Component({
  selector: 'app-moskito-control-config',
  templateUrl: './moskito-control-config.component.html',
  styleUrls: ['./moskito-control-config.component.css']
})
export class MoskitoControlConfigComponent implements OnInit {

  configuration: any;
  text: string;
  options: any = { maxLines: 1000, printMargin: false };

  @Input()
  analyzeMode: boolean;


  constructor(
    private analyzeRest: MoskitoAnalyzeRestService,
    private rest: HttpService
  ) { }

  ngOnInit() {
    if (this.analyzeMode) {
      this.analyzeRest.getMoskitoAnalyzeConfig().subscribe((configuration) => {
        this.configuration = configuration;
        this.text = JSON.stringify(this.configuration, undefined, 2);
      });
    }
    else {
      this.rest.getMoskitoConfiguration().subscribe((configuration) => {
        this.configuration = configuration;
        this.text = JSON.stringify(this.configuration, undefined, 2);
      });
    }
  }

  onChange(event: Event) {

  }
}
