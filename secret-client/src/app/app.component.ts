import { Component } from '@angular/core';
import { ISecret } from './secret';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  title: string = "Page de test";
  secret: ISecret = {"name":"", "description":"", "value":""};
  name: string = "name de test";
  description: string;
  value: string;


  save(): void {
    console.log(this.secret);
  }

}
