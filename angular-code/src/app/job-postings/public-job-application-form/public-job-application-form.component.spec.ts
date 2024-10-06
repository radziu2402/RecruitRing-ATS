import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicJobApplicationFormComponent } from './public-job-application-form.component';

describe('PublicJobApplicationFormComponent', () => {
  let component: PublicJobApplicationFormComponent;
  let fixture: ComponentFixture<PublicJobApplicationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicJobApplicationFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicJobApplicationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
