import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlantaRegistroComponent } from './planta-registro.component';

describe('PlantaRegistroComponent', () => {
  let component: PlantaRegistroComponent;
  let fixture: ComponentFixture<PlantaRegistroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlantaRegistroComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PlantaRegistroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
