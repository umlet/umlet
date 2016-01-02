package com.baselet.standalone;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.io.Files;

public class MainBatchmodeTest {

	static String TEST_FILE_LOCATION; // the testfile is a CustomElement with custom fontsize and fontfamily to simulate a special edge case

	@Rule
	public TemporaryFolder tmpDir = new TemporaryFolder();

	@BeforeClass
	public static void beforeClass() throws URISyntaxException {
		// Skip tests on Travis CI because they require a local Umlet installation (e.g. for umlet.cfg file)
		Assume.assumeFalse("If built is run on TRAVIS CI Server, skip these tests", "TRUE".equalsIgnoreCase(System.getenv("TRAVIS")));
		TEST_FILE_LOCATION = MainBatchmodeTest.class.getProtectionDomain().getCodeSource().getLocation().toURI().getSchemeSpecificPart() + MainBatchmodeTest.class.getCanonicalName().replace(".", "/").replace(MainBatchmodeTest.class.getSimpleName(), "");
	}

	@Test
	public void batchConvertToPng_wildcardAndNoOutput() throws Exception {
		File copy = copyInputToTmp();
		String wildcard = copy.getParent().toString() + "/*";
		MainStandalone.main(new String[] { "-action=convert", "-format=png", "-filename=" + wildcard });
		assertFilesEqual(new File(TEST_FILE_LOCATION + "out.png"), new File(copy + "." + "png"));
	}

	private void assertFilesEqual(File expected, File actual) throws IOException {
		assertTrue("The content of both files must match. Expected" + expected + ", Actual: " + actual, Files.equal(expected, actual));
	}

	@Test
	public void batchConvertToSvg_specificInputAndOutputFile() throws Exception {
		// whitespaces must be trimmed for the test (SVG is an XML based format and whitespaces can be different)
		File expected = changeLines(new File(TEST_FILE_LOCATION + "out.svg"), null, true);
		File actual = changeLines(createOutputfile("svg"), null, true);
		assertFilesEqual(expected, actual);
	}

	@Test
	public void batchConvertToEps_specificInputAndOutputFile() throws Exception {
		// eps files contain the CreationDate which must be removed before the comparison
		File output = changeLines(createOutputfile("eps"), "%%CreationDate", false);
		File outWithoutCreated = changeLines(copyToTmp("out.eps"), "%%CreationDate", false);
		assertFilesEqual(outWithoutCreated, output);
	}

	private File changeLines(File output, String excludedPrefix, boolean streamlineWhitespaces) throws FileNotFoundException, IOException {
		FileReader fr = new FileReader(output);
		String s;
		String totalStr = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(fr);
			while ((s = br.readLine()) != null) {
				if (excludedPrefix != null && !s.startsWith(excludedPrefix)) {
					totalStr += s + System.getProperty("line.separator");
				}
			}
			FileWriter fw = new FileWriter(output);
			if (streamlineWhitespaces) {
				totalStr = totalStr.replaceAll("\\s+", " ");
			}
			fw.write(totalStr);
			fw.close();
			return output;
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	private File createOutputfile(String format) throws IOException {
		String outputFileLoc = tmpDirString() + "bla." + format;
		MainStandalone.main(new String[] { "-action=convert", "-format=" + format, "-filename=" + copyInputToTmp(), "-output=" + outputFileLoc });
		File outputFile = new File(outputFileLoc);
		return outputFile;
	}

	private File copyInputToTmp() throws IOException {
		return copyToTmp("in.uxf");
	}

	private File copyToTmp(String file) throws IOException {
		File newFile = tmpDir.newFile();
		Files.copy(new File(TEST_FILE_LOCATION + file), newFile);
		return newFile;
	}

	private String tmpDirString() {
		return tmpDir.getRoot().toURI().getSchemeSpecificPart();
	}

}
