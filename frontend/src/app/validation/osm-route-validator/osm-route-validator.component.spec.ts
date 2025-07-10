import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OsmRouteValidatorComponent } from './osm-route-validator.component';

describe('OsmRouteValidatorComponent', () => {
  let component: OsmRouteValidatorComponent;
  let fixture: ComponentFixture<OsmRouteValidatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OsmRouteValidatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OsmRouteValidatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
