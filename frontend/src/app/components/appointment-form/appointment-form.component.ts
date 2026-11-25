import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { AppointmentService } from '../../services/appointment.service';
import { AppointmentRequest } from '../../models/appointment.model';

@Component({
  selector: 'app-appointment-form',
  templateUrl: './appointment-form.component.html',
  styleUrls: ['./appointment-form.component.scss']
})
export class AppointmentFormComponent {
  form: FormGroup;
  submitting = false;
  successMessage = '';
  errorMessage = '';
  minDate: string;

  constructor(private fb: FormBuilder, private service: AppointmentService) {
    const min = new Date(Date.now() + 60 * 60 * 1000);
    this.minDate = this.toLocalIso(min);

    this.form = this.fb.group({
      customerName: ['', [Validators.required, Validators.maxLength(120)]],
      customerEmail: ['', [Validators.required, Validators.email, Validators.maxLength(180)]],
      appointmentTime: ['', [Validators.required, this.futureValidator]],
      notes: ['', [Validators.maxLength(255)]]
    });
  }

  futureValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const selected = new Date(control.value).getTime();
    return selected > Date.now() ? null : { notFuture: true };
  }

  private toLocalIso(d: Date): string {
    const pad = (n: number) => n.toString().padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
  }

  submit(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    const payload: AppointmentRequest = {
      customerName: this.form.value.customerName!.trim(),
      customerEmail: this.form.value.customerEmail!.trim(),
      appointmentTime: `${this.form.value.appointmentTime}:00`,
      notes: this.form.value.notes?.trim() || undefined
    };

    this.service.book(payload).subscribe({
      next: (apt) => {
        this.submitting = false;
        this.successMessage = `✅ Appointment booked! ID: ${apt.id} — Confirmation sent to ${apt.customerEmail}.`;
        this.form.reset();
      },
      error: (err: Error) => {
        this.submitting = false;
        this.errorMessage = err.message;
      }
    });
  }

  invalid(field: string): boolean {
    const c = this.form.get(field);
    return !!c && c.invalid && (c.touched || c.dirty);
  }
}