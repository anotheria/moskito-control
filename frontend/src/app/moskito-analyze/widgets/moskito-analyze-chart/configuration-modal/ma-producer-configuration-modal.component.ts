import { MoskitoAnalyzeRestService } from "./../../../services/moskito-analyze-rest.service";
import { MoskitoAnalyzeProducer } from "./../../../model/moskito-analyze-producer.model";
import { EventEmitter, Component, Input, OnInit, Output } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { MoskitoAnalyzeService } from "../../../services/moskito-analyze.service";

@Component({
  selector: 'ma-configuration-modal',
  templateUrl: './ma-producer-configuration-modal.component.html',
  styleUrls: ['./ma-producer-configuration-modal.component.css']
})
export class MoskitoAnalyzeProducerConfigurationModalComponent implements OnInit {

  @Input()
  producer: MoskitoAnalyzeProducer;

  /**
   * Determines behavior / purpose of modal window: add or update producer.
   */
  @Input()
  action: string;

  @Output()
  onProducerConfigurationUpdate = new EventEmitter<MoskitoAnalyzeProducer>();

  @Output()
  onProducerConfigurationCreate = new EventEmitter<MoskitoAnalyzeProducer>();

  producerForm: FormGroup;

  producerNames: string[];


  constructor(
    public activeModal: NgbActiveModal,
    private moskitoAnalyze: MoskitoAnalyzeService,
    private rest: MoskitoAnalyzeRestService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.getProducerNames();
    this.buildProducerForm();
  }

  /**
   * Replaces producer configuration object in service stored producers or adds the new one.
   */
  saveProducerConfiguration() {
    const producer = new MoskitoAnalyzeProducer();

    producer.id = this.producer.id;
    producer.caption = this.producerForm.value.caption;
    producer.producer = this.producerForm.value.producer;
    producer.stat = this.producerForm.value.stat;
    producer.value = this.producerForm.value.value;

    // Fire producer updated or created action
    if (this.action === 'update')
      this.onProducerConfigurationUpdate.emit(producer);
    else if (this.action === 'add') {
      this.onProducerConfigurationCreate.emit(producer);
    }

    this.activeModal.close();
  }

  private getProducerNames() {
    this.rest.getProducerNames().subscribe((producers) => {
      this.producerNames = producers;
    });
  }

  private buildProducerForm() {
    this.producerForm = this.fb.group({
      caption: [ this.producer.caption, [ Validators.required ] ],
      producer: [ this.producer.producer, [ Validators.required ] ],
      stat: [ this.producer.stat, [ Validators.required ] ],
      value: [ this.producer.value, [ Validators.required ] ]
    });
  }
}
