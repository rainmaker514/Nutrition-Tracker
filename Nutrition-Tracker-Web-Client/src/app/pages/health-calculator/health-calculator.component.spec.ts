import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HealthCalculatorComponent } from './health-calculator.component';

describe('HealthCalculatorComponent', () => {
  let component: HealthCalculatorComponent;
  let fixture: ComponentFixture<HealthCalculatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HealthCalculatorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HealthCalculatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
