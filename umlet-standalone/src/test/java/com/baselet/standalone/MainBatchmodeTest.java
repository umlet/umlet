package com.baselet.standalone;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.io.Files;

/**
 * Test several different Batch exports
 * Currently pdf is not tested because it's hard to do a visual comparison of a pdf. svg and eps are cleaned up to make this comparison work
 * To let those tests work on build servers such as Travis CI, only svg uses text in the uxf files (because only svg stores the information system-neutral, eps and png seem to look different on different systems which makes the validation of the output too complex)
 */
public class MainBatchmodeTest {

	static String TEST_FILE_LOCATION; // the testfile is a CustomElement with custom fontsize and fontfamily to simulate a special edge case

	@Rule
	public TemporaryFolder tmpDir = new TemporaryFolder();

	@BeforeClass
	public static void beforeClass() throws URISyntaxException {
		TEST_FILE_LOCATION = MainBatchmodeTest.class.getProtectionDomain().getCodeSource().getLocation().toURI().getSchemeSpecificPart() + MainBatchmodeTest.class.getCanonicalName().replace(".", "/").replace(MainBatchmodeTest.class.getSimpleName(), "");
	}

	@Test
	public void batchConvertToSvg_diagramSpecificFontSizeAndFamily() throws Exception {
		// whitespaces must be trimmed for the test (SVG is an XML based format and whitespaces can be different)
		File expected = changeLines(new File(TEST_FILE_LOCATION + "out_diagramFontSizeAndFamily.svg"), null, true);
		File actual = changeLines(createOutputfile("svg", "in_diagramFontSizeAndFamily.uxf"), null, true);
		assertFilesEqual(expected, actual);
	}

	@Test
	public void batchConvertToSvg_diagramSpecificFontSizeAndFamilyxxx() throws Exception {
		// whitespaces must be trimmed for the test (SVG is an XML based format and whitespaces can be different)
		File expected = changeLines(new File(TEST_FILE_LOCATION + "out_newAllInOne.svg"), null, true);
		File actual = changeLines(createOutputfile("svg", "in_newAllInOne.uxf"), null, true);
		assertFilesEqual(expected, actual);
	}

	@Test
	public void batchConvertToPng_wildcardAndNoOutputParam_newCustomElement() throws Exception {
		File copy = copyInputToTmp("in_newCustomElement.uxf");
		String wildcard = copy.getParent().toString() + "/*";
		MainStandalone.main(new String[] { "-action=convert", "-format=png", "-filename=" + wildcard });
		assertImageEqual(new File(TEST_FILE_LOCATION + "out_newCustomElement.png"), new File(copy + "." + "png"));
	}

	@Test
	public void batchConvertToEps_newCustomElement() throws Exception {
		// eps files contain the CreationDate which must be removed before the comparison
		File output = changeLines(createOutputfile("eps", "in_newCustomElement.uxf"), "%%CreationDate", false);
		File outWithoutCreated = changeLines(copyToTmp("out_newCustomElement.eps"), "%%CreationDate", false);
		assertFilesEqual(outWithoutCreated, output);
	}

	private void assertImageEqual(File expected, File actual) throws IOException {
		BufferedImage expectedPicture = ImageIO.read(expected);
		BufferedImage actualPicture = ImageIO.read(actual);

		int expectedHeight = expectedPicture.getHeight();
		int expectedWidth = expectedPicture.getWidth();
		String expSize = Integer.toString(expectedWidth) + "x" + Integer.toString(expectedHeight);
		String actSize = Integer.toString(actualPicture.getWidth()) + "x" + Integer.toString(actualPicture.getHeight());
		assertTrue("The size of the images " + expected + " and " + actual + " must match. Expected: " + expSize + ", Actual: " + actSize, expSize.equals(actSize));

		for (int y = 0; y < expectedHeight; y++) {
			for (int x = 0; x < expectedWidth; x++) {
				assertTrue("The images " + expected + " and " + actual + " don't match in the pixel (" + x + "/" + y + ")", expectedPicture.getRGB(x, y) == actualPicture.getRGB(x, y));
			}
		}
	}

	private void assertFilesEqual(File expected, File actual) throws IOException {
		assertTrue("The content of both files must match. Expected: " + expected + ", Actual: " + actual, Files.equal(expected, actual));
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

	private File createOutputfile(String format, String inputFilename) throws IOException {
		String outputFileLoc = tmpDirString() + "bla." + format;
		MainStandalone.main(new String[] { "-action=convert", "-format=" + format, "-filename=" + copyInputToTmp(inputFilename), "-output=" + outputFileLoc });
		File outputFile = new File(outputFileLoc);
		return outputFile;
	}

	private File copyInputToTmp(String inputFilename) throws IOException {
		return copyToTmp(inputFilename);
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
