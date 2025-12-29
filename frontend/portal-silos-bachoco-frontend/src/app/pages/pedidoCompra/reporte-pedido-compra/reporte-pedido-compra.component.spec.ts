import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportePedidoCompraComponent } from './reporte-pedido-compra.component';

describe('ReportePedidoCompraComponent', () => {
  let component: ReportePedidoCompraComponent;
  let fixture: ComponentFixture<ReportePedidoCompraComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportePedidoCompraComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportePedidoCompraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
