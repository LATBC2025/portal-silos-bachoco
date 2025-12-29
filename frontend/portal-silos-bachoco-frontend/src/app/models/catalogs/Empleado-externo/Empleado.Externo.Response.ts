import { Empleado } from "../Empleado-Interno/Empleado.response";
import { Silo } from "./Silo.Response";

export interface EmpleadoExternoResponse {
    id:           number;
    empleado:     Empleado;
    silo:       Silo;
}