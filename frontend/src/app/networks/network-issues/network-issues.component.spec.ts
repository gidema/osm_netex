import { ComponentFixture, TestBed } from '@angular/core/testing';

import NetworkIssuesComponent from '@networks/network-issues.component';

describe('NetworkIssuesComponent', () => {
  let component: NetworkDetailComponent;
  let fixture: ComponentFixture<NetworkDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NetworkIssuesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NetworkIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
