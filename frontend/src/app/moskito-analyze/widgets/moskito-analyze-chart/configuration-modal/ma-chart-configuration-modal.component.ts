import { MoskitoAnalyzeChart } from "./../../../model/moskito-analyze-chart.model";
import { MoskitoAnalyzeRestService } from "./../../../services/moskito-analyze-rest.service";
import { EventEmitter, Component, Input, OnInit, Output } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { MoskitoAnalyzeService } from "../../../services/moskito-analyze.service";
import { IMultiSelectOption, IMultiSelectSettings } from "angular-2-dropdown-multiselect";
import { UUID } from "angular2-uuid";


@Component({
  selector: 'ma-configuration-modal',
  templateUrl: './ma-chart-configuration-modal.component.html',
  styleUrls: ['./ma-chart-configuration-modal.component.css']
})
export class MoskitoAnalyzeChartConfigurationModalComponent implements OnInit {

  /**
   * Chart data to update.
   */
  @Input()
  chart: MoskitoAnalyzeChart;

  /**
   * Determines behavior / purpose of modal window: create or update chart.
   */
  @Input()
  action: string;

  /**
   * Update event. Fires when chart is ready to be updated.
   * @type {EventEmitter<MoskitoAnalyzeChart>}
   */
  @Output()
  onChartConfigurationUpdate = new EventEmitter<MoskitoAnalyzeChart>();

  /**
   * Create event. Fires when user wants to create new chart.
   * @type {EventEmitter<MoskitoAnalyzeChart>}
   */
  @Output()
  onChartConfigurationCreate = new EventEmitter<MoskitoAnalyzeChart>();

  /**
   * Chart form parameters.
   */
  chartForm: FormGroup;

  /**
   * List of selected hosts.
   * Used for custom multi select component.
   */
  selectedHosts: number[];

  /**
   * List of possible hosts that can be selected.
   * Used for custom multi select component.
   */
  availableHosts: IMultiSelectOption[];

  /**
   * Multi Select component settings.
   */
  hostsSettings: IMultiSelectSettings;

  /**
   * List of producer names that can be selected for given chart
   * @type {Array}
   */
  producers: string[] = [];

  /**
   * List of stat names that can be selected for given producer
   * @type {Array}
   */
  stats: string[] = [];

  /**
   * List of value names that can be selected for given stat
   * @type {Array}
   */
  values: string[] = [];


  constructor(
    public activeModal: NgbActiveModal,
    private moskitoAnalyze: MoskitoAnalyzeService,
    private rest: MoskitoAnalyzeRestService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.availableHosts = [
      { id: 1, name: 'DE1ANI3BURGR201' },
      { id: 2, name: 'DE1ANI3BURGR302' }
    ];

    this.hostsSettings = {
      checkedStyle: 'fontawesome',
      buttonClasses: 'custom-dropdown-block',
      containerClasses: 'dropdown-block'
    };

    this.selectedHosts = this.getHostIdsByNames(this.chart.hosts);
    this.buildChartForm();

    this.producers = this.getProducerNames(this.producerData);
    this.stats = this.getStatNames(this.chart.producer, this.producerData);
    this.values = this.getValueNames(this.chart.producer, this.chart.stat, this.producerData);

    this.producerNameChange();
    this.statNameChange();
  }

  /**
   * Replaces chart configuration object in service stored producers or adds the new one.
   */
  saveChartConfiguration() {
    const chart = new MoskitoAnalyzeChart();

    chart.id = this.chart.id;
    chart.name = this.chart.name ? this.chart.name : this.generateChartName();
    chart.type = this.chartForm.value.type;
    chart.interval = this.chartForm.value.interval;
    chart.hosts = this.resolveHostsByIds(this.selectedHosts);
    chart.startDate = new Date(this.chartForm.value.startDate);
    chart.endDate = new Date(this.chartForm.value.endDate);
    chart.caption = this.chartForm.value.caption;
    chart.producer = this.chartForm.value.producer;
    chart.stat = this.chartForm.value.stat;
    chart.value = this.chartForm.value.value;

    // Fire chart updated or created action
    if (this.action === 'update')
      this.onChartConfigurationUpdate.emit(chart);
    else if (this.action === 'create') {
      this.onChartConfigurationCreate.emit(chart);
    }

    this.activeModal.close();
  }

  /**
   * @param data
   * @returns {Array} List of producer names
   */
  private getProducerNames(data: any[]) {
    let producers = [];

    for (let producer of data) {
      producers.push(producer.name);
    }

    return producers;
  }

  /**
   * Retrieves list of stat names that can be selected with given producer
   * @param producerName
   * @param data
   * @returns {Array}
   */
  private getStatNames(producerName: string, data: any[]) {
    let stats = [];

    let producer = this.getProducerByName(producerName, data);
    if (!producer) return [];

    for (let stat of producer.stats) {
      stats.push(stat.name);
    }

    return stats;
  }

  /**
   * Retrieves list of value names that can be selected with given producer and stat
   * @param producerName
   * @param statName
   * @param data
   * @returns {Array}
   */
  private getValueNames(producerName: string, statName: string, data: any[]) {
    let producer = this.getProducerByName(producerName, data);
    if (!producer) return [];

    let stat = this.getStatByName(statName, producer.stats);
    if (!stat) return [];

    return stat.values;
  }

  /**
   * Returns producer object by given name
   * @param name
   * @param producers
   * @returns {any}
   */
  private getProducerByName(name: string, producers: any[]) {
    for (let producer of producers) {
      if (producer.name === name) {
        return producer;
      }
    }
  }

  /**
   * Returns stat object by given name
   * @param name
   * @param stats
   * @returns {any}
   */
  private getStatByName(name: string, stats: any[]) {
    for (let stat of stats) {
      if (stat.name === name) {
        return stat;
      }
    }
  }

  /**
   * When user selects another producer,
   * lists of stats and values are refreshed.
   */
  private producerNameChange() {
    const producerControl = this.chartForm.get('producer');
    producerControl.valueChanges.forEach(
      (producerName: string) => {
        this.stats = this.getStatNames(producerName, this.producerData);
        this.values = this.getValueNames(producerName, this.chartForm.value.stat, this.producerData);
      }
    );
  }

  /**
   * When user selects another stat, list of possible
   * value names is refreshed.
   */
  private statNameChange() {
    const statControl = this.chartForm.get('stat');
    statControl.valueChanges.forEach(
      (statName: string) => this.values = this.getValueNames(this.chartForm.value.producer, statName, this.producerData)
    );
  }

  /**
   * TODO: Remove this shit.
   * New charts will get UUID as name.
   * @returns {string}
   */
  private generateChartName(): string {
    return UUID.UUID();
  }

  /**
   * Returns list of host names by their indexes from array.
   * @param ids
   * @returns {string[]}
   */
  private resolveHostsByIds(ids: number[]): string[] {
    let hosts: string[] = [];

    for (let id of ids) {
      hosts.push(this.availableHosts[id - 1].name);
    }

    return hosts;
  }

  /**
   * Returns host indexes in array by given host names.
   * @param hosts
   */
  private getHostIdsByNames(hosts: string[]) {
    let hostNames = [];

    for (let hostName of hosts) {
      for (let hostItem of this.availableHosts) {
        if (hostItem.name === hostName) {
          hostNames.push(hostItem.id);
        }
      }
    }

    return hostNames;
  }

  /**
   * Initializes chart form parameters.
   */
  private buildChartForm() {
    this.chartForm = this.fb.group({
      type: [ this.chart.type, [ Validators.required ] ],
      interval: [ this.chart.interval, [ Validators.required ] ],
      startDate: [ this.moskitoAnalyze.formatDate(this.chart.startDate), [ Validators.required ] ],
      endDate: [ this.moskitoAnalyze.formatDate(this.chart.endDate), [ Validators.required ] ],
      caption: [ this.chart.caption, [ Validators.required ] ],
      producer: [ this.chart.producer, [ Validators.required ] ],
      stat: [ this.chart.stat, [ Validators.required ] ],
      value: [ this.chart.value, [ Validators.required ] ]
    });
  }

  /**
   * TODO: TEST DATA
   */
  private producerData = [{"name":"ActivityAPI","stats":[{"name":"cumulated","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"init","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"notifyMyActivity","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]}]},{"name":"Domain","stats":[{"name":"-other-","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"cumulated","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"localhost","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]}]},{"name":"ErrorProducer","stats":[{"name":"cumulated","values":["INITIAL","MAXINITIAL","MAXRETHROWN","MAXTOTAL","RETHROWN","TOTAL"]}]},{"name":"GC","stats":[{"name":"PS MarkSweep","values":["CurrentCollectionCount","CurrentCollectionTime","TotalCollectionCount","TotalCollectionTime"]},{"name":"PS Scavenge","values":["CurrentCollectionCount","CurrentCollectionTime","TotalCollectionCount","TotalCollectionTime"]}]},{"name":"Heap memory","stats":[{"name":"Heap memory","values":["COMMITED","INIT","MAX","MAX_COMMITED","MAX_USED","MIN_COMMITED","MIN_USED","USED"]}]},{"name":"JavaRuntimeFree","stats":[{"name":"JavaRuntimeFree","values":[]}]},{"name":"JavaRuntimeMax","stats":[{"name":"JavaRuntimeMax","values":[]}]},{"name":"JavaRuntimeTotal","stats":[{"name":"JavaRuntimeTotal","values":[]}]},{"name":"LoginAPI","stats":[{"name":"cumulated","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"init","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"isLogedIn","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]}]},{"name":"MemoryPool-Code Cache-NonHeap","stats":[{"name":"MemoryPool-Code Cache-NonHeap","values":["COMMITED","INIT","MAX","MAX_COMMITED","MAX_USED","MIN_COMMITED","MIN_USED","USED"]}]},{"name":"MemoryPool-Compressed Class Space-NonHeap","stats":[{"name":"MemoryPool-Compressed Class Space-NonHeap","values":["COMMITED","INIT","MAX","MAX_COMMITED","MAX_USED","MIN_COMMITED","MIN_USED","USED"]}]},{"name":"MemoryPool-Metaspace-NonHeap","stats":[{"name":"MemoryPool-Metaspace-NonHeap","values":["COMMITED","INIT","MAX","MAX_COMMITED","MAX_USED","MIN_COMMITED","MIN_USED","USED"]}]},{"name":"MemoryPool-PS Eden Space-Heap","stats":[{"name":"MemoryPool-PS Eden Space-Heap","values":["COMMITED","INIT","MAX","MAX_COMMITED","MAX_USED","MIN_COMMITED","MIN_USED","USED"]}]},{"name":"MemoryPool-PS Old Gen-Heap","stats":[{"name":"MemoryPool-PS Old Gen-Heap","values":["COMMITED","INIT","MAX","MAX_COMMITED","MAX_USED","MIN_COMMITED","MIN_USED","USED"]}]},{"name":"MemoryPool-PS Survivor Space-Heap","stats":[{"name":"MemoryPool-PS Survivor Space-Heap","values":["COMMITED","INIT","MAX","MAX_COMMITED","MAX_USED","MIN_COMMITED","MIN_USED","USED"]}]},{"name":"Method","stats":[{"name":"-other-","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"GET","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"OPTIONS","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"POST","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"cumulated","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]}]},{"name":"Non-heap memory","stats":[{"name":"Non-heap memory","values":["COMMITED","INIT","MAX","MAX_COMMITED","MAX_USED","MIN_COMMITED","MIN_USED","USED"]}]},{"name":"OS","stats":[{"name":"OS","values":["CPU TIME","FREE","FREE MB","Map supported Open Files","Max Open Files","Min Open Files","Open Files","ProcessCPULoad","Processors","SystemCPULoad","TOTAL","TOTAL MB","Total CPU TIME"]}]},{"name":"ObservationAPI","stats":[{"name":"cumulated","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"init","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]}]},{"name":"Referer","stats":[{"name":"-other-","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"_this_server_","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"cumulated","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]}]},{"name":"RequestURI","stats":[{"name":"-other-","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"/rest/analyze/chart/sales-number-lamb/update","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"/rest/analyze/charts","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"/rest/analyze/configuration","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"/rest/control","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"/rest/producers/get/all","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"cumulated","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]}]},{"name":"Runtime","stats":[{"name":"Runtime","values":["Process","Starttime","Uptime"]}]},{"name":"SessionCount","stats":[{"name":"Sessions","values":["Cur","Del","Max","Min","New"]}]},{"name":"ThreadCount","stats":[{"name":"ThreadCount","values":["Cur","Daemon","Max","Min","Started"]}]},{"name":"ThreadStates","stats":[{"name":"BLOCKED","values":["CUR","MAX","MIN"]},{"name":"NEW","values":["CUR","MAX","MIN"]},{"name":"RUNNABLE","values":["CUR","MAX","MIN"]},{"name":"TERMINATED","values":["CUR","MAX","MIN"]},{"name":"TIMED_WAITING","values":["CUR","MAX","MIN"]},{"name":"WAITING","values":["CUR","MAX","MIN"]},{"name":"cumulated","values":["CUR","MAX","MIN"]}]},{"name":"UserAgent","stats":[{"name":"-other-","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/60.0.3112.78 Chrome/60.0.3112.78 Safari/537.36","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]},{"name":"cumulated","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]}]},{"name":"maffilter","stats":[{"name":"cumulated","values":["Avg","CR","ERR","Last","MCR","Max","Min","TR","TT"]}]},{"name":"session-refIds-3","stats":[{"name":"session-refIds-3","values":[]}]},{"name":"sessions-2","stats":[{"name":"sessions-2","values":[]}]},{"name":"subjects-1","stats":[{"name":"subjects-1","values":[]}]}];
}
