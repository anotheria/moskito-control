import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { MoskitoAnalyzeChartComponent } from "./moskito-analyze-chart.component";

describe('MoskitoAnalyzeChartComponent', () => {
  let component: MoskitoAnalyzeChartComponent;
  let fixture: ComponentFixture<MoskitoAnalyzeChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MoskitoAnalyzeChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MoskitoAnalyzeChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
