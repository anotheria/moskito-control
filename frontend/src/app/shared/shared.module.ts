import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import {TimerComponent} from "./timer/timer.component";


@NgModule({
   declarations: [
       TimerComponent
   ],
   imports: [
       BrowserModule,
       FormsModule,
       ReactiveFormsModule,
       HttpModule
   ],
   exports: [
       // Shared Modules
       BrowserModule,
       FormsModule,
       ReactiveFormsModule,
       HttpModule,

       // Shared Components
       TimerComponent
   ],
   providers: [],
})
export class SharedModule { }
