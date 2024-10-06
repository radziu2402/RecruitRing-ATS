import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicJobDetailComponent } from './public-job-detail.component';

describe('PublicJobDetailComponent', () => {
  let component: PublicJobDetailComponent;
  let fixture: ComponentFixture<PublicJobDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicJobDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicJobDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
