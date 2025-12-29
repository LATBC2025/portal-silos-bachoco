import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmacionDespachoComponent } from './confirmacion-despacho.component';

describe('ConfirmacionDespachoComponent', () => {
  let component: ConfirmacionDespachoComponent;
  let fixture: ComponentFixture<ConfirmacionDespachoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmacionDespachoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConfirmacionDespachoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
