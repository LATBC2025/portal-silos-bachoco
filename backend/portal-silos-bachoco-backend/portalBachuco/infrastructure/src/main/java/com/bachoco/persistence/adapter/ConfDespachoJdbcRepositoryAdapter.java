package com.bachoco.persistence.adapter;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bachoco.dto.sap.confDespacho.SapResponse;
import com.bachoco.model.ConfDespachoPesosRequest;
import com.bachoco.model.ConfirmDespachoResponse;
import com.bachoco.model.ConfirmacionDespachoRequest;
import com.bachoco.model.ConfirmacionDespachoResponse;
import com.bachoco.persistence.repository.jdbc.CatalogJdbcRepository;
import com.bachoco.persistence.repository.jdbc.ConfDespachoJdbcRepository;
import com.bachoco.persistence.repository.jdbc.PedidoTrasladoJdbcRepository;
import com.bachoco.port.ConfirmacionDespachoJdbcRepository;
import com.bachoco.port.ProgramArriboRepositoryPort;
import com.bachoco.utils.ConfirmacionDespachoUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConfDespachoJdbcRepositoryAdapter implements ConfirmacionDespachoJdbcRepository {

	private final ConfirmacionDespachoSapClientAdapter confDespachoSapClientAdapter;
	private final ConfDespachoJdbcRepository confDespachoJdbcRepository;
	private final PedidoTrasladoJdbcRepository pedidoTrasladoJdbcRepository;
	private final CatalogJdbcRepository catalogJdbcRepository;
	private final ProgramArriboRepositoryPort programArriboRepositoryPort;
	private final String ESTATUS_EXITOSO_SAP="S";
	private final String KEY_ID="id";
	private final String KEY_FOLIO="folio";
	private final String OPERACION_352="352";
	private final String ESTATUS_CODE_FAIL_SAVE_BD="-2";
	private final String ESTATUS_CODE_FAIL_SAP="-1";
	private final String EMPTY="";
	private static final Logger logger = LoggerFactory.getLogger(ConfDespachoJdbcRepositoryAdapter.class);

	public ConfDespachoJdbcRepositoryAdapter(ConfirmacionDespachoSapClientAdapter confDespachoSapClientAdapter,
			ConfDespachoJdbcRepository confDespachoJdbcRepository,
			PedidoTrasladoJdbcRepository pedidoTrasladoJdbcRepository, CatalogJdbcRepository catalogJdbcRepository,
			ProgramArriboRepositoryPort programArriboRepositoryPor) {
		this.confDespachoSapClientAdapter = confDespachoSapClientAdapter;
		this.confDespachoJdbcRepository = confDespachoJdbcRepository;
		this.pedidoTrasladoJdbcRepository = pedidoTrasladoJdbcRepository;
		this.catalogJdbcRepository = catalogJdbcRepository;
		this.programArriboRepositoryPort=programArriboRepositoryPor;
	}

	private SapResponse sapResponse(ConfirmacionDespachoRequest req,Float pesoNeto) throws JsonMappingException, JsonProcessingException {
		String jsonResponse = this.confDespachoSapClientAdapter.sendConfirmacionDespacho(req.getClaveSilo(),
				req.getClaveMaterial(), req.getNumPedidoTraslado(), req.getTipoMovimiento(), req.getNumBoleta(),
				pesoNeto.toString(), req.getClaveDestino(), "");
		if (jsonResponse != null) {
			ObjectMapper mapper = new ObjectMapper();
			SapResponse responseObj = mapper.readValue(jsonResponse, SapResponse.class);
			return responseObj;
		}
		return null;
	}
	
	private Float pesoTotalPedidoTraslado(List<String> numPedidoTraslados, String claveSilo,
			String claveMaterial, String clavePlanta, String fechaInicio, String fechaFin) {
		return this.programArriboRepositoryPort.findPesoNetoByNumPedTraslado(numPedidoTraslados, claveSilo, claveMaterial, clavePlanta, fechaInicio, fechaFin);
	}
	
	//envia siempre 351 a sap y registra la confirmacion despacho en base de datos
	@Override
	public ConfirmacionDespachoResponse save(ConfirmacionDespachoRequest req) {
		Float pesoNeto = req.getPesoBruto() - req.getPesoTara();
		ConfirmacionDespachoResponse response = new ConfirmacionDespachoResponse();
		try {
			Float pesoTotalProgramadoArribo=this.pesoTotalPedidoTraslado(List.of(req.getNumPedidoTraslado()),req.getClaveSilo(),
					req.getClaveMaterial(),req.getClaveDestino(),EMPTY, EMPTY);
			SapResponse sapResponse=sapResponse(req,pesoNeto);
			if(sapResponse!=null) {
				response.setCode(sapResponse.getT_RETURN().get(0).getType());
				response.setNumeroSap(String.valueOf(sapResponse.getT_RETURN().get(0).getMessage_V1()));
				if (response.getCode().equals(ESTATUS_EXITOSO_SAP)) {
					
					Map<String, String> result = this.confDespachoJdbcRepository.registroConfDespacho(req,
							(String) sapResponse.getT_RETURN().get(0).getMessage_V1(),
							sapResponse.getT_RETURN().get(0).getType());
					response.setMensaje(sapResponse.getT_RETURN().get(0).getMessage());
					
					if (result.get(ConfirmacionDespachoUtil.VALUE_STATUS).equals(ConfirmacionDespachoUtil.VALUE_ZERO_STR)) {
						response.setId(result.get(KEY_ID));
						response.setMensaje(result.get(KEY_FOLIO));
						this.catalogJdbcRepository.restaCantidadStockSilo(pesoNeto,req.getClaveSilo(),-1);
						pedidoTrasladoJdbcRepository.restaCantidadPedTraslado(pesoNeto, req.getNumPedidoTraslado(), 1);
					} else {
						response.setCode(ESTATUS_CODE_FAIL_SAVE_BD);
						response.setId(result.get(KEY_ID));
						response.setMensaje("Error al registrar la confirmacion despacho");
					}
					
				}else {
					response.setCode(ESTATUS_CODE_FAIL_SAP);
					response.setId(EMPTY);
					response.setMensaje(sapResponse.getT_RETURN().get(0).getMessage());
				}
				return response;
			}
		} catch (JsonMappingException e) {
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.save: "+e.getMessage());
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.save: "+e.getCause());
			response.setCode("ERRO-C-001");
			response.setNumeroSap("");
			response.setMensaje(e.getMessage());
		} catch (JsonProcessingException e) {
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.save: "+e.getMessage());
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.save: "+e.getCause());
			response.setCode("ERRO-C-002");
			response.setNumeroSap("");
			response.setMensaje(e.getMessage());
		}
		return response;
	}
	
	private boolean isSuma(Float pesoNetoOld,Float pesoNeto) {
		return pesoNetoOld<pesoNeto?true:false;
	}

	//actualiza la informacion en base de datos y envia 352 a sap con el peso neto anterior y luego tambien envia un 351 con el nuevo peso
	@Override
	public ConfirmacionDespachoResponse updateSap(ConfirmacionDespachoRequest req) {
		ConfirmacionDespachoResponse response = new ConfirmacionDespachoResponse();
		boolean isSuma=false;
		Float pesoNetoResult=0.0F;
		try {
			Float pesoNeto = req.getPesoBruto() - req.getPesoTara();
			Float pesoNetoOld = req.getPesoBruto() - req.getPesoTara();
			Float differencia=0F;
			Float pesoNetoOldSinModified=0.0F;
			String jsonResponse=EMPTY;
			//352 cancela la confirmacion despacho enviando peso neto y 352
			if(req.getTipoMovimiento().equals(OPERACION_352)) {
				pesoNetoOld=this.confDespachoJdbcRepository.findPesoNetoByIdPedtraslado(req.getIdConfDespacho());
				pesoNetoOldSinModified=pesoNetoOld;
				pesoNetoResult=pesoNetoOldSinModified;
				if(pesoNeto!=null && pesoNetoOld!=null) {
					isSuma=this.isSuma(pesoNetoOld, pesoNeto);
				}
				if(pesoNetoOld!=null) {
					differencia= Math.abs(pesoNeto-pesoNetoOld);
				}
				jsonResponse=this.confDespachoSapClientAdapter.sendConfirmacionDespacho(req.getClaveSilo(),
						req.getClaveMaterial(), req.getNumPedidoTraslado(), req.getTipoMovimiento(), req.getNumBoleta(),
						pesoNetoOldSinModified.toString(), req.getClaveDestino(),EMPTY);
			}else {
				//envia 351 con el peso neto nuevo
				jsonResponse = this.confDespachoSapClientAdapter.sendConfirmacionDespacho(req.getClaveSilo(),
						req.getClaveMaterial(), req.getNumPedidoTraslado(), req.getTipoMovimiento(), req.getNumBoleta(),
						pesoNeto.toString(), req.getClaveDestino(),EMPTY);
				pesoNetoResult=pesoNeto;
			}
			if (jsonResponse != null) {
				ObjectMapper mapper = new ObjectMapper();
				SapResponse responseObj = mapper.readValue(jsonResponse, SapResponse.class);
				String codeResponse = responseObj.getT_RETURN().get(0).getType();
				String numeroSap = String.valueOf(responseObj.getT_RETURN().get(0).getMessage_V1());
				String mensajeSap = responseObj.getT_RETURN().get(0).getMessage();
				response.setCode(codeResponse);
				response.setNumeroSap(numeroSap);
				response.setMensaje(mensajeSap);
				if (codeResponse.equals(ESTATUS_EXITOSO_SAP)) {
					Map<String, String> result = this.confDespachoJdbcRepository.updateConfDespacho(req, numeroSap,
							codeResponse);
					if (result.get("estatus").equals("0")) {
						response.setCode("0");
						logger.info("=============== TRANSACCION REGISTRO CON SAP ================");
						logger.info("TIPO MOVIMIENTO: "+req.getTipoMovimiento());
						logger.info("PESO NETO: "+pesoNetoResult);
						logger.info("NUMERO PEDIDO TRASLADO: "+req.getNumPedidoTraslado());
						logger.info("SILO: "+req.getClaveSilo());
						logger.info("MATERIAL: "+req.getClaveMaterial());
						logger.info("===============================================");
						if(!isSuma) {
							pedidoTrasladoJdbcRepository.sumaCantidadPedTraslado(differencia, req.getNumPedidoTraslado(), 1);
							this.catalogJdbcRepository.restaCantidadStockSilo(differencia,req.getClaveSilo(),-1);
						}else {
							pedidoTrasladoJdbcRepository.restaCantidadPedTraslado(differencia, req.getNumPedidoTraslado(), 1);
							this.catalogJdbcRepository.sumaCantidadStockSilo(differencia,req.getClaveSilo(),-1);
						}
						
					}else {
						response.setCode(ESTATUS_CODE_FAIL_SAVE_BD);
						response.setMensaje("Hubo un error al actualizar la confirmacion despacho");
					}
				}else {
					response.setCode(ESTATUS_CODE_FAIL_SAP);
					response.setId("");
					response.setMensaje("Error al registrar la confirmacion despacho en sap");
				}
				return response;
			}
		} catch (JsonMappingException e) {
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.updateSap: "+e.getMessage());
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.updateSap: "+e.getCause());
			response.setCode("ERRO-C-001");
			response.setNumeroSap("");
			response.setMensaje(e.getMessage());
		} catch (JsonProcessingException e) {
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.updateSap: "+e.getMessage());
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.updateSap: "+e.getCause());
			response.setCode("ERRO-C-002");
			response.setNumeroSap("");
			response.setMensaje(e.getMessage());
		}
		return response;
	}

	//actualiza solo la informacion de la base de datos de confirmacion despacho sin cambio en pesos netos
	@Override
	public ConfirmacionDespachoResponse updateBd(ConfirmacionDespachoRequest req) {
		ConfirmacionDespachoResponse response = new ConfirmacionDespachoResponse();
		try {
			Map<String, String> result = this.confDespachoJdbcRepository.updateConfDespachoSinSap(req, "");
			response.setCode(result.get("estatus"));
			logger.info("=============== TRANSACCION REGISTRO SIN SAP ================");
			logger.info("TIPO MOVIMIENTO: "+req.getTipoMovimiento());
			logger.info("NUMERO PEDIDO TRASLADO: "+req.getNumPedidoTraslado());
			logger.info("SILO: "+req.getClaveSilo());
			logger.info("MATERIAL: "+req.getClaveMaterial());
			logger.info("===============================================");
			return response;
		} catch (Exception e) {
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.updateBd: "+e.getMessage());
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.updateBd: "+e.getCause());
			response.setCode("ERRO-C-002");
			response.setNumeroSap("");
			response.setMensaje(e.getMessage());
		}
		return response;
	}

	//valida que no hubo cambio de pesos neto cuando se modifica una confirmacion despacho 
	@Override
	public ConfirmacionDespachoResponse esIgualPesosPorId(ConfDespachoPesosRequest req) {
		ConfirmacionDespachoResponse response = new ConfirmacionDespachoResponse();
		try {
			Boolean result = this.confDespachoJdbcRepository.esIgualPesoBrutoYPesotaraPorId(req.getPesoBruto(),
					req.getPesoTara(), req.getIdconfDespacho());
			if (result) {
				response.setCode("0");
				response.setMensaje("Los pesos son los mismos");
			} else {
				response.setCode("-1");
				response.setMensaje("Los pesos no son los mismsos");
			}
			return response;
		} catch (Exception e) {
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.esIgualPesosPorId: "+e.getMessage());
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.esIgualPesosPorId: "+e.getCause());
			response.setCode("ERRO-C-002");
			response.setNumeroSap("");
			response.setMensaje(e.getMessage());
		}
		return response;
	}

	@Override
	public Float findCantidadPromediotrasporte(Integer siloId) {
		return this.confDespachoJdbcRepository.findPromedioPesoNetoConfDespacho();
	}

	@Override
	public List<ConfirmDespachoResponse> findAllConfirmacionDespacho(String silo, String material, String fechaInicio,
			String fechaFin) {
		return this.confDespachoJdbcRepository.findAllConfirmacionesDespacho(silo,material,fechaInicio,fechaFin);
	}

	//envia 352 con el peso neto para cancelar la confimacion despacho
	@Override
	public ConfirmacionDespachoResponse delete(ConfirmacionDespachoRequest req) {
		ConfirmacionDespachoResponse response = new ConfirmacionDespachoResponse();
		try {
			Float pesoNetoOld = 0.0f;
			String jsonResponse="";
			pesoNetoOld=this.confDespachoJdbcRepository.findPesoNetoByIdPedtraslado(req.getIdConfDespacho());
			if(pesoNetoOld==null) {
				return response;
			}
			jsonResponse=this.confDespachoSapClientAdapter.sendConfirmacionDespacho(req.getClaveSilo(),
						req.getClaveMaterial(), req.getNumPedidoTraslado(), req.getTipoMovimiento(), req.getNumBoleta(),
						pesoNetoOld.toString(), req.getClaveDestino(), "");
			if (jsonResponse != null) {
				ObjectMapper mapper = new ObjectMapper();
				SapResponse responseObj = mapper.readValue(jsonResponse, SapResponse.class);
				String codeResponse = responseObj.getT_RETURN().get(0).getType();
				String numeroSap = String.valueOf(responseObj.getT_RETURN().get(0).getMessage_V1());
				String mensajeSap = responseObj.getT_RETURN().get(0).getMessage();
				response.setCode(codeResponse);
				response.setNumeroSap(numeroSap);
				response.setMensaje(mensajeSap);
				if (codeResponse.equals(ESTATUS_EXITOSO_SAP)) {
					//despues que hubo cancelacion exitosa de sap, se tiene que eliminar el registro en la base de datos y se tiene que sumar nuevamente el stock
					int eliminado=this.confDespachoJdbcRepository.deleteById(req.getIdConfDespacho());
					if (eliminado!=0) {
						pedidoTrasladoJdbcRepository.sumaCantidadPedTraslado(pesoNetoOld, req.getNumPedidoTraslado(), 1);
					}else {
						response.setCode(ESTATUS_CODE_FAIL_SAVE_BD);
						response.setMensaje("Hubo un error al eliminar la confirmacion despacho");
					}
				}else {
					response.setCode(ESTATUS_CODE_FAIL_SAP);
					response.setId("");
					response.setMensaje("Error al eliminar la confirmacion despacho: "+mensajeSap);
				}
				return response;
			}
		} catch (JsonMappingException e) {
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.delete: "+e.getMessage());
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.delete: "+e.getCause());
			response.setCode("ERRO-C-001");
			response.setNumeroSap("");
			response.setMensaje(e.getMessage());
		} catch (JsonProcessingException e) {
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.delete: "+e.getMessage());
			logger.error("ERROR EN METODO ConfDespachoJdbcRepositoryAdapter.delete: "+e.getCause());
			response.setCode("ERRO-C-002");
			response.setNumeroSap("");
			response.setMensaje(e.getMessage());
		}
		return response;
	}
	
}
