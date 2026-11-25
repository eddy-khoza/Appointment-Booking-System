import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppointmentService } from '../../services/appointment.service';
import { Appointment } from '../../models/appointment.model';

@Component({
  selector: 'app-appointment-list',
  templateUrl: './appointment-list.component.html',
  styleUrls: ['./appointment-list.component.scss']
})
export class AppointmentListComponent implements OnInit {
  appointments: Appointment[] = [];
  loading = false;
  errorMessage = '';
  emailFilter = '';
  filterActive = false;

  constructor(private service: AppointmentService, private router: Router) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loading = true;
    this.errorMessage = '';
    this.filterActive = false;
    this.service.list().subscribe({
      next: (data) => { this.appointments = data; this.loading = false; },
      error: (err: Error) => { this.errorMessage = err.message; this.loading = false; }
    });
  }

  searchByEmail(): void {
    const email = this.emailFilter.trim();
    if (!email) { this.loadAll(); return; }

    this.loading = true;
    this.errorMessage = '';
    this.filterActive = true;
    this.service.byEmail(email).subscribe({
      next: (data) => { this.appointments = data; this.loading = false; },
      error: (err: Error) => { this.errorMessage = err.message; this.loading = false; }
    });
  }

  cancel(id: number): void {
    if (!confirm(`Cancel appointment #${id}?`)) return;
    this.service.cancel(id).subscribe({
      next: () => this.refresh(),
      error: (err: Error) => this.errorMessage = err.message
    });
  }

  view(id: number): void {
    this.router.navigate(['/appointments', id]);
  }

  private refresh(): void {
    this.filterActive ? this.searchByEmail() : this.loadAll();
  }

  trackById(_: number, a: Appointment) { return a.id; }
}