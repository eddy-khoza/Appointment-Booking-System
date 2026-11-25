export interface Appointment {
  id: number;
  customerName: string;
  customerEmail: string;
  appointmentTime: string;
  status: 'CONFIRMED' | 'CANCELLED';
  notes?: string;
  createdAt?: string;
}

export interface AppointmentRequest {
  customerName: string;
  customerEmail: string;
  appointmentTime: string;
  notes?: string;
}

export interface ApiError {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
  path?: string;
  details?: Record<string, string>;
}