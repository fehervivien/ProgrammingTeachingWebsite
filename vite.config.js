import { defineConfig } from 'vite';
import { resolve } from 'node:path';

export default defineConfig({
    configFile: resolve(process.cwd(), 'vite.config.js'),

    base: '/js/',

    build: {
        outDir: 'src/main/resources/static/js',
        emptyOutDir: false,

        rolldownOptions: {
            input: resolve(
                process.cwd(),
                'src/main/frontend/editor.js'
            ),

            output: {
                entryFileNames: 'editor.bundle.js',
                chunkFileNames: 'chunks/[name]-[hash].js',
                assetFileNames: 'assets/[name]-[hash][extname]'
            }
        }
    }
});