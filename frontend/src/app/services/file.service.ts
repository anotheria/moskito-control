import { Injectable } from '@angular/core';
import { Http } from '@angular/http';


@Injectable()
export class FileService {
  constructor( private http: Http ) {}

  public getData(fileName: string) {
    return this.http.get( fileName );
  }
}
