import { SharedModule } from "./shared/shared.module";
import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { ScanColumnNavigationComponent } from "./scan-column-navigation/scan-column-navigation.component";
import { ConnectComponent } from "./scan-column-navigation/connect.component";
import { ContentComponent } from "./content/content.component";
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
import { MoskitoApplicationService } from "./services/moskito-application.service";
import { ChartService } from "./services/chart.service";
import { NotificationsConfigComponent } from "./shared/notifications/notifications-config.component";
import { KeysPipe } from "./pipes/keys.pipe";
import { MoskitoBetaComponentsWidget } from "./widgets/moskito-beta-components-widget.component";
import { CategoriesService } from "./services/categories.service";
import { StatusService } from "./services/status.service";
import { ComponentsCategoryFilterPipe } from "./pipes/components-category-filter.pipe";
import { HistoryCategoryFilterPipe } from "./pipes/history-category-filter.pipe";
import { ComponentsStatusFilterPipe } from "./pipes/components-status-filter.pipe";
import { HistoryStatusFilterPipe } from "./pipes/history-status-filter.pipe";
import { SanitizeHtmlPipe } from "./pipes/sanitarize-html.pipe";
import { HealthStatusService } from "./services/health-status.service";
import { Routes, RouterModule } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { ComponentInspectionModalComponent } from "./widgets/modal/component-inspection-modal.component";


const appRoutes: Routes =[
  {
    path: 'beta',
    component: HomeComponent
  },
  {
    path: '',
    redirectTo: '/beta',
    pathMatch: 'full'
  }
];


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
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
    ConnectComponent,
    MoskitoBetaComponentsWidget,
    ComponentInspectionModalComponent,

    // Pipes
    KeysPipe,
    ComponentsCategoryFilterPipe,
    ComponentsStatusFilterPipe,
    HistoryCategoryFilterPipe,
    HistoryStatusFilterPipe,
    SanitizeHtmlPipe
  ],
  imports: [
    SharedModule,

    // Routes
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    WidgetService,
    HttpService,
    HealthStatusService,
    MoskitoApplicationService,
    ChartService,
    CategoriesService,
    StatusService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
