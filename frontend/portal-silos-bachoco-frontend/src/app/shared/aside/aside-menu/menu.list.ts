import { Menu } from "../../../models/menu.interface";

export const MENU: Menu[] = [
{
    id: 1,
    name: "Gestión de Cobranza",
    url: "/gestion-cobranza",
    icon: "dashboard",
    description: "Módulo para la gestión y control de la cobranza",
    children: [
        { id: 2, name: "Seleccion Cadena Comercial", url: "/seleccion-cadena-comercial", icon: null, description: "Seleccionar la cadena comercial" },
        { id: 3, name: "Conciliación Cuenta por Cobrar", url: "/conciliacion-cuenta-por-cobrar", icon: null, description: "Conciliar cuentas por cobrar" },
        { id: 4, name: "Cobranza de Cuenta por Cobrar", url: "/cobranza-cuenta-por-cobrar", icon: null, description: "Gestionar la cobranza de cuentas por cobrar" },
        { id: 5, name: "Contabilización de Pagos", url: "/contabilizacion-pagos", icon: null, description: "Contabilizar los pagos recibidos" },
        { id: 6, name: "Carga FBL5N", url: "/carga-fbl5n", icon: null, description: "Cargar datos FBL5N" },
        { id: 7, name: "Depósitos Bancarios", url: "/depositos-bancarios", icon: null, description: "Gestionar depósitos bancarios" },
        { id: 8, name: "Envío de Notificaciones", url: "/envio-notificaciones", icon: null, description: "Enviar notificaciones a los clientes" },
        { id: 9, name: "Carga IMR-NAR", url: "/carga-imr-nar", icon: null, description: "Cargar datos IMR-NAR" },
        { id: 10, name: "Contabilización Automática", url: "/contabilizacion-automatica", icon: null, description: "Contabilización automática de transacciones" },
        { id: 11, name: "Conciliación Filiales", url: "/conciliacion-filiales", icon: null, description: "Conciliar cuentas con filiales" }
    ]
},
{
    id: 12,
    name: "Gestión de catálogos",
    url: "/gestion-catalogos",
    icon: "dashboard",
    description: "Módulo para la gestión de catálogos",
    children: [
        { id: 13, name: "Administración de Catálogos", url: "/administracion-catalogos", icon: null, description: "Administrar los catálogos del sistema" }
    ]
},
{
    id: 14,
    name: "Gestión de reportes",
    url: "/gestion-reportes",
    icon: "dashboard",
    description: "Módulo para la gestión de reportes",
    children: [
        { id: 15, name: "Reportes Generales", url: "/reportes-generales", icon: null, description: "Generar reportes generales" },
        { id: 16, name: "Reportes de Administrador", url: "/reportes-administrador", icon: null, description: "Generar reportes para administradores" }
    ]
},
{
    id: 17,
    name: "Proceso de Impuestos",
    url: "/proceso-impuestos",
    icon: "dashboard",
    description: "Módulo para la gestión de impuestos",
    children: [
        { id: 18, name: "Reporte de impuestos", url: "/reporte-impuestos", icon: null, description: "Generar reportes de impuestos" },
        { id: 19, name: "Validación de impuestos", url: "/validacion-impuestos", icon: null, description: "Validar los impuestos" },
        { id: 20, name: "Catálogo Impuestos", url: "/catalogo-impuestos", icon: null, description: "Administrar el catálogo de impuestos" },
        { id: 21, name: "Detalle Acreditable", url: "/detalle-acreditable", icon: null, description: "Ver detalles acreditables" },
        { id: 22, name: "Partidas Acreditable", url: "/partidas-acreditable", icon: null, description: "Gestionar partidas acreditables" },
        { id: 23, name: "Partidas Traslado", url: "/partidas-traslado", icon: null, description: "Gestionar partidas de traslado" }
    ]
},
{
    id: 24,
    name: "Gestión de seguridad",
    url: "/security",
    icon: "dashboard",
    description: "Módulo para la gestión de seguridad",
    children: [
        { id: 25, name: "Administración de Usuarios", url: "/user", icon: null, description: "Administrar los usuarios del sistema" },
        { id: 26, name: "Administración de Perfiles", url: "/profile", icon: null, description: "Administrar los perfiles del sistema" },
    ]
},
{
    id: 27,
    name: "Gestión de incobrables",
    url: "/gestion-incobrables",
    icon: "dashboard",
    description: "Módulo para la gestión de incobrables"
},
{
    id: 28,
    name: "Gestión de notas de crédito",
    url: "/gestion-notas-credito",
    icon: "dashboard",
    description: "Módulo para la gestión de notas de crédito"
},
{
    id: 29,
    name: "Gestión de memos",
    url: "/gestion-memos",
    icon: "dashboard",
    description: "Módulo para la gestión de memos"
},
{
    id: 30,
    name: "Configuración",
    url: "/configuracion",
    icon: "dashboard",
    description: "Módulo de configuración del sistema"
}
];
