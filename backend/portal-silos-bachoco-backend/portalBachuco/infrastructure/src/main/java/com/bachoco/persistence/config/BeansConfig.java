package com.bachoco.persistence.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bachoco.model.service.DocumentoRepositoryPort;
import com.bachoco.port.BodegaRepositoryPort;
import com.bachoco.port.CategoriaRepositoryPort;
import com.bachoco.port.ConfirmacionDespachoJdbcRepository;
import com.bachoco.port.DepartamentoRepositoryPort;
import com.bachoco.port.EmpleadoExternoRepositoryPort;
import com.bachoco.port.EmpleadoInternoRepositoryPort;
import com.bachoco.port.EmpleadoRepositoryPort;
import com.bachoco.port.MaterialRepositoryPort;
import com.bachoco.port.PedidoCompraJdbcRepositoryPort;
import com.bachoco.port.PedidoTrasladoJdbcRepositoryPort;
import com.bachoco.port.PerfilRepositoryPort;
import com.bachoco.port.PlantaRepositoryPort;
import com.bachoco.port.ProgramArriboRepositoryPort;
import com.bachoco.port.PuestoRepositoryPort;
import com.bachoco.port.ReporteDespachosPort;
import com.bachoco.port.ReporteProgramArriboPort;
import com.bachoco.port.SiloRepositoryPort;
import com.bachoco.port.UsuarioRepositoryPort;
import com.bachoco.service.usecase.BodegaUsecase;
import com.bachoco.service.usecase.ConfirmacionDespachoUseCase;
import com.bachoco.service.usecase.DepartamentoUseCase;
import com.bachoco.service.usecase.EmpleadoExternoUseCase;
import com.bachoco.service.usecase.EmpleadoUseCase;
import com.bachoco.service.usecase.GenerarYEnviarOtpUseCase;
import com.bachoco.service.usecase.MaterialUseCase;
import com.bachoco.service.usecase.PedidoCompraUsecase;
import com.bachoco.service.usecase.PedidoTrasladoJdbcUseCase;
import com.bachoco.service.usecase.PlantaUseCase;
import com.bachoco.service.usecase.ProgramArriboUseCase;
import com.bachoco.service.usecase.PuestoUseCase;
import com.bachoco.service.usecase.ReporteDespachoUseCase;
import com.bachoco.service.usecase.ReporteProgramArriboUseCase;
import com.bachoco.service.usecase.SiloUseCase;
import com.bachoco.service.usecase.UsuarioUseCase;

@Configuration
public class BeansConfig {

	@Bean
	public MaterialUseCase createMaterialUseCase(MaterialRepositoryPort materialRepositoryPort,CategoriaRepositoryPort categoriaRepositoryPort) {
		return new MaterialUseCase(materialRepositoryPort,categoriaRepositoryPort);
	}
	
	@Bean
	public SiloUseCase siloUsecase(SiloRepositoryPort siloRepositoryPort) {
		return new SiloUseCase(siloRepositoryPort);
	}
	
	@Bean
	public BodegaUsecase bodegaSiloCaseUse(BodegaRepositoryPort bodegaRepositoryPort,
			SiloRepositoryPort siloRepositoryPort) {
		return new BodegaUsecase(bodegaRepositoryPort,siloRepositoryPort);
	}
	
	@Bean
	public PlantaUseCase plantaUseCase(PlantaRepositoryPort plantaRepositoryPort) {
		return new PlantaUseCase(plantaRepositoryPort);
	}
	
	@Bean
	public EmpleadoUseCase empleadoUseCase(UsuarioRepositoryPort usuarioRepositoryPort,EmpleadoRepositoryPort empleadoRepositoryPort,
			 EmpleadoInternoRepositoryPort empleadoInternoRepositoryPort,PerfilRepositoryPort perfilRepositoryPort,
			 GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase) {
		return new EmpleadoUseCase(usuarioRepositoryPort, empleadoRepositoryPort,empleadoInternoRepositoryPort,perfilRepositoryPort,generarYEnviarOtpUseCase);
	}
	
	@Bean
	public UsuarioUseCase usuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort,EmpleadoRepositoryPort empleadoRepositoryPort,GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase) {
		return new UsuarioUseCase(usuarioRepositoryPort,empleadoRepositoryPort,generarYEnviarOtpUseCase);
	}
	
	@Bean
	public PedidoCompraUsecase pedidoCompraUsecase(PedidoCompraJdbcRepositoryPort pedidoCompraRepositoryPort,DocumentoRepositoryPort documentoRepositoryPort) {
		return new PedidoCompraUsecase(pedidoCompraRepositoryPort,documentoRepositoryPort);
	}
	
	@Bean
	public PedidoTrasladoJdbcUseCase pedidoTrasladoJdbcUseCase(PedidoTrasladoJdbcRepositoryPort pedidoTrasladoJdbcRepositoryPort) {
		return new PedidoTrasladoJdbcUseCase(pedidoTrasladoJdbcRepositoryPort);
	}
	
	@Bean
	public ReporteProgramArriboUseCase reporteProgramArriboUseCase( ReporteProgramArriboPort programArriboPort) {
		return new ReporteProgramArriboUseCase(programArriboPort);
	}
	
	@Bean
	public ReporteDespachoUseCase reporteDespachoUseCase(ReporteDespachosPort despachosPort) {
		return new ReporteDespachoUseCase(despachosPort);
	}
	
	@Bean
	public EmpleadoExternoUseCase empleadoExternoUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
			EmpleadoExternoRepositoryPort empExternoRepositoryPort,
			GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase) {
		return new EmpleadoExternoUseCase(usuarioRepositoryPort,empExternoRepositoryPort,generarYEnviarOtpUseCase);
	}
	
	@Bean
	public ConfirmacionDespachoUseCase confirmacionDespachoUseCase(ConfirmacionDespachoJdbcRepository port) {
		return new ConfirmacionDespachoUseCase(port);
	}
	
	@Bean
	public DepartamentoUseCase departamentoUseCase(DepartamentoRepositoryPort deptoRepositoryPort) {
		return new DepartamentoUseCase(deptoRepositoryPort);
	}
	
	@Bean
	public PuestoUseCase puestoUseCase(PuestoRepositoryPort puestoRepositoryPort) {
		return new PuestoUseCase(puestoRepositoryPort);
	}
	
	@Bean
	public ProgramArriboUseCase programArriboUseCase(ProgramArriboRepositoryPort programArriboRepositoryPort) {
		return new ProgramArriboUseCase(programArriboRepositoryPort);
	}
}
