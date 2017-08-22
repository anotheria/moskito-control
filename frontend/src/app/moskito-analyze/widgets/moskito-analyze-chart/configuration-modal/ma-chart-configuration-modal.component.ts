import { MoskitoAnalyzeChart } from "./../../../model/moskito-analyze-chart.model";
import { MoskitoAnalyzeRestService } from "./../../../services/moskito-analyze-rest.service";
import { EventEmitter, Component, Input, OnInit, Output } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { MoskitoAnalyzeService } from "../../../services/moskito-analyze.service";
import { IMultiSelectOption, IMultiSelectSettings } from "angular-2-dropdown-multiselect";


@Component({
  selector: 'ma-configuration-modal',
  templateUrl: './ma-chart-configuration-modal.component.html',
  styleUrls: ['./ma-chart-configuration-modal.component.css']
})
export class MoskitoAnalyzeChartConfigurationModalComponent implements OnInit {

  @Input()
  chart: MoskitoAnalyzeChart;

  /**
   * Determines behavior / purpose of modal window: add or update chart.
   */
  @Input()
  action: string;

  @Output()
  onChartConfigurationUpdate = new EventEmitter<MoskitoAnalyzeChart>();

  @Output()
  onChartConfigurationCreate = new EventEmitter<MoskitoAnalyzeChart>();

  chartForm: FormGroup;

  producerNames: string[];

  selectedHosts: number[];

  availableHosts: IMultiSelectOption[];

  hostsSettings: IMultiSelectSettings;


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

    this.setHostIds(this.chart.hosts);
    this.getProducerNames();
    this.buildProducerForm();
  }

  /**
   * Replaces chart configuration object in service stored producers or adds the new one.
   */
  saveChartConfiguration() {
    const chart = new MoskitoAnalyzeChart();

    chart.id = this.chart.id;
    chart.name = this.chart.name;
    chart.type = this.chartForm.value.type;
    chart.interval = this.chartForm.value.interval;
    chart.hosts = this.resolveHostsByIds(this.selectedHosts);
    chart.caption = this.chartForm.value.caption;
    chart.producer = this.chartForm.value.producer;
    chart.stat = this.chartForm.value.stat;
    chart.value = this.chartForm.value.value;

    // Fire chart updated or created action
    if (this.action === 'update')
      this.onChartConfigurationUpdate.emit(chart);
    else if (this.action === 'add') {
      this.onChartConfigurationCreate.emit(chart);
    }

    this.activeModal.close();
  }

  private resolveHostsByIds(ids: number[]): string[] {
    let hosts: string[] = [];

    for (let id of ids) {
      hosts.push(this.availableHosts[id - 1].name);
    }

    return hosts;
  }

  private setHostIds(hosts: string[]) {
    this.selectedHosts = [];

    for (let hostName of hosts) {
      for (let hostItem of this.availableHosts) {
        if (hostItem.name === hostName) {
          this.selectedHosts.push(hostItem.id);
        }
      }
    }
  }

  private getProducerNames() {
    this.rest.getProducerNames().subscribe((producers) => {
      this.producerNames = producers;
    });
  }

  private buildProducerForm() {
    this.chartForm = this.fb.group({
      type: [ this.chart.type, [ Validators.required ] ],
      interval: [ this.chart.interval, [ Validators.required ] ],
      caption: [ this.chart.caption, [ Validators.required ] ],
      producer: [ this.chart.producer, [ Validators.required ] ],
      stat: [ this.chart.stat, [ Validators.required ] ],
      value: [ this.chart.value, [ Validators.required ] ]
    });
  }
}
