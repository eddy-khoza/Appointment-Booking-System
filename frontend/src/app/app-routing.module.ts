import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppointmentFormComponent } from './components/appointment-form/appointment-form.component';
import { AppointmentListComponent } from './components/appointment-list/appointment-list.component';
import { AppointmentDetailComponent } from './components/appointment-detail/appointment-detail.component';

const routes: Routes = [
  { path: '', redirectTo: 'book', pathMatch: 'full' },
  { path: 'book', component: AppointmentFormComponent },
  { path: 'appointments', component: AppointmentListComponent },
  { path: 'appointments/:id', component: AppointmentDetailComponent },
  { path: '**', redirectTo: 'book' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}