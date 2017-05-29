import { MoskitoApplicationStatus } from "./moskito-application-status";
import { UpdaterStatus } from "./updater-status";


export class SystemStatus {

  applicationStatuses: MoskitoApplicationStatus[];
  statusUpdater: UpdaterStatus;
  chartsUpdater: UpdaterStatus;


  constructor() {
    this.applicationStatuses = [];
  }
}
