import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateJobComponent } from './create-update-job.component';

describe('CreateJobComponent', () => {
  let component: CreateUpdateJobComponent;
  let fixture: ComponentFixture<CreateUpdateJobComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateJobComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateJobComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
