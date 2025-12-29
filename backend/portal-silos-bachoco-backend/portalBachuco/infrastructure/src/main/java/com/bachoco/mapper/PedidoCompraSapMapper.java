package com.bachoco.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bachoco.model.PedidoCompra;
import com.bachoco.model.PedidoCompraSapDTO;
import com.bachoco.model.procedores.PedidoCompraDTO;
import com.bachoco.persistence.entity.PedidoCompraEntity;

public class PedidoCompraSapMapper {

	public static PedidoCompra toDomain(PedidoCompraEntity entity) {
		PedidoCompra pedidos=new PedidoCompra();
		if(entity.getCantidadPedida()!=null) {
			pedidos.setCantidadPedida(entity.getCantidadPedida());
		}
		if(entity.getUrlCerDeposito()!=null) {
			pedidos.setUrlcertificadoConservacion(entity.getUrlCerDeposito());
		}
		if(entity.getCertificadoDeposito()!=null) {
			pedidos.setCertificadoDeposito(entity.getCertificadoDeposito());
		}
		if(entity.getContratoLegal()!=null) {
			pedidos.setContratoLegal(entity.getContratoLegal());
		}
		if(entity.getFechaCompra()!=null) {
			pedidos.setFechaCompra(entity.getFechaCompra());
		}
		if(entity.getNumeroPedido()!=null) {
			pedidos.setNumeroPedido(entity.getNumeroPedido());
		}
		if(entity.getPosicion()!=null) {
			pedidos.setPosicion(entity.getPosicion());
		}
		if(entity.getSilo()!=null) {
			pedidos.setSilo(SiloMapper.toDomain(entity.getSilo()));
		}
		if(entity.getMaterial()!=null) {
			pedidos.setMaterial(MaterialMapper.toDomain(entity.getMaterial()));
		}
		if(entity.getDetallePedidoCompra()!=null) {
			pedidos.setDetallePedidoCompra(DetallePedidoCompraMapper.toDomain(entity.getDetallePedidoCompra()));
		}
		return pedidos;
	}
	
	public static PedidoCompraEntity toEntity(PedidoCompra pedido) {
		PedidoCompraEntity entity= new PedidoCompraEntity();
		if(pedido.getCantidadPedida()!=null) {
			entity.setCantidadPedida(pedido.getCantidadPedida());
		}
		if(pedido.getUrlcertificadoConservacion()!=null) {
			entity.setUrlCerDeposito(pedido.getUrlcertificadoConservacion());
		}
		if(pedido.getCertificadoDeposito()!=null) {
			entity.setCertificadoDeposito(pedido.getCertificadoDeposito());
		}
		if(pedido.getContratoLegal()!=null) {
			entity.setContratoLegal(pedido.getContratoLegal());
		}
		if(pedido.getFechaCompra()!=null) {
			pedido.setFechaCompra(pedido.getFechaCompra());
		}
		if(pedido.getNumeroPedido()!=null) {
			pedido.setNumeroPedido(pedido.getNumeroPedido());
		}
		if(pedido.getPosicion()!=null) {
			entity.setPosicion(pedido.getPosicion());
		}
		if(pedido.getSilo()!=null) {
			entity.setSilo(SiloMapper.toEntity(pedido.getSilo()));
		}
		if(pedido.getMaterial()!=null) {
			entity.setMaterial(MaterialMapper.toEntity(pedido.getMaterial()));
		}
		if(pedido.getDetallePedidoCompra()!=null) {
			entity.setDetallePedidoCompra(DetallePedidoCompraMapper.toEntity(pedido.getDetallePedidoCompra()));
		}
		return entity;
	}
	
	public static List<PedidoCompra> toDomain(List<PedidoCompraEntity> entities){
		return entities.stream().map(PedidoCompraSapMapper::toDomain).collect(Collectors.toList());
	}
	
	public static List<PedidoCompraDTO> sapToDomain(List<PedidoCompraSapDTO> sap){
		List<PedidoCompraDTO> pedidos= new ArrayList<>();
		for(PedidoCompraSapDTO s:sap) {
			PedidoCompraDTO pe=new PedidoCompraDTO();
			if(s.getPedCompra()!=null) {
				pe.setNumeroPedido(s.getPedCompra());
			}
			if(s.getCantidadDespacho()!=null) {
				pe.setCantidadDespachada(Float.parseFloat(s.getCantidadDespacho()));
			}
			if(s.getCantidadEntrega()!=null) {
				pe.setCantidadEntregada(Float.parseFloat(s.getCantidadEntrega()));
			}
			if(s.getCantidadPedido()!=null) {
				pe.setCantidadPedida(Float.parseFloat(s.getCantidadPedido()));
			}
			if(s.getContratoLegal()!=null) {
				pe.setContratoLegal(s.getContratoLegal());
			}
			pedidos.add(pe);
		}
		return pedidos;
	}
	
	
}
