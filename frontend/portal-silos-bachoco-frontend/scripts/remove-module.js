import { readFileSync, writeFileSync } from 'fs';
// Cambia 'tu-app' por el nombre real del folder generado en dist
const filePath = '../proyecto-bachuco-arq-hexagonal/portalBachuco/app-boot/src/main/webapp/browser/index.html';
let html = readFileSync(filePath, 'utf8');
// Elimina type="module" y defer del script principal
html = html.replace(/type="module"/g, '');
html = html.replace(/defer/g, '');
writeFileSync(filePath, html);
console.log('✅ Atributos "type=module" y "defer" eliminados del index.html');