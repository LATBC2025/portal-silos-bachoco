package com.bachoco.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.bachoco.dto.BoletaDTO;
import com.bachoco.dto.BoletaSalida;
import com.bachoco.dto.Cliente;
import com.bachoco.model.ReportConfDespacho;
import com.bachoco.persistence.repository.jdbc.ConfDespachoJdbcRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class ReporteConfDespachoService {
	
    @Autowired
    private org.thymeleaf.TemplateEngine templateEngine;
	private final ConfDespachoJdbcRepository confDespachoJdbcRepository;
	private final String PATH_NAME_LOGO="images/logo.png";
	private final String NAME_FILE_TEMPLATE_BOLETA="boleta";
	private final String NAME_FILE_TEMPLATE_BOLETA_HTMTL="boleta.html";
    
    public ReporteConfDespachoService(ConfDespachoJdbcRepository confDespachoJdbcRepository) {
		this.confDespachoJdbcRepository = confDespachoJdbcRepository;
	}
    

	public ByteArrayOutputStream reporteConfDespacho() throws FileNotFoundException, MalformedURLException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(out);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);
		// Título
		Paragraph titulo = new Paragraph("Reporte Confirmacion Despacho").setFontSize(20).setBold()
				.setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.LIGHT_GRAY)
				.setMarginBottom(20);
		document.add(titulo);
		// Imagen (logo)
		Image img = new Image(com.itextpdf.io.image.ImageDataFactory.create("src/main/resources/logo.png")).setWidth(150)
				.setHeight(80).setHorizontalAlignment(HorizontalAlignment.LEFT);
		document.add(img);
		// Tabla con datos
		float[] columnas = { 100f, 200f, 100f,100f, 200f, 100f,100f };
		Table table = new Table(columnas);
		table.setWidth(UnitValue.createPercentValue(100));
		// Encabezado
		table.addHeaderCell(new Cell().add(new Paragraph("Numero SAP")).setBackgroundColor(ColorConstants.GRAY));
		table.addHeaderCell(new Cell().add(new Paragraph("Numero Pedido Traslaldo")).setBackgroundColor(ColorConstants.GRAY));
		table.addHeaderCell(new Cell().add(new Paragraph("Cantidad Toneladas")).setBackgroundColor(ColorConstants.GRAY));
		table.addHeaderCell(new Cell().add(new Paragraph("Pedido Compra asociado")).setBackgroundColor(ColorConstants.GRAY));
		table.addHeaderCell(new Cell().add(new Paragraph("Chofer")).setBackgroundColor(ColorConstants.GRAY));
		table.addHeaderCell(new Cell().add(new Paragraph("Licencia Transportista")).setBackgroundColor(ColorConstants.GRAY));
		table.addHeaderCell(new Cell().add(new Paragraph("Placa Jaula")).setBackgroundColor(ColorConstants.GRAY));
		// Filas
		table.addCell("202510151001");
		table.addCell("4500256298");
		table.addCell("1,000");
		table.addCell("4500256221");
		table.addCell("Ricardo Salazar Perez");
		table.addCell("LCI1002901");
		table.addCell("SALTILLO-10992");
		document.add(table);
		// Pie de página
		Paragraph footer = new Paragraph("Generado automáticamente por el Sistema Portal de proveedores silo")
				.setFontSize(10).setTextAlignment(TextAlignment.CENTER).setMarginTop(50);
		document.add(footer);
		document.close();
		return out;
	}
	
	
	public ByteArrayOutputStream reporteConfDespachoTymeleaf() throws IOException {
        BoletaDTO boleta = new BoletaDTO(
                "161",
                "2025-10-15",
                "GRANEROS CONTINENTAL",
                "JUAN ANTONIO MOZQUEDA",
                "21UPB8",
                "Observaciones del despacho..."
        );
        Context context = new Context();
        context.setVariable("boleta", boleta);
        String html = templateEngine.process(NAME_FILE_TEMPLATE_BOLETA_HTMTL, context);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(baos);
        builder.run();
        return baos;
	}

	
	public BoletaSalida obtenerDatosBoletaByQuery(ReportConfDespacho conf) {
        Cliente proveedor = new Cliente(conf.getSilo(),
                "",
                "GCO070803V61", "8688166509");
        Cliente cliente = new Cliente(conf.getSilo(),
                "AV. TECNOLÓGICO 401, COL. ZONA INDUSTRIAL CELAYA, GTO. C.P. 38010",
                "BAC800208B25", null);
        BoletaSalida boleta = new BoletaSalida();
        boleta.setProveedor(proveedor);
        boleta.setCliente(cliente);
        boleta.setNombreSilo(conf.getSilo());
        boleta.setFolio(conf.getFolio());
        boleta.setNumeroBoleta(conf.getNumBoleta());
        boleta.setFecha(conf.getFechaEmbarque());
        boleta.setHora("");
        boleta.setProducto(conf.getMaterial());
        boleta.setTransportista(conf.getLineaTransportista());
        boleta.setGuia(conf.getNumeroSap());
        boleta.setChofer(conf.getChofer());
        boleta.setNumeroTicketBascula(conf.getFolio());
        boleta.setPlacasVehiculo(conf.getPlacaJaula());
        boleta.setObservaciones("DESTINO ".concat(conf.getPlantaDestino().concat(" NO. DE PEDIDO ").concat(conf.getNumPedTraslado())));
        boleta.setPedidoTraslado(conf.getNumPedTraslado());
        boleta.setPesoBruto(Double.parseDouble(conf.getPesoBruto()));
        boleta.setTara(Double.parseDouble(conf.getPesoTara()));
        boleta.setPesoNeto((Double.parseDouble(conf.getPesoBruto())-Double.parseDouble(conf.getPesoTara())));
        boleta.setHumedad(conf.getHumedad());
        return boleta;
    }
	
	public ByteArrayOutputStream generarPdfBoleta(Integer id) throws IOException {
	    ReportConfDespacho reporte = this.confDespachoJdbcRepository.findConfDespachoById(id);
	    BoletaSalida boleta = obtenerDatosBoletaByQuery(reporte);
	    // Convertir imagen a base64
	    String logoBase64 = imageToBase64();
	    Context context = new Context(new Locale("es", "MX"));
	    context.setVariable(NAME_FILE_TEMPLATE_BOLETA, boleta);
	    context.setVariable("logoBase64", logoBase64);
	    String htmlContent = templateEngine.process(NAME_FILE_TEMPLATE_BOLETA_HTMTL, context);
	    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
	        PdfRendererBuilder builder = new PdfRendererBuilder();
	        builder.withHtmlContent(htmlContent, "");
	        builder.toStream(os);
	        builder.run();
	        return os;
	    }
	}

	private String imageToBase64() throws IOException {
	    try (InputStream inputStream = getClass().getClassLoader()
	            .getResourceAsStream(PATH_NAME_LOGO)) {
	        if (inputStream == null) {
	            throw new IOException("No se pudo encontrar el logo en: images/logo.png");
	        }
	        byte[] imageBytes = inputStream.readAllBytes();
	        String base64 = java.util.Base64.getEncoder().encodeToString(imageBytes);
	        return "data:image/png;base64," + base64;
	    }
	}
}
