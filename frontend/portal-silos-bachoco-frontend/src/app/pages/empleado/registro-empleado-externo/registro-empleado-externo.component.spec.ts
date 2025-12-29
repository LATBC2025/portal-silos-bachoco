import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistroEmpleadoExternoComponent } from './registro-empleado-externo.component';

describe('RegistroEmpleadoExternoComponent', () => {
  let component: RegistroEmpleadoExternoComponent;
  let fixture: ComponentFixture<RegistroEmpleadoExternoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroEmpleadoExternoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegistroEmpleadoExternoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
