import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDobleFactorAuthComponent } from './confirm-doble-factor-auth.component';

describe('ConfirmDobleFactorAuthComponent', () => {
  let component: ConfirmDobleFactorAuthComponent;
  let fixture: ComponentFixture<ConfirmDobleFactorAuthComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmDobleFactorAuthComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmDobleFactorAuthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
