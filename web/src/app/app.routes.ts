import { Routes } from '@angular/router';
import { LogInComponentComponent } from './log-in-component/log-in-component.component';
import { ChatComponent } from './chat/chat.component';

export const routes: Routes = [
  { path: 'login', component: LogInComponentComponent },
  {path: 'chat', component: ChatComponent},
  { path: '', redirectTo: 'login', pathMatch: 'full' },
];
