
import {MoskitoComponent} from "./moskito-component";
import {MoskitoConnector} from "./moskito-connector";
import {Chart} from "./chart";
import {StatusUpdater} from "./status-updater";
import {ChartsUpdater} from "./charts-updater";


export class Application {
  name: string;
  color: string;
  active: boolean;
}

export class MoskitoApplication {
  name: string;
  components: MoskitoComponent[];
  charts: Chart[];
  connectors: MoskitoConnector[];
  statusUpdater: StatusUpdater;
  chartsUpdater: ChartsUpdater;
}
