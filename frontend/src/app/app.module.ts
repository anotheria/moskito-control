import {SharedModule} from "./shared/shared.module";
import {NgModule} from "@angular/core";
import {AppComponent} from "./app.component";
import {ScanColumnNavigationComponent} from "./scan-column-navigation/scan-column-navigation.component";
import {ContentComponent} from "./content/content.component";
import {DataService} from "./services/data.service";
import {CategoriesComponent} from "./scan-column-navigation/categories.component";
import {WidgetsToggleComponent} from "./scan-column-navigation/widgets-toggle.component";
import {StatisticsComponent} from "./scan-column-navigation/statistics.component";
import {TvWidget} from "./widgets/tv-widget.component";
import {ChartsWidget} from "./widgets/charts-widget.component";
import {HistoryWidget} from "./widgets/history-widget.component";
import {MoskitoComponentsWidget} from "./widgets/moskito-components-widget.component";
import {SettingsComponent} from "./content/settings.component";
import {WidgetConfigService} from "./services/widget-config.service";


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
  providers: [DataService, WidgetConfigService],
  bootstrap: [AppComponent]
})
export class AppModule { }
