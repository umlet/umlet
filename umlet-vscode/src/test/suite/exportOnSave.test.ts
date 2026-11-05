import * as assert from 'assert';
import * as vscode from 'vscode';
import * as http from 'http';
import * as path from 'path';
import * as connect from 'connect';
import * as serveStatic from 'serve-static';
import * as parser from 'fast-xml-parser';
import * as nodeFs from 'fs';
import {ExportOnSave} from '../../ExportOnSave';
import {UmletEditorProvider} from '../../UmletEditorProvider';

export const png = Buffer.from('iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+aX1sAAAAASUVORK5CYII=', 'base64');
const pngUrl = 'data:image/png;base64,' + png.toString('base64');

export async function until(condition: () => boolean, timeout = 5000): Promise<void> {
    const end = Date.now() + timeout;
    while (!condition()) {
        if (Date.now() > end) {
            assert.fail('Timed out waiting for test condition');
        }
        await new Promise(resolve => setTimeout(resolve, 10));
    }
}

export class MemoryFileSystem implements vscode.FileSystemProvider {
    private readonly changes = new vscode.EventEmitter<vscode.FileChangeEvent[]>();
    readonly onDidChangeFile = this.changes.event;
    readonly files = new Map<string, Uint8Array>();
    readonly pngWrites: string[] = [];
    failWrites = false;
    holdWrite?: Promise<void>;
    watch(): vscode.Disposable { return new vscode.Disposable(() => {}); }
    stat(uri: vscode.Uri): vscode.FileStat {
        const data = this.files.get(uri.path);
        if (!data) { throw vscode.FileSystemError.FileNotFound(uri); }
        return {type: vscode.FileType.File, ctime: 1, mtime: 1, size: data.length};
    }
    readDirectory(): [string, vscode.FileType][] { return []; }
    createDirectory(): void {}
    readFile(uri: vscode.Uri): Uint8Array {
        this.stat(uri);
        return this.files.get(uri.path)!;
    }
    async writeFile(uri: vscode.Uri, content: Uint8Array): Promise<void> {
        if (uri.path.endsWith('.png')) {
            this.pngWrites.push(uri.path);
            if (this.failWrites) { throw vscode.FileSystemError.NoPermissions('PNG destination is read-only'); }
            if (this.holdWrite) { await this.holdWrite; }
        }
        this.files.set(uri.path, content.slice());
    }
    delete(uri: vscode.Uri): void { this.files.delete(uri.path); }
    rename(): void { throw vscode.FileSystemError.Unavailable(); }
}

class TestPanel {
    private readonly incoming = new vscode.EventEmitter<any>();
    private readonly closing = new vscode.EventEmitter<void>();
    readonly messages: any[] = [];
    delivered = true;
    readonly panel = {
        webview: {
            onDidReceiveMessage: this.incoming.event,
            postMessage: (message: any) => { this.messages.push(message); return Promise.resolve(this.delivered); }
        },
        onDidDispose: this.closing.event
    } as unknown as vscode.WebviewPanel;
    ready(): void { this.incoming.fire({command: 'pngExportReady'}); }
    reply(request = this.messages[this.messages.length - 1], text = pngUrl, error?: string): void {
        this.incoming.fire({command: 'exportPngOnSave', requestId: request.requestId, text, error});
    }
    close(): void { this.closing.fire(); this.closing.dispose(); this.incoming.dispose(); }
}

export async function save(document: vscode.TextDocument, text: string): Promise<void> {
    const edit = new vscode.WorkspaceEdit();
    edit.replace(document.uri, new vscode.Range(0, 0, document.lineCount, 0), text);
    assert.ok(await vscode.workspace.applyEdit(edit));
    assert.ok(await document.save());
}

suite('PNG export on save', function () {
    this.timeout(10000);
    let exporter: ExportOnSave;
    let fs: MemoryFileSystem;
    let provider: vscode.Disposable;
    let errors: string[];
    let panels: TestPanel[];
    let serial = 0;
    let scheme: string;

    setup(async () => {
        scheme = 'umlet-save-test-' + (++serial);
        fs = new MemoryFileSystem();
        provider = vscode.workspace.registerFileSystemProvider(scheme, fs);
        errors = [];
        panels = [];
        exporter = new ExportOnSave(message => errors.push(message), 1000);
        await vscode.workspace.getConfiguration('umlet').update('exportOnSave', true, vscode.ConfigurationTarget.Workspace);
        await vscode.workspace.getConfiguration('umlet').update('exportScale', 4, vscode.ConfigurationTarget.Workspace);
    });

    teardown(() => {
        panels.forEach(panel => panel.close());
        exporter.dispose();
        provider.dispose();
    });

    async function open(name = 'Architecture.uxf', ready = true): Promise<{document: vscode.TextDocument; view: TestPanel}> {
        const uri = vscode.Uri.parse(`${scheme}://remote/folder/${name}`);
        await vscode.workspace.fs.writeFile(uri, Buffer.from('<diagram/>'));
        const document = await vscode.workspace.openTextDocument(uri);
        const view = attach(document, ready);
        return {document, view};
    }

    function attach(document: vscode.TextDocument, ready = true): TestPanel {
        const view = new TestPanel();
        panels.push(view);
        exporter.register(document, view.panel);
        if (ready) { view.ready(); }
        return view;
    }

    test('disabled by default and disabled saves produce no PNG request', async () => {
        const config = vscode.workspace.getConfiguration('umlet');
        assert.strictEqual(config.inspect<boolean>('exportOnSave')!.defaultValue, false);
        await config.update('exportOnSave', false, vscode.ConfigurationTarget.Workspace);
        const {document, view} = await open();
        await save(document, '<diagram>disabled</diagram>');
        assert.strictEqual(view.messages.length, 0);
        assert.strictEqual(fs.pngWrites.length, 0);
    });

    test('enabled saves use the saved snapshot, sibling URI and configured scale; subsequent saves overwrite', async () => {
        await vscode.workspace.getConfiguration('umlet').update('exportScale', 2.5, vscode.ConfigurationTarget.Workspace);
        const {document, view} = await open('A%20%23%20B.UXF');
        const text = '<diagram>first</diagram>';
        await save(document, text);
        assert.strictEqual(view.messages[0].text, text);
        assert.strictEqual(view.messages[0].scale, 2.5);
        assert.strictEqual(view.messages[0].command, 'requestExportPngOnSave');
        view.reply();
        await until(() => fs.files.has('/folder/A # B.png'));
        assert.deepStrictEqual(Buffer.from(fs.files.get('/folder/A # B.png')!), png);
        await save(document, '<diagram>second</diagram>');
        await until(() => view.messages.length === 2);
        view.reply();
        await until(() => fs.pngWrites.length === 2);
        assert.deepStrictEqual(fs.pngWrites, ['/folder/A # B.png', '/folder/A # B.png']);
        assert.deepStrictEqual(errors, []);
    });

    test('saves and replies are associated with their own document, regardless of focus', async () => {
        const a = await open('A.uxf');
        const b = await open('B.uxf');
        await save(a.document, '<diagram>A</diagram>');
        assert.strictEqual(b.view.messages.length, 0);
        b.view.reply(a.view.messages[0]);
        assert.strictEqual(fs.pngWrites.length, 0);
        await save(b.document, '<diagram>B</diagram>');
        b.view.reply();
        a.view.reply();
        await until(() => fs.pngWrites.length === 2);
        assert.deepStrictEqual(fs.pngWrites.sort(), ['/folder/A.png', '/folder/B.png']);
    });

    test('rapid saves discard stale responses and coalesce to the newest saved contents', async () => {
        const {document, view} = await open();
        await save(document, 'one');
        const oldRequest = view.messages[0];
        await save(document, 'two');
        await save(document, 'three');
        assert.strictEqual(view.messages.length, 1);
        view.reply(oldRequest);
        assert.strictEqual(fs.pngWrites.length, 0);
        assert.strictEqual(view.messages.length, 2);
        assert.strictEqual(view.messages[1].text, 'three');
        view.reply();
        await until(() => fs.pngWrites.length === 1);
        view.reply(oldRequest);
        assert.strictEqual(fs.pngWrites.length, 1);
    });

    test('writes remain serialized across more saves and closing/reopening a view', async () => {
        const {document, view} = await open();
        let release!: () => void;
        fs.holdWrite = new Promise<void>(resolve => { release = resolve; });
        await save(document, 'first');
        view.reply();
        await until(() => fs.pngWrites.length === 1);
        view.close();
        const reopened = attach(document);
        await save(document, 'latest');
        assert.strictEqual(reopened.messages.length, 0);
        fs.holdWrite = undefined;
        release();
        await until(() => reopened.messages.length === 1);
        assert.strictEqual(reopened.messages[0].text, 'latest');
        reopened.reply();
        await until(() => fs.pngWrites.length === 2);
        assert.deepStrictEqual(errors, []);
    });

    test('waits for renderer readiness and sends only the latest save', async () => {
        const {document, view} = await open('Loading.uxf', false);
        await save(document, 'before ready');
        await save(document, 'newer');
        assert.strictEqual(view.messages.length, 0);
        view.ready();
        assert.strictEqual(view.messages.length, 1);
        assert.strictEqual(view.messages[0].text, 'newer');
        view.reply();
        await until(() => fs.pngWrites.length === 1);
    });

    test('closing a renderer cancels requests and retries in another view of the same document', async () => {
        const {document, view} = await open();
        const other = attach(document);
        await save(document, 'saved');
        view.close();
        assert.strictEqual(other.messages.length, 1);
        other.close();
        await new Promise(resolve => setTimeout(resolve, 1100));
        assert.strictEqual(fs.pngWrites.length, 0);
        assert.deepStrictEqual(errors, []);
    });

    test('renderer timeouts and failed delivery report errors and allow a later save', async () => {
        const {document, view} = await open('Slow.uxf', false);
        await save(document, 'waiting');
        await until(() => errors.length === 1);
        view.ready();
        await save(document, 'no response');
        const expired = view.messages[0];
        await until(() => errors.length === 2);
        view.reply(expired);
        assert.strictEqual(fs.pngWrites.length, 0);
        view.delivered = false;
        await save(document, 'delivery failure');
        await until(() => errors.length === 3);
        view.delivered = true;
        await save(document, 'recovered');
        view.reply();
        await until(() => fs.pngWrites.length === 1);
    });

    test('write failures and invalid renderer data leave the UXF unchanged and permit recovery', async () => {
        const {document, view} = await open();
        fs.failWrites = true;
        await save(document, 'saved source');
        view.reply();
        await until(() => errors.length === 1);
        assert.strictEqual(Buffer.from(fs.readFile(document.uri)).toString(), 'saved source');
        fs.failWrites = false;
        await save(document, 'saved source 2');
        view.reply(undefined, 'data:,');
        await until(() => errors.length === 2);
        assert.strictEqual(fs.pngWrites.length, 1);
        await save(document, 'saved source 3');
        view.reply(undefined, pngUrl, 'invalid UXF');
        await until(() => errors.length === 3);
        assert.strictEqual(fs.pngWrites.length, 1);
        await save(document, 'saved source 4');
        view.reply();
        await until(() => fs.files.has('/folder/Architecture.png'));
        assert.strictEqual(Buffer.from(fs.readFile(document.uri)).toString(), 'saved source 4');
    });

    test('unavailable filesystem providers fail gracefully after a save', async () => {
        const {document, view} = await open();
        await save(document, 'persisted');
        provider.dispose();
        view.reply();
        await until(() => errors.length === 1);
        assert.strictEqual(Buffer.from(fs.readFile(document.uri)).toString(), 'persisted');
    });

    test('invalid scale and extension disposal cannot write output', async () => {
        const {document, view} = await open();
        await vscode.workspace.getConfiguration('umlet').update('exportScale', 0, vscode.ConfigurationTarget.Workspace);
        await save(document, 'invalid scale');
        assert.strictEqual(errors.length, 1);
        assert.strictEqual(view.messages.length, 0);
        await vscode.workspace.getConfiguration('umlet').update('exportScale', 4, vscode.ConfigurationTarget.Workspace);
        await save(document, 'valid scale');
        exporter.dispose();
        view.reply();
        await save(document, 'after disposal');
        assert.strictEqual(view.messages.length, 1);
        assert.strictEqual(fs.pngWrites.length, 0);
    });

    test('untitled, non-UXF and unopened documents are ignored; disabling cancels pending output', async () => {
        const untitled = await vscode.workspace.openTextDocument({content: 'unsaved', language: 'umlet'});
        const untitledView = attach(untitled);
        // VS Code normally offers Save As instead of emitting a save for an untitled document.
        exporter['saved'](untitled);
        assert.strictEqual(untitledView.messages.length, 0);
        const text = await open('Other.txt');
        await save(text.document, 'text');
        assert.strictEqual(text.view.messages.length, 0);
        const {document, view} = await open();
        await save(document, 'enabled');
        await vscode.workspace.getConfiguration('umlet').update('exportOnSave', false, vscode.ConfigurationTarget.Workspace);
        view.reply();
        assert.strictEqual(fs.pngWrites.length, 0);
        view.close();
        await save(document, 'no view');
        assert.strictEqual(view.messages.length, 1);
    });
});

suite('PNG export with the GWT renderer', function () {
    this.timeout(60000);
    let server: http.Server;
    let port: number;
    let fs: MemoryFileSystem;
    let filesystem: vscode.Disposable;
    let editor: UmletEditorProvider;
    let subscriptions: vscode.Disposable[];
    let panels: vscode.WebviewPanel[];
    let manualPngs: Buffer[];
    let serial = 0;
    const extensionPath = path.resolve(__dirname, '../../../..');

    suiteSetup(async () => {
        const pom = parser.parse(nodeFs.readFileSync(path.join(extensionPath, 'pom.xml')).toString()).project;
        const webRoot = path.join(extensionPath, 'target', pom.artifactId + '-' + pom.parent.version);
        assert.ok(nodeFs.existsSync(path.join(webRoot, 'umletvscode', 'umletvscode.nocache.js')), 'Build GWT before running tests');
        server = http.createServer(connect().use(serveStatic(webRoot)));
        await new Promise<void>(resolve => server.listen(0, resolve));
        port = (server.address() as {port: number}).port;
        fs = new MemoryFileSystem();
        filesystem = vscode.workspace.registerFileSystemProvider('umlet-render-test', fs);
    });

    setup(async () => {
        subscriptions = [];
        panels = [];
        manualPngs = [];
        editor = new UmletEditorProvider({extensionPath, subscriptions} as unknown as vscode.ExtensionContext, port);
        // Capture the manual dialog input; the automatic protocol must never reach this method.
        editor.saveFileDecodePng = data => { manualPngs.push(Buffer.from(data, 'base64')); };
        await vscode.workspace.getConfiguration('umlet').update('exportOnSave', true, vscode.ConfigurationTarget.Workspace);
        await vscode.workspace.getConfiguration('umlet').update('exportScale', 2, vscode.ConfigurationTarget.Workspace);
    });

    teardown(() => {
        panels.forEach(panel => panel.dispose());
        subscriptions.forEach(subscription => subscription.dispose());
    });

    suiteTeardown(() => { filesystem.dispose(); server.close(); });

    function diagram(label: string, zoom = 10): string {
        return `<diagram program="umlet" version="15.1"><zoom_level>${zoom}</zoom_level><element><id>UMLClass</id>` +
            `<coordinates><x>${10 * zoom}</x><y>${10 * zoom}</y><w>${12 * zoom}</w><h>${6 * zoom}</h></coordinates>` +
            `<panel_attributes>${label}</panel_attributes><additional_attributes/></element></diagram>`;
    }

    async function open(text: string) {
        const uri = vscode.Uri.parse(`umlet-render-test://remote/diagram-${++serial}.uxf`);
        await vscode.workspace.fs.writeFile(uri, Buffer.from(text));
        const document = await vscode.workspace.openTextDocument(uri);
        const panel = vscode.window.createWebviewPanel('umlet.test', 'UMLet export test', vscode.ViewColumn.One,
            {enableScripts: true, retainContextWhenHidden: true});
        panels.push(panel);
        const messages: any[] = [];
        subscriptions.push(panel.webview.onDidReceiveMessage(message => messages.push(message)));
        const token = new vscode.CancellationTokenSource();
        subscriptions.push(token);
        editor.resolveCustomTextEditor(document, panel, token.token);
        await until(() => messages.some(message => message.command === 'pngExportReady'), 30000);
        return {document, panel, messages, pngPath: uri.path.replace('.uxf', '.png')};
    }

    test('real saves produce cropped PNGs from the correct document and match manual export at the same scale', async () => {
        const a = await open(diagram('Architecture A'));
        const b = await open(diagram('Database B'));
        await save(a.document, diagram('Architecture saved'));
        await until(() => fs.files.has(a.pngPath));
        assert.ok(!fs.files.has(b.pngPath));
        assert.strictEqual(manualPngs.length, 0, 'Automatic export must not open the manual Save dialog');
        const automatic = Buffer.from(fs.files.get(a.pngPath)!);
        assert.strictEqual(automatic.slice(0, 8).toString('hex'), '89504e470d0a1a0a');
        // 120x60 content at scale 2, plus the existing 10px border on each side.
        assert.strictEqual(automatic.readUInt32BE(16), 260);
        assert.strictEqual(automatic.readUInt32BE(20), 140);
        await a.panel.webview.postMessage({command: 'requestExportPng', text: 2});
        await until(() => manualPngs.length === 1);
        assert.deepStrictEqual(automatic, manualPngs[0]);
        assert.strictEqual(a.document.getText(), diagram('Architecture saved'));
        assert.strictEqual(a.document.isDirty, false);
        const before = fs.pngWrites.length;
        await save(a.document, diagram('New contents'));
        await until(() => fs.pngWrites.length > before);
        assert.notDeepStrictEqual(Buffer.from(fs.files.get(a.pngPath)!), automatic);
    });

    test('snapshot rendering preserves editor contents and normalizes zoom; invalid UXF cannot overwrite the PNG', async () => {
        const a = await open(diagram('Zoomed', 5));
        await save(a.document, diagram('Zoomed saved', 5));
        await until(() => fs.files.has(a.pngPath));
        const zoomed = Buffer.from(fs.files.get(a.pngPath)!);
        await a.panel.webview.postMessage({command: 'requestExportPng', text: 2});
        await until(() => manualPngs.length === 1);
        assert.deepStrictEqual(zoomed, manualPngs[0]);
        // Directly exercise the correlated snapshot protocol with a different image and scale.
        // An unknown request ID must not cause the host to write it or invoke manual export.
        const sourceBefore = a.document.getText();
        await a.panel.webview.postMessage({command: 'requestExportPngOnSave', requestId: -1, text: diagram('Snapshot'), scale: 1});
        await until(() => a.messages.some(message => message.requestId === -1));
        const result = a.messages.find(message => message.requestId === -1);
        const image = Buffer.from(result.text.replace('data:image/png;base64,', ''), 'base64');
        assert.strictEqual(image.readUInt32BE(16), 140);
        assert.strictEqual(image.readUInt32BE(20), 80);
        assert.strictEqual(a.document.getText(), sourceBefore);
        assert.strictEqual(a.document.isDirty, false);
        assert.deepStrictEqual(Buffer.from(fs.files.get(a.pngPath)!), zoomed);
        await save(a.document, '<diagram><element><id>NotAnElement</id></element></diagram>');
        await until(() => a.messages.some(message => message.command === 'exportPngOnSave' && message.error));
        assert.deepStrictEqual(Buffer.from(fs.files.get(a.pngPath)!), zoomed);
        assert.strictEqual(a.document.isDirty, false);
    });
});
