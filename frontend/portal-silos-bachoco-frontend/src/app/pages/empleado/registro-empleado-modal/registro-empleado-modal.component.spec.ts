import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistroEmpleadoModalComponent } from './registro-empleado-modal.component';

describe('RegistroEmpleadoModalComponent', () => {
  let component: RegistroEmpleadoModalComponent;
  let fixture: ComponentFixture<RegistroEmpleadoModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroEmpleadoModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegistroEmpleadoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
