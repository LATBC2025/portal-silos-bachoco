import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportePedidoTrasladoComponent } from './reporte-pedido-traslado.component';

describe('ReportePedidoTrasladoComponent', () => {
  let component: ReportePedidoTrasladoComponent;
  let fixture: ComponentFixture<ReportePedidoTrasladoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportePedidoTrasladoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportePedidoTrasladoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
