import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistroSiloComponent } from './registro-silo.component';

describe('RegistroSiloComponent', () => {
  let component: RegistroSiloComponent;
  let fixture: ComponentFixture<RegistroSiloComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroSiloComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegistroSiloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
