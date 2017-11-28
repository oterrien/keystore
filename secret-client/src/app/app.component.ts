import {Component, OnInit} from '@angular/core';
import {ISecret} from './secret';
import {SecretService} from './secret.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title: string = 'Create a new secret';
  secret: ISecret = null;

  constructor(private _secretService: SecretService) {

  }

  save(): void {
    this._secretService.createSecret(this.secret);
  }


  ngOnInit(): void {
    this._secretService.findSecret('MySecret').subscribe(secret => this.secret = secret);
  }
}
