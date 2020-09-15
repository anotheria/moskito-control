import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { AppRoutingModule } from './app-routing.module';

@NgModule({
    declarations: [AppComponent],
    imports: [AppRoutingModule, CoreModule],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
