import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteArriboComponent } from './reporte-arribo.component';

describe('ReporteArriboComponent', () => {
  let component: ReporteArriboComponent;
  let fixture: ComponentFixture<ReporteArriboComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteArriboComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReporteArriboComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
