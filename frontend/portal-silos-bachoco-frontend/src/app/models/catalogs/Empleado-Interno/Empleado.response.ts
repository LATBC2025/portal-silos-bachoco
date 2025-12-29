import { Usuario } from "./Usuario.response";

export interface Empleado {
    id:                number;
    nombre:            string;
    apellidoPaterno:   string;
    appelidoMaterno:   string;
    correo:            string;
    fechaContratacion: string;
    fechaBaja:         string;
    usuario:           Usuario;
}
