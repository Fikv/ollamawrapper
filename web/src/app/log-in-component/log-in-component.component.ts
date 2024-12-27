import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-log-in-component',
  standalone: true,
  imports: [],
  templateUrl: './log-in-component.component.html',
  styleUrl: './log-in-component.component.css'
})
export class LogInComponentComponent {
  constructor(private router: Router) {}
  navigateToChat() {
    this.router.navigate(['/chat'])
  }
}
