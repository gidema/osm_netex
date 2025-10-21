import { ComponentFixture, TestBed } from '@angular/core/testing';

import NetexLineComponent from '@lines/netex-line.component';

describe('NetexLineComponent', () => {
  let component: NetexLineComponent;
  let fixture: ComponentFixture<NetexLineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NetexLineComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NetexLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
