// En un servicio o archivo de utilidades
export class DateUtils {
  
  static getFechaHoy(): string {
    const ahora = new Date();
    const año = ahora.getFullYear();
    const mes = String(ahora.getMonth() + 1).padStart(2, '0');
    const dia = String(ahora.getDate()).padStart(2, '0');
    return `${año}-${mes}-${dia}`; // ✅ Formato correcto YYYY-MM-DD
  }

  static getFechaHoyFormateada(formato: string = 'YYYY-MM-DD'): string {
    const ahora = new Date();
    const año = ahora.getFullYear();
    const mes = String(ahora.getMonth() + 1).padStart(2, '0');
    const dia = String(ahora.getDate()).padStart(2, '0');

    switch (formato) {
      case 'DD/MM/YYYY':
        return `${dia}/${mes}/${año}`;
      case 'MM/DD/YYYY':
        return `${mes}/${dia}/${año}`;
      default: // 'YYYY-MM-DD'
        return `${año}-${mes}-${dia}`;
    }
  }
}

// Uso en tu componente:
const hoy = DateUtils.getFechaHoy();
const hoyFormateada = DateUtils.getFechaHoyFormateada('DD/MM/YYYY');