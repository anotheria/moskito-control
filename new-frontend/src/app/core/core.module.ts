import { Injector, NgModule, Optional, SkipSelf } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { HttpClientModule } from '@angular/common/http';
import { IconsService } from './icons';
import { BrowserModule } from '@angular/platform-browser';

@NgModule({
    declarations: [],
    imports: [CommonModule, BrowserModule, BrowserAnimationsModule, MatIconModule, HttpClientModule],
})
export class CoreModule {
    constructor(@Optional() @SkipSelf() parentModule: CoreModule, private injector: Injector) {
        if (parentModule) {
            throw new Error('CoreModule is already loaded!');
        }

        CoreModule.registerIcons(injector);
    }

    private static registerIcons(injector: Injector): void {
        injector.get(IconsService).registerIcons();
    }
}
