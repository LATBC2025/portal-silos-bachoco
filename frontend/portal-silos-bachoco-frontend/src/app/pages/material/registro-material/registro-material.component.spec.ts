import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistroMaterialComponent } from './registro-material.component';

describe('RegistroMaterialComponent', () => {
  let component: RegistroMaterialComponent;
  let fixture: ComponentFixture<RegistroMaterialComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroMaterialComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegistroMaterialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
