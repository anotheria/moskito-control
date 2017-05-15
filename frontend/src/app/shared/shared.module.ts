import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';


@NgModule({
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
   ],
   declarations: [],
   providers: [],
})
export class SharedModule { }
