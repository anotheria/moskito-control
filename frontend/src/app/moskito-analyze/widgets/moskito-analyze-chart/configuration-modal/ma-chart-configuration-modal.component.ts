import { EventEmitter, Component, Input, OnInit, Output } from "@angular/core";
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { IMultiSelectOption, IMultiSelectSettings } from "angular-2-dropdown-multiselect";
import { UUID } from "angular2-uuid";
import { MoskitoAnalyzeService } from "app/moskito-analyze/services/moskito-analyze.service";
import { MoskitoAnalyzeRestService } from "app/moskito-analyze/services/moskito-analyze-rest.service";
import { MoskitoAnalyzeChart } from "app/moskito-analyze/model/moskito-analyze-chart.model";
import { Producer } from "../../../model/producer.model";
import { Stat } from "../../../model/stat.model";
import { MoskitoAnalyzeChartLine } from "../../../model/moskito-analyze-chart-line.model";


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

  chartLines: FormArray;

  /**
   * List of selected components.
   * Used for custom multi select component.
   */
  selectedComponents: any[] = [];

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
  producers: any[] = [];

  /**
   * List of stat names that can be selected for given producer
   * @type {Array}
   */
  stats: any[] = [];

  /**
   * List of value names that can be selected for given stat
   * @type {Array}
   */
  values: any[] = [];


  constructor(
    public activeModal: NgbActiveModal,
    private moskitoAnalyze: MoskitoAnalyzeService,
    private rest: MoskitoAnalyzeRestService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.availableComponents = [];
    this.moskitoAnalyze.components.forEach((component: string, index: number) => {
      this.availableComponents.push({ id: index + 1, name: component });
    });

    this.componentsSettings = {
      checkedStyle: 'fontawesome',
      buttonClasses: 'custom-dropdown-block',
      containerClasses: 'dropdown-block'
    };

    this.buildChartForm();

    this.rest.getProducers().subscribe((producers: Producer[]) => {
      this.producerData = producers;

      if (this.chart) {
        this.chart.lines.forEach(line => {
          this.producers.push(this.getProducerNames(this.producerData));
          this.stats.push(this.getStatNames(line.producer, this.producerData));
          this.values.push(this.getValueNames(line.producer, line.stat, this.producerData));
        });
      }
    });

    if (this.chart) {
      this.chart.lines.forEach(line => {
        this.selectedComponents.push(this.getComponentIdsByNames(line.components));
      });
    }
  }

  /**
   * Replaces chart configuration object in service stored producers or adds the new one.
   */
  saveChartConfiguration() {
    const chart = new MoskitoAnalyzeChart();

    chart.id = this.chart.id;
    chart.name = this.chart.name ? this.chart.name : this.generateChartName();
    chart.caption = this.chartForm.value.caption;
    chart.lines = [];

    let linesFormArr = this.chartForm.controls.lines as FormArray;
    linesFormArr.controls.forEach((lineForm, index) => {
      let line = new MoskitoAnalyzeChartLine();

      line.name = lineForm.value.name;
      line.producer = lineForm.value.producer;
      line.stat = lineForm.value.stat;
      line.value = lineForm.value.value;
      line.components = this.resolveComponentsByIds(this.selectedComponents[index]);
      line.average = lineForm.value.average;
      line.baseline = lineForm.value.baseline;

      chart.lines.push(line);
    });

    chart.interval = this.chartForm.value.interval;
    chart.startDate = new Date(this.chartForm.value.startDate);
    chart.endDate = new Date(this.chartForm.value.endDate);

    // Fire chart updated or created action
    if (this.action === 'update')
      this.onChartConfigurationUpdate.emit(chart);
    else if (this.action === 'create') {
      this.onChartConfigurationCreate.emit(chart);
    }

    this.activeModal.close();
  }

  addChartLine() {
    this.chartLines = this.chartForm.get('lines') as FormArray;
    this.chartLines.push(this.createChartLine());
  }

  removeChartLine( index ) {
    this.chartLines = this.chartForm.get('lines') as FormArray;
    this.chartLines.removeAt( index );
  }

  createChartLine() {
    return this.fb.group({
      name: '',
      producer: '',
      stat: '',
      value: '',
      average: false,
      baseline: false
    });
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
  producerNameChange(event, producerName, index) {
    this.stats[index] = this.getStatNames(producerName, this.producerData);
    this.values[index] = this.getValueNames(producerName, '', this.producerData);
  }

  /**
   * When user selects another stat, list of possible
   * value names is refreshed.
   */
  statNameChange(event, producerName, statName, index) {
    this.values[index] = this.getValueNames(producerName, statName, this.producerData)
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
  private getComponentIdsByNames(components: string[]) {
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

    let linesConfig = [];
    this.chart.lines.forEach(line => {
      linesConfig.push(this.fb.group({
        name: line.name,
        producer: line.producer,
        stat: line.stat,
        value: line.value,
        average: line.average,
        baseline: line.baseline
      }));
    });

    this.chartForm = this.fb.group({
      caption: [ this.chart.caption, [ Validators.required ] ],
      interval: [ this.chart.interval, [ Validators.required ] ],

      lines: this.fb.array(linesConfig.length > 0 ? linesConfig : [this.createChartLine()]),

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
      ]
    });
  }
}
