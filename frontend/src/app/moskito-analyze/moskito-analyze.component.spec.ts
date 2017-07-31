import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { MoskitoAnalyzeComponent } from "./moskito-analyze.component";

describe('MoskitoAnalyzeComponent', () => {
  let component: MoskitoAnalyzeComponent;
  let fixture: ComponentFixture<MoskitoAnalyzeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MoskitoAnalyzeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MoskitoAnalyzeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
