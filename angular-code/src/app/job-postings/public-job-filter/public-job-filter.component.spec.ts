import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicJobFilterComponent } from './public-job-filter.component';

describe('PublicJobFilterComponent', () => {
  let component: PublicJobFilterComponent;
  let fixture: ComponentFixture<PublicJobFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicJobFilterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicJobFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
