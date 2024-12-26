import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LogInComponentComponent } from "./log-in-component/log-in-component.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LogInComponentComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'ollamawrapper-web';
}
