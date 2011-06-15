package com.umlet.custom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.SystemInfo;
import com.baselet.control.Path;
import com.baselet.control.Utils;
import com.baselet.element.ErrorOccurred;
import com.baselet.element.GridElement;

public class CustomElementCompiler {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private static CustomElementCompiler compiler;
	private static final String templatefile = "Default.java";
	private String template;
	private Pattern template_pattern;
	private Pattern empty_line;
	private Pattern error_pattern;
	private Matcher template_match;
	private String classname;
	private int beforecodelines; // lines of code before the custom code begins (for error processing)
	private Vector<String> compilation_errors;
	private boolean global_error;

	public static CustomElementCompiler getInstance() {
		if (compiler == null) compiler = new CustomElementCompiler();
		return compiler;
	}

	private File sourcefile;

	private CustomElementCompiler() {
		this.global_error = false;
		this.compilation_errors = new Vector<String>();
		this.beforecodelines = 0;
		this.template_pattern = Pattern.compile("(.*)(/\\*\\*\\*\\*CUSTOM_CODE START\\*\\*\\*\\*/\n)(.*)(\n\\s\\s/\\*\\*\\*\\*CUSTOM_CODE END\\*\\*\\*\\*/)(.*)", Pattern.DOTALL);
		this.error_pattern = Pattern.compile(".*ERROR.*at line ([0-9]+).*\\s*$");
		this.empty_line = Pattern.compile("^\\s*$");
		this.template = this.loadJavaSource(new File(Path.customElements() + templatefile));
		if (!"".equals(this.template)) {
			this.template_match = this.template_pattern.matcher(this.template);
			try {
				if (template_match.matches()) beforecodelines = this.template_match.group(1).split(Constants.NEWLINE).length;
				else this.global_error = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else global_error = true;

		this.classname = Constants.CUSTOM_ELEMENT_CLASSNAME;
		File tmpDir = new File(Path.temp());
		tmpDir.mkdir();
		this.sourcefile = new File(Path.temp() + this.classname + ".java");
		sourcefile.deleteOnExit();
		new File(Path.temp() + this.classname + ".class").deleteOnExit();
	}

	// compiles the element and returns the new entity if successful
	private CustomElement compile(String code) {
		this.saveJavaSource(code);
		CustomElement entity = null;
		this.compilation_errors.clear();
		try {
			StringWriter compilerErrorMessageSW = new StringWriter(); // catch compiler messages
			PrintWriter compilerErrorMessagePW = new PrintWriter(compilerErrorMessageSW);
			String path = Path.executable();

			String javaVersion = "-\"" + SystemInfo.JAVA_VERSION + "\"";
			String classpath = "-classpath \"" + path + "\"" + File.pathSeparator + "\"" + path + "bin/\"";
			String sourcefile = "\"" + this.sourcefile.getAbsolutePath() + "\"";

			// Compiler Information at http://dev.eclipse.org/viewcvs/index.cgi/jdt-core-home/howto/batch%20compile/batchCompile.html?revision=1.7
			@SuppressWarnings("deprecation")
			boolean compilationSuccessful = org.eclipse.jdt.internal.compiler.batch.Main.compile(
					javaVersion + " " + classpath + " " + sourcefile,
					new PrintWriter(System.out),
					compilerErrorMessagePW);

			if (compilationSuccessful) {
				FileClassLoader fcl = new FileClassLoader(Thread.currentThread().getContextClassLoader());
				Class<?> c = fcl.findClass(this.classname); // load class by type name
				if (c != null) entity = (CustomElement) c.newInstance();
			}
			else {
				this.compilation_errors.addAll(Utils.decomposeStrings(compilerErrorMessageSW.toString()));
			}
		} catch (Exception e) {
			log.error(null, e);
		}
		return entity;
	}

	// loads the source from a file
	private String loadJavaSource(File sourceFile) { // LME3
		String _javaSource = "";
		if ((sourceFile != null) && (sourceFile.getName().endsWith(".java"))) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(sourceFile));
				String line;
				while ((line = br.readLine()) != null)
					_javaSource += line + Constants.NEWLINE;
			} catch (Exception e) {
				log.error(null, e);
			}
		}
		return _javaSource.replaceAll("\r\n", Constants.NEWLINE);
	}

	// saves the source to a file
	private void saveJavaSource(String code) { // LME3
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(sourcefile, false));
			bw.write(this.parseCodeIntoTemplate(code));
			bw.flush();
			bw.close();
		} catch (IOException e) {
			log.error(null, e);
		}
	}

	private String parseCodeFromTemplate(String template) {
		Matcher m = this.template_pattern.matcher(template);
		if (m.matches()) return m.group(3);
		else return "";
	}

	private String parseCodeIntoTemplate(String code) {
		return template_match.group(1).replaceFirst("<!CLASSNAME!>", this.classname) +
				template_match.group(2) +
				code +
				template_match.group(4) +
				template_match.group(5);
	}

	private void handleErrors(String txt, ErrorHandler errorhandler) {
		txt = txt.replaceAll("\r\n", Constants.NEWLINE);
		String[] text = txt.split(Constants.NEWLINE);
		for (int x = 0; x < compilation_errors.size(); x++) {
			Matcher m = this.error_pattern.matcher(compilation_errors.get(x));
			if (m.matches()) {
				int errorLineNr = Integer.parseInt(m.group(1)) - this.beforecodelines;
				x += 3; // The error description is 3 lines later
				String error = compilation_errors.get(x);

				int beftxt = 0, i = 0;
				for (; (i < errorLineNr - 1) && (i < text.length - 1); i++)
					beftxt += text[i].length() + 1;
				if (i < text.length) {
					for (Matcher emptymatcher = this.empty_line.matcher(text[i]); emptymatcher.matches() && (i > 0); i--) {
						beftxt -= text[i - 1].length() + 1;
						emptymatcher = this.empty_line.matcher(text[i - 1]);
					}
					errorhandler.addError(i, error, beftxt, text[i].length());
				}
			}
		}
	}

	public GridElement genEntity(String code, ErrorHandler errorhandler) {
		if (!Constants.enable_custom_elements) return new ErrorOccurred("Custom Elements are disabled\nEnable them in the Options\nOnly open Custom Elements\nfrom trusted sources!");
		if (this.global_error) return new ErrorOccurred();

		if (code == null) code = this.parseCodeFromTemplate(this.template);

		CustomElement element = this.compile(code);
		if (element != null) {
			element.setCode(code);
			return element;
		}
		else if (errorhandler != null) this.handleErrors(code, errorhandler);

		return new ErrorOccurred();
	}

	public GridElement genEntity(String code) {
		return this.genEntity(code, null);
	}

	public GridElement genEntityFromTemplate(String templatename, ErrorHandler errorhandler) {
		String template = this.loadJavaSource(new File(Path.customElements() + templatename + ".java"));
		if (!"".equals(template)) return this.genEntity(this.parseCodeFromTemplate(template), errorhandler);

		return null;
	}
}
