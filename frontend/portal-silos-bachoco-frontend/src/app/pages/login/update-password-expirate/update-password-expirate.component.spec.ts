import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePasswordExpirateComponent } from './update-password-expirate.component';

describe('UpdatePasswordExpirateComponent', () => {
  let component: UpdatePasswordExpirateComponent;
  let fixture: ComponentFixture<UpdatePasswordExpirateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdatePasswordExpirateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdatePasswordExpirateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
