import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Appointment Booking System';
  menuOpen = false;
  toggleMenu() { this.menuOpen = !this.menuOpen; }
}