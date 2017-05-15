
import { Component, Input } from "@angular/core";


@Component({
  selector: 'settings',
  templateUrl: 'settings.component.html'
})
export class SettingsComponent {

  @Input()
  configuration: any;

}
