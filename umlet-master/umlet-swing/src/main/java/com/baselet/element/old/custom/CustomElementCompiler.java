package com.baselet.element.old.custom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.config.Config;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Path;
import com.baselet.custom.CompileError;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.element.ErrorOccurred;

public class CustomElementCompiler {

	private static final Logger log = LoggerFactory.getLogger(CustomElementCompiler.class);

	private static CustomElementCompiler compiler;
	private static final String templatefile = "Default.java";
	private final String template;
	private final Pattern template_pattern;
	private Matcher template_match;
	private final String classname;
	private int beforecodelines; // lines of code before the custom code begins (for error processing)
	private List<CompileError> compilation_errors;
	private boolean global_error;

	public static CustomElementCompiler getInstance() {
		if (compiler == null) {
			compiler = new CustomElementCompiler();
		}
		return compiler;
	}

	private final File sourcefile;

	private CustomElementCompiler() {
		global_error = false;
		compilation_errors = new ArrayList<CompileError>();
		beforecodelines = 0;
		template_pattern = Pattern.compile("(.*)(/\\*\\*\\*\\*CUSTOM_CODE START\\*\\*\\*\\*/\n)(.*)(\n\\s\\s/\\*\\*\\*\\*CUSTOM_CODE END\\*\\*\\*\\*/)(.*)", Pattern.DOTALL);
		template = loadJavaSource(new File(Path.customElements() + templatefile));
		if (!"".equals(template)) {
			template_match = template_pattern.matcher(template);
			try {
				if (template_match.matches()) {
					beforecodelines = template_match.group(1).split(Constants.NEWLINE).length;
				}
				else {
					global_error = true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			global_error = true;
		}

		classname = Constants.CUSTOM_ELEMENT_CLASSNAME;
		sourcefile = new File(Path.temp() + classname + ".java");
		sourcefile.deleteOnExit();
		new File(Path.temp() + classname + ".class").deleteOnExit();
	}

	// compiles the element and returns the new entity if successful
	private CustomElement compile(String code) {
		saveJavaSource(code);
		CustomElement entity = null;
		compilation_errors.clear();
		try {
			StringWriter compilerErrorMessageSW = new StringWriter(); // catch compiler messages
			PrintWriter compilerErrorMessagePW = new PrintWriter(compilerErrorMessageSW);
			String javaVersion = "-\"1.6\""; // custom elements use Java6 (previously SystemInfo.JAVA_VERSION, but this only works if the compiler.jar supports the system java version which is not guaranteed)
			String classpath = "-classpath \"" + createClasspath() + "\"";
			String sourcefile = "\"" + this.sourcefile.getAbsolutePath() + "\"";

			// Compiler Information at http://dev.eclipse.org/viewcvs/index.cgi/jdt-core-home/howto/batch%20compile/batchCompile.html?revision=1.7
			boolean compilationSuccessful = BatchCompiler.compile(
					javaVersion + " " + classpath + " " + sourcefile,
					new PrintWriter(System.out),
					compilerErrorMessagePW, null);

			if (compilationSuccessful) {
				FileClassLoader fcl = new FileClassLoader(Thread.currentThread().getContextClassLoader());
				Class<?> c = fcl.findClass(classname); // load class by type name
				if (c != null) {
					entity = (CustomElement) c.newInstance();
				}
			}
			else {
				compilation_errors = CompileError.getListFromString(compilerErrorMessageSW.toString(), beforecodelines);
			}
		} catch (Exception e) {
			log.error(null, e);
		}
		if (entity == null) {
			entity = new CustomElementWithErrors(compilation_errors);
		}
		return entity;
	}

	private String createClasspath() {
		// If the Eclipse Plugin is started from Eclipse (for debugging), the other projects are linked source dirs and therefore all classes are in the same target dir
		if (!Path.executable().endsWith(".jar") && Program.getInstance().getRuntimeType() == RuntimeType.ECLIPSE_PLUGIN) {
			return Path.executable() + "target/classes";
		}
		else {
			return Path.executable() + "\"" + File.pathSeparator + "\"" + Path.executableShared();
		}
	}

	// loads the source from a file
	private String loadJavaSource(File sourceFile) { // LME3
		StringBuilder sb = new StringBuilder("");
		if (sourceFile != null && sourceFile.getName().endsWith(".java")) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(sourceFile));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append(Constants.NEWLINE);
				}
				br.close();
			} catch (Exception e) {
				log.error(null, e);
			}
		}
		return sb.toString().replaceAll("\r\n", Constants.NEWLINE);
	}

	// saves the source to a file
	private void saveJavaSource(String code) { // LME3
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(sourcefile, false));
			bw.write(parseCodeIntoTemplate(code));
			bw.flush();
		} catch (IOException e) {
			log.error(null, e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String parseCodeFromTemplate(String template) {
		Matcher m = template_pattern.matcher(template);
		if (m.matches()) {
			return m.group(3);
		}
		else {
			return "";
		}
	}

	private String parseCodeIntoTemplate(String code) {
		return template_match.group(1).replaceFirst("<!CLASSNAME!>", classname) +
				template_match.group(2) +
				code +
				template_match.group(4) +
				template_match.group(5);
	}

	public GridElement genEntity(String code, ErrorHandler errorhandler) {
		if (!Config.getInstance().isEnable_custom_elements()) {
			String errorMessage = "Custom Elements are disabled\nEnabled them in the Options\nOnly open them from trusted\nsources to avoid malicious code execution!";
			if (SharedConfig.getInstance().isDev_mode()) {
				errorMessage += "\n------------------------------------\n" + code;
			}
			return new ErrorOccurred(errorMessage);
		}
		if (global_error) {
			return new ErrorOccurred();
		}

		if (code == null) {
			code = parseCodeFromTemplate(template);
		}

		CustomElement element = compile(code);
		if (errorhandler != null) {
			errorhandler.addErrors(compilation_errors);
		}
		element.setCode(code);
		return element;
	}

	public GridElement genEntity(String code) {
		return this.genEntity(code, null);
	}

	public GridElement genEntityFromTemplate(String templatename, ErrorHandler errorhandler) {
		String template = loadJavaSource(new File(Path.customElements() + templatename + ".java"));
		if (!"".equals(template)) {
			return this.genEntity(parseCodeFromTemplate(template), errorhandler);
		}

		return null;
	}
}
