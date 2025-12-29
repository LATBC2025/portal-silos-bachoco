import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistroEmpleadoExternoModalComponent } from './registro-empleado-externo-modal.component';

describe('RegistroEmpleadoExternoModalComponent', () => {
  let component: RegistroEmpleadoExternoModalComponent;
  let fixture: ComponentFixture<RegistroEmpleadoExternoModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroEmpleadoExternoModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegistroEmpleadoExternoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
