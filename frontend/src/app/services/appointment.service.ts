import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Appointment, AppointmentRequest, ApiError } from '../models/appointment.model';

@Injectable({ providedIn: 'root' })
export class AppointmentService {
  private readonly base = `${environment.apiUrl}/appointments`;

  constructor(private http: HttpClient) {}

  book(request: AppointmentRequest): Observable<Appointment> {
    return this.http.post<Appointment>(this.base, request).pipe(
      catchError(this.handleError)
    );
  }

  list(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(this.base).pipe(catchError(this.handleError));
  }

  findById(id: number): Observable<Appointment> {
    return this.http.get<Appointment>(`${this.base}/${id}`).pipe(catchError(this.handleError));
  }

  byEmail(email: string): Observable<Appointment[]> {
    const params = new HttpParams().set('email', email);
    return this.http.get<Appointment[]>(`${this.base}/by-email`, { params }).pipe(catchError(this.handleError));
  }

  cancel(id: number): Observable<Appointment> {
    return this.http.delete<Appointment>(`${this.base}/${id}`).pipe(catchError(this.handleError));
  }

  private handleError = (error: HttpErrorResponse) => {
    let message = 'Unexpected error';
    if (error.error instanceof ErrorEvent) {
      message = error.error.message;
    } else if (error.error && typeof error.error === 'object') {
      const apiErr = error.error as ApiError;
      message = apiErr.message || `Error ${error.status}`;
      if (apiErr.details) {
        const fields = Object.entries(apiErr.details).map(([k, v]) => `${k}: ${v}`).join(', ');
        if (fields) message += ` (${fields})`;
      }
    } else {
      message = `Server error ${error.status}`;
    }
    return throwError(() => new Error(message));
  };
}