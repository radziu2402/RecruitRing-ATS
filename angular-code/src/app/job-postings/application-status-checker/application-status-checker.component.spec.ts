import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationStatusCheckerComponent } from './application-status-checker.component';

describe('ApplicationStatusCheckerComponent', () => {
  let component: ApplicationStatusCheckerComponent;
  let fixture: ComponentFixture<ApplicationStatusCheckerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationStatusCheckerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApplicationStatusCheckerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
