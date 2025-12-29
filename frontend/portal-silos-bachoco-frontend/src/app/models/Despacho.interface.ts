export interface Despacho {
  bodega: string;
  folio: string;
  fecha_embarque: string;
  num_boleta: string;
  peso_bruto: number | null;
  peso_tara:number | null;
  peso_neto:number | null;
  humedad:string | null;
  chofer:string | null;
  tractor:string | null;
  jaula:string | null;
  trasportista:string | null;
}
