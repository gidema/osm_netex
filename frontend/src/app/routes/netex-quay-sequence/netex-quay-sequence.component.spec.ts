import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NetexQuaySequenceComponent } from './netex-quay-sequence.component';

describe('NetexQuaySequenceComponent', () => {
  let component: NetexQuaySequenceComponent;
  let fixture: ComponentFixture<NetexQuaySequenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NetexQuaySequenceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NetexQuaySequenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
