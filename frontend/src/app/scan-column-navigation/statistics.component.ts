
import {Component, Input} from "@angular/core";
import {StatusStatistics} from "../entities/status-statistic";


@Component({
  selector: 'statistics',
  templateUrl: 'statistics.component.html'
})
export class StatisticsComponent {

  @Input()
  statistics: StatusStatistics[];

}
