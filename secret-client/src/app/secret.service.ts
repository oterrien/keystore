import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';

import {ISecret} from './secret';

@Injectable()
export class SecretService {

  //private _secretUrl = 'http://localhost:8080/api/v1/secrets/';
  private _secretUrl = './api/secrets/secret.json';

  constructor(private _http: HttpClient) {
  }

  findSecret(name: string): Observable<ISecret> {
    //return this._http.get<ISecret>(this._secretUrl + '/' + name);
    return this._http.get<ISecret>(this._secretUrl);
  }

  createSecret(secret: ISecret): Observable<void> {
    return this._http.post<void>(this._secretUrl, secret);
  }
}
