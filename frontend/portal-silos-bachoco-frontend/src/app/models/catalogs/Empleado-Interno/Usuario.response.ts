import { Perfile } from "./Perfile.response";

export interface Usuario {
    id:          number;
    usuarioTipo: string;
    usuario:     string;
    password:    null;
    activo:      null;
    perfiles:    Perfile[];
}