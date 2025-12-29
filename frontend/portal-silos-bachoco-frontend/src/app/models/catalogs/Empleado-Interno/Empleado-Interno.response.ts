import { Departamento } from "./Departamento.response";
import { Empleado } from "./Empleado.response";

export interface EmpleadoInternoResponse {
    id:           number;
    empleado:     Empleado;
    departamento: Departamento;
    puesto:       any;
}
