export interface EmpleadoExternoResponseDTO {
    id:      number;
    nombre:  string;
    rfc:     string;
    usuario:     string;
    correo:  string;
    siloId:   number;

    numeroProveedor?: string; // <-- el que viene del backend
  bodegasIds?: number[];    // <-- si también lo regresas
}
