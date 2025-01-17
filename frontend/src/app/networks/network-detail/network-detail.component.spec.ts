import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkDetailComponent } from './network-detail.component';

describe('NetworkDetailComponent', () => {
  let component: NetworkDetailComponent;
  let fixture: ComponentFixture<NetworkDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NetworkDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NetworkDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
