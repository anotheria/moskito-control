import { Injectable } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({
    providedIn: 'root',
})
export class IconsService {
    private readonly regularIcons = [];
    private readonly solidIcons = [];

    constructor(private readonly matIconRegistry: MatIconRegistry, private readonly domSanitizer: DomSanitizer) {}

    public registerIcons(): void {
        this.addIconsToIconsRegistry(this.regularIcons, 'far', 'regular');
        this.addIconsToIconsRegistry(this.solidIcons, 'fas', 'solid');
    }

    private addIconsToIconsRegistry(icons: string[], namespace: string, folder: string) {
        icons.forEach((iconName) => {
            this.matIconRegistry.addSvgIconInNamespace(
                namespace,
                iconName,
                this.domSanitizer.bypassSecurityTrustResourceUrl(`/assets/svgs/${folder}/${iconName}.svg`),
            );
        });
    }
}
