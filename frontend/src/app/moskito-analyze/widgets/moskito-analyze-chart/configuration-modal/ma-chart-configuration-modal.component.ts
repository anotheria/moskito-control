import { EventEmitter, Component, Input, OnInit, Output } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { IMultiSelectOption, IMultiSelectSettings } from "angular-2-dropdown-multiselect";
import { UUID } from "angular2-uuid";
import { MoskitoAnalyzeService } from "app/moskito-analyze/services/moskito-analyze.service";
import { MoskitoAnalyzeRestService } from "app/moskito-analyze/services/moskito-analyze-rest.service";
import { MoskitoAnalyzeChart } from "app/moskito-analyze/model/moskito-analyze-chart.model";
import { Producer } from "../../../model/producer.model";
import { Stat } from "../../../model/stat.model";


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
   * List of selected components.
   * Used for custom multi select component.
   */
  selectedComponents: number[] = [];

  /**
   * List of possible components that can be selected.
   * Used for custom multi select component.
   */
  availableComponents: IMultiSelectOption[];

  /**
   * Multi Select component settings.
   */
  componentsSettings: IMultiSelectSettings;

  /**
   * Producer tree, received from MoSKito-Analyze.
   */
  producerData: Producer[];

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
    this.availableComponents = [
      { id: 1, name: 'munich' },
      { id: 2, name: 'bedcon' }
    ];

    this.componentsSettings = {
      checkedStyle: 'fontawesome',
      buttonClasses: 'custom-dropdown-block',
      containerClasses: 'dropdown-block'
    };

    this.buildChartForm();

    this.rest.getProducers().subscribe((producers: Producer[]) => {
      this.producerData = producers;

      this.producers = this.getProducerNames(this.producerData);
      this.stats = this.getStatNames(this.chart ? this.chart.producer : '', this.producerData);
      this.values = this.getValueNames(this.chart ? this.chart.producer : '', this.chart ? this.chart.stat : '', this.producerData);
    });

    if (this.chart) {
      this.selectedComponents = this.getHostIdsByNames(this.chart.components);
    }

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
    chart.components = this.resolveComponentsByIds(this.selectedComponents);
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
  private getProducerNames(data: Producer[]) {
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
  private getStatNames(producerName: string, data: Producer[]) {
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
  private getValueNames(producerName: string, statName: string, data: Producer[]) {
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
  private getProducerByName(name: string, producers: Producer[]) {
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
  private getStatByName(name: string, stats: Stat[]) {
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
   * Returns list of component names by their indexes from array.
   * @param ids
   * @returns {string[]}
   */
  private resolveComponentsByIds(ids: number[]): string[] {
    let components: string[] = [];

    for (let id of ids) {
      components.push(this.availableComponents[id - 1].name);
    }

    return components;
  }

  /**
   * Returns components indexes in array by given host names.
   * @param components
   */
  private getHostIdsByNames(components: string[]) {
    let componentNames = [];

    for (let componentName of components) {
      for (let componentItem of this.availableComponents) {
        if (componentItem.name === componentName) {
          componentNames.push(componentItem.id);
        }
      }
    }

    return componentNames;
  }

  /**
   * Initializes chart form parameters.
   */
  private buildChartForm() {
    this.chartForm = this.fb.group({
      type: [ this.chart.type, [ Validators.required ] ],
      interval: [ this.chart.interval, [ Validators.required ] ],

      startDate: [
        this.chart.startDate ?
          this.moskitoAnalyze.formatDate(this.chart.startDate) :
          this.moskitoAnalyze.formatDate(this.moskitoAnalyze.getStartDate()),
        [ Validators.required ]
      ],

      endDate: [
        this.chart.endDate ?
          this.moskitoAnalyze.formatDate(this.chart.endDate) :
          this.moskitoAnalyze.formatDate(this.moskitoAnalyze.getEndDate()),
        [ Validators.required ]
      ],

      caption: [ this.chart.caption, [ Validators.required ] ],
      producer: [ this.chart.producer, [ Validators.required ] ],
      stat: [ this.chart.stat, [ Validators.required ] ],
      value: [ this.chart.value, [ Validators.required ] ]
    });
  }
}
