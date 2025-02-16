import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import {MAT_DIALOG_DATA, MatDialog, MatDialogContent, MatDialogTitle} from '@angular/material/dialog'
import {MatFormFieldModule} from '@angular/material/form-field';


@Component({
  selector: 'app-log-in-component',
  standalone: true,
  imports: [],
  templateUrl: './log-in-component.component.html',
  styleUrl: './log-in-component.component.css'
})
export class LogInComponentComponent {
  constructor(private router: Router) {}

  dialog = inject(MatDialog)

  navigateToChat() {
    this.router.navigate(['/chat'])
  }

  openRegisterDialog() {
    this.dialog.open(RegisterDialog, {
      width: '440px', 
      disableClose: false, 
      hasBackdrop: true, 
      backdropClass: 'blurred-background' 
    });
  }

}

@Component({
  selector: 'register-dialog',
  templateUrl: 'register-dialog.html',
  styleUrl: 'register-dialog.css',
  standalone: true,
  imports: [MatDialogTitle, MatDialogContent, MatFormFieldModule]
})
export class RegisterDialog {
  data = inject(MAT_DIALOG_DATA)
 
}

