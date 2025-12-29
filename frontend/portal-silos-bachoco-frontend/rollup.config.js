// rollup.config.mjs
import path from 'path';
import { readdirSync } from 'fs';
import { fileURLToPath } from 'url';
import { defineConfig } from 'rollup';
import resolve from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';
import terser from '@rollup/plugin-terser';


// Necesario en ESM para emular __dirname
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Ruta donde Angular genera los JS
const distPath = path.resolve(
  __dirname,
  '../proyecto-bachuco-arq-hexagonal/portalBachuco/app-boot/src/main/webapp/browser'
);

// Obtener todos los archivos JS generados por Angular
const files = readdirSync(distPath).filter(f => f.endsWith('.js'));

// Archivos ordenados: polyfills, scripts, runtime, main, chunks
const orderedFiles = [
  files.find(f => f.startsWith('polyfills-legacy')),
  files.find(f => f.startsWith('runtime')),
  files.find(f => f.startsWith('scripts')),
  files.find(f => f.startsWith('main')),
  ...files.filter(f => f.startsWith('chunk'))
];

// Validar que existan los archivos principales
if (!orderedFiles[0] || !orderedFiles[2] || !orderedFiles[3]) {
  console.error({
    runtimeFile: orderedFiles[1],
    polyfillsFile: orderedFiles[0],
    mainFile: orderedFiles[3]
  });
  throw new Error(`❌ No se encontraron los archivos necesarios en distPath: ${distPath}`);
}

// Crear un solo archivo temporal concatenando todos los JS
const tempBundlePath = path.resolve(distPath, 'temp-concat.js');
let combinedContent = '';
orderedFiles.forEach(f => {
  const fullPath = path.resolve(distPath, f);
  combinedContent += readFileSync(fullPath, 'utf8') + '\n';
});
writeFileSync(tempBundlePath, combinedContent);
console.log(`✅ JS combinados en: ${tempBundlePath}`);

// Configuración de Rollup
export default defineConfig({
  input: tempBundlePath,
  output: {
    file: path.resolve(distPath, 'bundle-legacy.js'),
    format: 'iife',      // IIFE clásico compatible con todos los navegadores
    name: 'AppBundle',   // expuesto como window.AppBundle
    sourcemap: true,
  },
  plugins: [
    terser() // minifica el bundle final
  ]
});
