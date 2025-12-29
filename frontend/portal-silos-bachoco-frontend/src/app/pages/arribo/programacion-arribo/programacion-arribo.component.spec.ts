import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramacionArriboComponent } from './programacion-arribo.component';

describe('ProgramacionArriboComponent', () => {
  let component: ProgramacionArriboComponent;
  let fixture: ComponentFixture<ProgramacionArriboComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgramacionArriboComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProgramacionArriboComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
