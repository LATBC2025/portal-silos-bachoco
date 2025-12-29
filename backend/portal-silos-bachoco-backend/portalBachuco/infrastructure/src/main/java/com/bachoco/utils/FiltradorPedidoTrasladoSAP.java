package com.bachoco.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bachoco.model.PedidoTrasladoSapResponseDTO;

public class FiltradorPedidoTrasladoSAP {

	private List<PedidoTrasladoSapResponseDTO> pedidosOriginal;
    private Set<String> plantasPermitidas;
    private Set<String> materialesPermitidas;
    private Set<String> foliosPedidoComprasInvalidos;
    private boolean removerNullsFolioPedidoCompra = true;
    
    //PATRÓN BUILDER
    public static class Builder {
        private List<PedidoTrasladoSapResponseDTO> pedidosOriginal;
        private Set<String> plantasPermitidas;
        private Set<String> materialesPermitidas;
        private Set<String> foliosPedidoComprasInvalidos;
        private boolean removerNullsFolioPedidoCompra = true;
        
        public Builder(List<PedidoTrasladoSapResponseDTO> pedidosOriginal) {
            this.pedidosOriginal = new ArrayList<>(pedidosOriginal); // Copia defensiva
        }
        
        public Builder conPlantasPermitidas(Set<String> plantasPermitidas) {
            this.plantasPermitidas = plantasPermitidas;
            return this;
        }
        
        public Builder conPlantasPermitidas(List<String> plantasPermitidas) {
            this.plantasPermitidas = new HashSet<>(plantasPermitidas);
            return this;
        }
        
        public Builder conMaterialesPermitidas(List<String> materialesPermitidas) {
            this.materialesPermitidas = new HashSet<>(materialesPermitidas);
            return this;
        }
        
        public Builder conFoliosPedidoComprasInvalidos(Set<String> foliosInvalidos) {
            this.foliosPedidoComprasInvalidos = foliosInvalidos;
            return this;
        }
        
        public Builder conFoliosPedidoComprasInvalidos(List<String> foliosInvalidos) {
            this.foliosPedidoComprasInvalidos = new HashSet<>(foliosInvalidos);
            return this;
        }
        
        public Builder removerNullsFolioPedidoCompra(boolean remover) {
            this.removerNullsFolioPedidoCompra = remover;
            return this;
        }
        
    }
    
    //Factory method
    public static Builder from(List<PedidoTrasladoSapResponseDTO> pedidosOriginal) {
        return new Builder(pedidosOriginal);
    }
    
    // ✅ MÉTODO PRINCIPAL QUE APLICA TODOS LOS FILTROS
    public ResultadoFiltrado filtrar() {
        if (pedidosOriginal == null || pedidosOriginal.isEmpty()) {
            return new ResultadoFiltrado(List.of(), 0, 0, 0, List.of());
        }
        
        List<PedidoTrasladoSapResponseDTO> resultado = new ArrayList<>(pedidosOriginal);
        List<String> plantasFiltradas = new ArrayList<>();
        
        //FILTRO 1: Plantas destino válidas
        if (!plantasPermitidas.isEmpty()) {
            int antes = resultado.size();
            resultado.removeIf(pedido -> !plantasPermitidas.contains(pedido.getPlantaDestino()));
            plantasFiltradas.add(String.format("Plantas: %d eliminados", antes - resultado.size()));
        }
        
        //FILTRO 2: Remover nulls en folio pedido compra y posición
        if (removerNullsFolioPedidoCompra) {
            int antes = resultado.size();
            resultado.removeIf(pedido -> 
                pedido.getPedidoDeComprasAsociado() == null ||
                pedido.getPosicion() == null || 
                pedido.getPosicion().trim().isEmpty()
            );
            plantasFiltradas.add(String.format("Nulls folio/posición: %d eliminados", antes - resultado.size()));
        }
        
        return new ResultadoFiltrado(
            Collections.unmodifiableList(resultado),
            pedidosOriginal.size(),
            resultado.size(),
            pedidosOriginal.size() - resultado.size(),
            Collections.unmodifiableList(plantasFiltradas)
        );
    }
    
    // ✅ CLASE DE RESULTADO MEJORADA
    public static class ResultadoFiltrado {
        public final List<PedidoTrasladoSapResponseDTO> pedidosFiltrados;
        public final int totalOriginal;
        public final int totalFiltrados;
        public final int totalEliminados;
        public final List<String> detallesFiltrado;
        
        public ResultadoFiltrado(List<PedidoTrasladoSapResponseDTO> pedidosFiltrados,
                               int totalOriginal,
                               int totalFiltrados,
                               int totalEliminados,
                               List<String> detallesFiltrado) {
            this.pedidosFiltrados = pedidosFiltrados;
            this.totalOriginal = totalOriginal;
            this.totalFiltrados = totalFiltrados;
            this.totalEliminados = totalEliminados;
            this.detallesFiltrado = detallesFiltrado;
        }
        
        public double getPorcentajeRetenido() {
            return totalOriginal > 0 ? (double) totalFiltrados / totalOriginal * 100 : 0;
        }
        
        public void imprimirEstadisticas() {
            System.out.println("=== ESTADÍSTICAS DE FILTRADO ===");
            System.out.printf("Total original: %d%n", totalOriginal);
            System.out.printf("Total filtrados: %d%n", totalFiltrados);
            System.out.printf("Total eliminados: %d%n", totalEliminados);
            System.out.printf("Porcentaje retenido: %.2f%%%n", getPorcentajeRetenido());
            
            if (!detallesFiltrado.isEmpty()) {
                System.out.println("Detalles por filtro:");
                detallesFiltrado.forEach(System.out::println);
            }
        }
    }
}
