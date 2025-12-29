package com.bachoco.utils;

import java.util.List;
import java.util.function.Predicate;


import com.bachoco.model.PedidoTrasladoSapResponseDTO;

public class PedidoTrasladoUtil {

	public static final Predicate<PedidoTrasladoSapResponseDTO> FILTRO_PED_COMPRA_NO_ASOCIADO = pedido -> pedido.getPedidoDeComprasAsociado() == null || pedido.getPosicion() == null;
	
	public static final Predicate<PedidoTrasladoSapResponseDTO> FILTRO_PED_NO_COINCIDEN_MATERIALES = pedido -> pedido.getPedidoDeComprasAsociado() == null || pedido.getPosicion() == null;

    public static List<PedidoTrasladoSapResponseDTO> cleanPedidoTrasladoPedCompraAndPosicionIsNull(
            List<PedidoTrasladoSapResponseDTO> pedidosTrasladoSap) {
        return CollectionUtils.filtrarLista(pedidosTrasladoSap,FILTRO_PED_COMPRA_NO_ASOCIADO);
    }

}
