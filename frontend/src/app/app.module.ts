import { SharedModule } from "./shared/shared.module";
import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { ScanColumnNavigationComponent } from "./scan-column-navigation/scan-column-navigation.component";
import { ContentComponent } from "./content/content.component";
import { DataService } from "./services/data.service";
import { CategoriesComponent } from "./scan-column-navigation/categories.component";
import { WidgetsToggleComponent } from "./scan-column-navigation/widgets-toggle.component";
import { StatisticsComponent } from "./scan-column-navigation/statistics.component";
import { TvWidget } from "./widgets/tv-widget.component";
import { ChartsWidget } from "./widgets/charts-widget.component";
import { HistoryWidget } from "./widgets/history-widget.component";
import { MoskitoComponentsWidget } from "./widgets/moskito-components-widget.component";
import { SettingsComponent } from "./settings/settings.component";
import { WidgetService } from "./services/widget.service";
import { HttpService } from "./services/http.service";
import { ApplicationColorService } from "./services/application-color.service";
import { MoskitoApplicationService } from "./services/moskito-application.service";
import { ChartService } from "./services/chart.service";
import { NotificationsConfigComponent } from "./shared/notifications/notifications-config.component";
import { DerpPipe } from "./pipes/derp.pipe";
import { KeysPipe } from "./pipes/keys.pipe";


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
    SettingsComponent,
    NotificationsConfigComponent,

    // Pipes
    DerpPipe,
    KeysPipe
  ],
  imports: [
    SharedModule
  ],
  providers: [DataService, WidgetService, HttpService, ApplicationColorService, MoskitoApplicationService, ChartService],
  bootstrap: [AppComponent]
})
export class AppModule { }
