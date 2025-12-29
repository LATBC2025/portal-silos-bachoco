import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePasswordOlvidadoComponent } from './update-password-olvidado.component';

describe('UpdatePasswordOlvidadoComponent', () => {
  let component: UpdatePasswordOlvidadoComponent;
  let fixture: ComponentFixture<UpdatePasswordOlvidadoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdatePasswordOlvidadoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdatePasswordOlvidadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
