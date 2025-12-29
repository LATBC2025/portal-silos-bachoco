// src/app/shared/models/menu-item.model.ts

import { IconProp } from "@fortawesome/fontawesome-svg-core";

export interface MenuItem {
  id: string;
  // Nivel de Acceso: 1 = Administrador/Todo, 2 = Usuario/Limitado, etc.
  permisosRequeridos: number[];
  // Título del enlace o encabezado del grupo
  titulo: string;
  // Subtítulo o descripción del encabezado del grupo (small class="text-muted...")
  subtitulo?: string; 
  // Icono de Material (ej. 'description', 'logout')
  icono: IconProp | null; 
  // Ruta del routerLink (Solo para enlaces directos o el nivel hoja).
  ruta?: string; 
  // Indica si es un contenedor de enlaces (mat-expansion-panel)
  isGroup: boolean; 
  // Hijos (sub-elementos o enlaces dentro del panel)
  children?: MenuItem[]; 
}