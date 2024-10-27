import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NetexLineDetailComponent } from './netex-line-detail.component';

describe('NetexLineDetailComponent', () => {
  let component: NetexLineDetailComponent;
  let fixture: ComponentFixture<NetexLineDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NetexLineDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NetexLineDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
