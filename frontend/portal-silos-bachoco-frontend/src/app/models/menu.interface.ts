export interface Menu {
  id: number;
  name: string;
  description: string | null;
  url: string;
  icon: string | null;
  children?: Menu[];
}
