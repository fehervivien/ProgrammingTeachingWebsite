import * as monaco from 'monaco-editor';

import editorWorker from
    '../../../node_modules/monaco-editor/esm/vs/editor/editor.worker.js?worker';

self.MonacoEnvironment = {
    getWorker() {
        return new editorWorker();
    }
};

const initialCode = `public class Main {
    public static void main(String[] args) {
        // Írd ide a kódod:

    }
}`;

const editor = monaco.editor.create(
    document.getElementById('code-editor'),
    {
        value: initialCode,
        language: 'java',
        theme: 'vs-dark',
        automaticLayout: true,
        fontSize: 14,
        lineNumbers: 'on',
        minimap: {
            enabled: false
        },
        scrollBeyondLastLine: false,
        tabSize: 4,
        insertSpaces: true,
        wordWrap: 'off',
        padding: {
            top: 12,
            bottom: 12
        }
    }
);

window.codeEditor = editor;