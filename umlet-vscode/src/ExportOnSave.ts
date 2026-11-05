import * as vscode from 'vscode';

interface ExportRequest {
    id: number;
    uri: vscode.Uri;
    text: string;
    scale: number;
}

interface ActiveExport {
    request: ExportRequest;
    panel: vscode.WebviewPanel;
    writing: boolean;
}

interface DocumentExports {
    panels: Map<vscode.WebviewPanel, boolean>;
    latestId: number;
    pending?: ExportRequest;
    active?: ActiveExport;
    timer?: NodeJS.Timeout;
}

/** One export queue per document, independent of editor focus and manual exports. */
export class ExportOnSave implements vscode.Disposable {
    private readonly documents = new Map<string, DocumentExports>();
    private readonly subscriptions: vscode.Disposable[] = [];
    private readonly panelSubscriptions = new Set<vscode.Disposable>();
    private nextId = 0;
    private disposed = false;

    constructor(
        private readonly reportError: (message: string) => void = message => { vscode.window.showErrorMessage(message); },
        private readonly timeoutMs = 30000
    ) {
        this.subscriptions.push(vscode.workspace.onDidSaveTextDocument(document => this.saved(document)));
    }

    register(document: vscode.TextDocument, panel: vscode.WebviewPanel): void {
        const key = document.uri.toString();
        let state = this.documents.get(key);
        if (!state) {
            state = {panels: new Map(), latestId: 0};
            this.documents.set(key, state);
        }
        const exports = state;
        exports.panels.set(panel, false);
        const messages = panel.webview.onDidReceiveMessage(message => {
            if (message.command === 'pngExportReady') {
                exports.panels.set(panel, true);
                this.start(key, exports);
            } else if (message.command === 'exportPngOnSave') {
                this.received(key, exports, panel, message);
            }
        });
        const closed = panel.onDidDispose(() => {
            registration.dispose();
            this.panelSubscriptions.delete(registration);
            exports.panels.delete(panel);
            if (exports.active?.panel === panel && !exports.active.writing) {
                // Retry the latest snapshot in another view of this same document, if available.
                exports.pending = exports.pending || exports.active.request;
                exports.active = undefined;
                this.clearTimer(exports);
            }
            if (exports.panels.size === 0) {
                exports.pending = undefined;
                this.clearTimer(exports);
            }
            this.start(key, exports);
        });
        // Panel-owned listeners are released on close; no growing context subscription list.
        const registration = new vscode.Disposable(() => { messages.dispose(); closed.dispose(); });
        this.panelSubscriptions.add(registration);
    }

    private saved(document: vscode.TextDocument): void {
        const key = document.uri.toString();
        const state = this.documents.get(key);
        if (!state || document.isUntitled || !/\/[^/]+\.uxf$/i.test(document.uri.path)) {
            return;
        }
        const config = vscode.workspace.getConfiguration('umlet', document.uri);
        state.latestId = ++this.nextId;
        state.pending = undefined;
        if (!config.get<boolean>('exportOnSave', false)) {
            return;
        }
        const scale = config.get<number>('exportScale', 4);
        if (!Number.isFinite(scale) || scale < 1) {
            this.reportError('UMLet PNG export failed: umlet.exportScale must be a finite number of at least 1.');
            return;
        }
        state.pending = {id: state.latestId, uri: document.uri, text: document.getText(), scale};
        this.start(key, state);
    }

    private start(key: string, state: DocumentExports): void {
        if (this.disposed || state.active) {
            return;
        }
        if (!state.pending || state.panels.size === 0) {
            this.clearTimer(state);
            if (state.panels.size === 0) {
                this.documents.delete(key);
            }
            return;
        }
        const readyPanel = Array.from(state.panels.keys()).find(panel => state.panels.get(panel));
        if (!readyPanel) {
            if (!state.timer) {
                state.timer = setTimeout(() => {
                    const request = state.pending;
                    state.pending = undefined;
                    this.clearTimer(state);
                    if (request) {
                        this.failed(request, 'the UMLet renderer did not become ready in time');
                    }
                }, this.timeoutMs);
            }
            return;
        }
        this.clearTimer(state);
        const active: ActiveExport = {request: state.pending, panel: readyPanel, writing: false};
        state.pending = undefined;
        state.active = active;
        state.timer = setTimeout(() => this.finish(key, state, active, 'the UMLet renderer did not respond in time'), this.timeoutMs);
        try {
            Promise.resolve(readyPanel.webview.postMessage({
                command: 'requestExportPngOnSave',
                requestId: active.request.id,
                text: active.request.text,
                scale: active.request.scale
            })).then(delivered => {
                if (!delivered) {
                    this.finish(key, state, active, 'the UMLet editor is unavailable');
                }
            }, error => this.finish(key, state, active, String(error)));
        } catch (error) {
            this.finish(key, state, active, String(error));
        }
    }

    private async received(key: string, state: DocumentExports, panel: vscode.WebviewPanel, message: any): Promise<void> {
        const active = state.active;
        if (!active || active.panel !== panel || active.request.id !== message.requestId || active.writing) {
            return;
        }
        this.clearTimer(state);
        if (active.request.id !== state.latestId || !vscode.workspace.getConfiguration('umlet', active.request.uri).get('exportOnSave', false)) {
            this.finish(key, state, active);
            return;
        }
        try {
            if (message.error) {
                throw new Error(message.error);
            }
            const data = decodePng(message.text);
            const destination = active.request.uri.with({path: active.request.uri.path.replace(/\.uxf$/i, '.png')});
            // Keep the queue occupied until the write settles, even if the panel closes.
            // Otherwise a slow earlier write could overwrite a newer export after reopening.
            active.writing = true;
            await vscode.workspace.fs.writeFile(destination, data);
            this.finish(key, state, active);
        } catch (error) {
            this.finish(key, state, active, String(error));
        }
    }

    private finish(key: string, state: DocumentExports, active: ActiveExport, error?: string): void {
        if (state.active !== active) {
            return;
        }
        this.clearTimer(state);
        state.active = undefined;
        if (error && active.request.id === state.latestId && !this.disposed) {
            this.failed(active.request, error);
        }
        this.start(key, state);
    }

    private failed(request: ExportRequest, error: string): void {
        this.reportError(`UMLet could not export ${request.uri.toString()} as PNG: ${error}. The saved UXF file is unchanged.`);
    }

    private clearTimer(state: DocumentExports): void {
        if (state.timer) {
            clearTimeout(state.timer);
            state.timer = undefined;
        }
    }

    dispose(): void {
        this.disposed = true;
        this.subscriptions.forEach(subscription => subscription.dispose());
        this.panelSubscriptions.forEach(subscription => subscription.dispose());
        this.panelSubscriptions.clear();
        this.documents.forEach(state => this.clearTimer(state));
        this.documents.clear();
    }
}

function decodePng(dataUrl: unknown): Uint8Array {
    const prefix = 'data:image/png;base64,';
    if (typeof dataUrl !== 'string' || !dataUrl.startsWith(prefix)) {
        throw new Error('the renderer returned no PNG image');
    }
    const encoded = dataUrl.substring(prefix.length);
    const data = Buffer.from(encoded, 'base64');
    if (data.toString('base64') !== encoded || data.length < 24 || data.slice(0, 8).toString('hex') !== '89504e470d0a1a0a') {
        throw new Error('the renderer returned invalid PNG data');
    }
    return data;
}
