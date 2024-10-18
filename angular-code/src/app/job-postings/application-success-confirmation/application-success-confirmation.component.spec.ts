import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationSuccessConfirmationComponent } from './application-success-confirmation.component';

describe('ApplicationSuccessConfirmationComponent', () => {
  let component: ApplicationSuccessConfirmationComponent;
  let fixture: ComponentFixture<ApplicationSuccessConfirmationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationSuccessConfirmationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApplicationSuccessConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
