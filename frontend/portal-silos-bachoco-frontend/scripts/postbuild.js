// postbuild.js
import { readFileSync, writeFileSync } from 'fs';
const filePath = '../proyecto-bachuco-arq-hexagonal/portalBachuco/app-boot/src/main/webapp/browser/browser/index.html';

let html = readFileSync(filePath, 'utf8');

// 1️⃣ Elimina referencias modernas
html = html.replace(/type="module"/g, '');
html = html.replace(/defer/g, '');

// 2️⃣ Reemplaza el script principal por el bundle clásico
html = html.replace(/src=".*main.*\.js"/, 'src="bundle-legacy.js"');

writeFileSync(filePath, html);
console.log('✅ index.html actualizado con bundle clásico (sin module/defer)');
