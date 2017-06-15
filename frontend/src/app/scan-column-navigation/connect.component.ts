import { Component } from "@angular/core";
import { HttpService } from "../services/http.service";


/**
 * @author strel
 */
@Component({
  selector: 'quick-connect',
  templateUrl: 'connect.component.html',
  styleUrls: ['connect.component.css']
})
export class ConnectComponent {

  host: string;
  port: string;
  path: string;


  constructor(private httpService: HttpService) {

  }

  connectServer() {
    this.httpService.changeServer( this.host + this.port + this.path );
  }
}
