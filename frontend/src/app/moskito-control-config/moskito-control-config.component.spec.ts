import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { MoskitoControlConfigComponent } from "./moskito-control-config.component";

describe('MoskitoControlConfigComponent', () => {
  let component: MoskitoControlConfigComponent;
  let fixture: ComponentFixture<MoskitoControlConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MoskitoControlConfigComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MoskitoControlConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
