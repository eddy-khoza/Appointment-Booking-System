import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppointmentService } from '../../services/appointment.service';
import { Appointment } from '../../models/appointment.model';

@Component({
  selector: 'app-appointment-detail',
  templateUrl: './appointment-detail.component.html',
  styleUrls: ['./appointment-detail.component.scss']
})
export class AppointmentDetailComponent implements OnInit {
  appointment?: Appointment;
  loading = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: AppointmentService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id || Number.isNaN(id)) {
      this.errorMessage = 'Invalid appointment ID';
      this.loading = false;
      return;
    }
    this.service.findById(id).subscribe({
      next: (a) => { this.appointment = a; this.loading = false; },
      error: (err: Error) => { this.errorMessage = err.message; this.loading = false; }
    });
  }

  back(): void { this.router.navigate(['/appointments']); }

  cancel(): void {
    if (!this.appointment) return;
    if (!confirm('Cancel this appointment?')) return;
    this.service.cancel(this.appointment.id).subscribe({
      next: (a) => { this.appointment = a; },
      error: (err: Error) => { this.errorMessage = err.message; }
    });
  }
}