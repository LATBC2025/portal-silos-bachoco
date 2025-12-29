/***************************************************************************************************
 * POLYFILLS para navegadores antiguos (como IE11 o Edge Legacy)
 **************************************************************************************************/

import 'core-js/es';          // Polyfills generales (Promises, Map, Set, etc.)
import 'core-js/stable';      // Soporte adicional para objetos ES2015+
import 'zone.js';             // Angular requiere Zone.js (ya incluye dist/zone)
