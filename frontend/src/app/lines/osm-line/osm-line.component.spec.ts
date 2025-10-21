import { ComponentFixture, TestBed } from '@angular/core/testing';

import OsmLineDetailComponent from '@lines/osm-line-detail.component';

describe('OsmLineDetailComponent', () => {
  let component: OsmLineDetailComponent;
  let fixture: ComponentFixture<OsmLineDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OsmLineDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OsmLineDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
