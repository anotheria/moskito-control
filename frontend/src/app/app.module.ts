import { SharedModule } from './shared/shared.module';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { ScanColumnNavigationComponent } from './scan-column-navigation/scan-column-navigation.component';
import { ContentComponent } from './content/content.component';

import {DataService} from "./services/data.service";
import {CategoriesComponent} from "./scan-column-navigation/categories.component";
import {WidgetsToggleComponent} from "./scan-column-navigation/widgets-toggle.component";
import {StatisticsComponent} from "./scan-column-navigation/statistics.component";
import {TvWidget} from "./widgets/tv-widget.component";
import {ChartsWidget} from "./widgets/charts-widget.component";
import {HistoryWidget} from "./widgets/history-widget.component";
import {MoskitoComponentsWidget} from "./widgets/moskito-components-widget.component";
import {SettingsComponent} from "./content/settings.component";

@NgModule({
  declarations: [
    AppComponent,
    ScanColumnNavigationComponent,
    ContentComponent,
    CategoriesComponent,
    WidgetsToggleComponent,
    StatisticsComponent,
    TvWidget,
    ChartsWidget,
    HistoryWidget,
    MoskitoComponentsWidget,
    SettingsComponent
  ],
  imports: [
    SharedModule
  ],
  providers: [DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
