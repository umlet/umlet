import * as path from 'path';
import * as fs from 'fs';
import * as os from 'os';

import { runTests } from 'vscode-test';

async function main() {
	try {
		// The folder containing the Extension Manifest package.json
		// Passed to `--extensionDevelopmentPath`
		const extensionDevelopmentPath = path.resolve(__dirname, '../../..');

		// The path to test runner
		// Passed to --extensionTestsPath
		const extensionTestsPath = path.resolve(__dirname, './suite/index');

		// Download VS Code, unzip it and run the integration test
		const testRoot = fs.mkdtempSync(path.join(os.tmpdir(), 'umlet-vscode-test-'));
		const workspacePath = path.join(testRoot, 'workspace');
		fs.mkdirSync(workspacePath);
		await runTests({
			extensionDevelopmentPath,
			extensionTestsPath,
			vscodeExecutablePath: process.env.VSCODE_EXECUTABLE_PATH,
			launchArgs: [workspacePath, '--disable-extensions', '--disable-workspace-trust', '--skip-welcome', '--skip-release-notes',
				'--user-data-dir=' + path.join(testRoot, 'profile'), '--extensions-dir=' + path.join(testRoot, 'extensions')]
		});
	} catch (err) {
		console.error('Failed to run tests');
		process.exit(1);
	}
}

main();
