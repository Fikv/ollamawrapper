import { Component } from '@angular/core';

@Component({
  selector: 'app-log-in-component',
  standalone: true,
  imports: [],
  templateUrl: './log-in-component.component.html',
  styleUrl: './log-in-component.component.css'
})
export class LogInComponentComponent {
  credentials = { username: '', password: ''}
user: any;

}
