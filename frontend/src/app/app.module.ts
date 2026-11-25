import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AppointmentFormComponent } from './components/appointment-form/appointment-form.component';
import { AppointmentListComponent } from './components/appointment-list/appointment-list.component';
import { AppointmentDetailComponent } from './components/appointment-detail/appointment-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    AppointmentFormComponent,
    AppointmentListComponent,
    AppointmentDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}