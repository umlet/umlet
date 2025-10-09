package com.baselet.control.config;

import java.awt.Font;
import java.awt.Point;
import java.io.File;

import javax.swing.UIManager;

import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.constants.Constants;
import com.baselet.control.constants.SystemInfo;
import com.baselet.control.enums.Os;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.util.logging.Logger;
import com.baselet.util.logging.LoggerFactory;

public class Config {

	private static Logger log = LoggerFactory.getLogger(Config.class);

	private static Config instance = new Config();

	public static Config getInstance() {
		return instance;
	}

	private final String DEFAULT_FILE_HOME = System.getProperty("user.dir");

	private String uiManager;
	private String openFileHome = DEFAULT_FILE_HOME;
	private String saveFileHome = DEFAULT_FILE_HOME;
	private String programVersion;

	private String lastUsedPalette = ""; // default is empty string not null because null cannot be stored as property
	private String pdfExportFont = ""; // eg in Windows: "pdf_export_font = c:/windows/fonts/msgothic.ttc,1"
	private String pdfExportFontBold = "";
	private String pdfExportFontItalic = "";
	private String pdfExportFontBoldItalic = "";
	private String lastExportFormat = "";
	private boolean checkForUpdates = true;
	private int printPadding = 20;
	private Point program_location = new Point(5, 5);
	private Dimension program_size = new Dimension(960, 750);
	private int mail_split_position = 250;
	private int right_split_position = 400;
	private int main_split_position = 600;
	private boolean enable_custom_elements = true;
	private boolean show_grid = false;
	private boolean start_maximized = false;
	private boolean secureXmlProcessing = true;
	private String defaultFontFamily = Font.SANS_SERIF;
	private Integer defaultFontsize = 14;
	private Integer propertiesPanelFontsize = 11;
	private boolean propertiesPanelLineWrap = false;
	private boolean propertiesPanelLineNumbers = false;
	private String uiScale = "Default";
	private Integer exportScale = 1;
	private Integer exportDpi = null;

	public void initUiManager() {
		// The default MacOS theme looks ugly, therefore we set metal
		if (SystemInfo.OS == Os.MAC) {
			uiManager = "javax.swing.plaf.metal.MetalLookAndFeel";
			addFlatLafThemes();
		}
		else if (Program.getInstance().getRuntimeType() == RuntimeType.ECLIPSE_PLUGIN && UIManager.getSystemLookAndFeelClassName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
			uiManager = "javax.swing.plaf.metal.MetalLookAndFeel";
		}
		else {
			uiManager = UIManager.getSystemLookAndFeelClassName();
			addFlatLafThemes();
		}
	}

	private void addFlatLafThemes() {
		UIManager.installLookAndFeel("Flat Darcula", Constants.FLAT_DARCULA_THEME);
		UIManager.installLookAndFeel("Flat Light", Constants.FLAT_LIGHT_THEME);
	}

	public String getUiManager() {
		return uiManager;
	}

	public void setUiManager(String uiManager) {
		this.uiManager = uiManager;
	}

	public String getOpenFileHome() {
		return getFileHelper(openFileHome, DEFAULT_FILE_HOME);
	}

	public void setOpenFileHome(String openFileHome) {
		log.trace("setting openFileHome path to: " + openFileHome);
		this.openFileHome = openFileHome;
	}

	public String getSaveFileHome() {
		return getFileHelper(saveFileHome, DEFAULT_FILE_HOME);
	}

	private String getFileHelper(String fileLocToCheck, String defaultValue) {
		try {
			if (new File(fileLocToCheck).exists()) {
				return fileLocToCheck;
			}
		} catch (Exception e) {/* nothing to do */}

		// if stored location doesn't exist or there is an exception while accessing the location, return default value
		return defaultValue;
	}

	public void setSaveFileHome(String saveFileHome) {
		log.trace("setting saveFileHome path to: " + saveFileHome);
		this.saveFileHome = saveFileHome;
	}

	public void setProgramVersion(String cfgVersion) {
		programVersion = cfgVersion;
	}

	public String getProgramVersion() {
		return programVersion;
	}

	public String getLastUsedPalette() {
		return lastUsedPalette;
	}

	public void setLastUsedPalette(String lastUsedPalette) {
		this.lastUsedPalette = lastUsedPalette;
	}

	public String getPdfExportFont() {
		return pdfExportFont;
	}

	public void setPdfExportFont(String pdfExportFont) {
		this.pdfExportFont = pdfExportFont;
	}

	public String getPdfExportFontBold() {
		return pdfExportFontBold;
	}

	public void setPdfExportFontBold(String pdfExportFontBold) {
		this.pdfExportFontBold = pdfExportFontBold;
	}

	public String getPdfExportFontItalic() {
		return pdfExportFontItalic;
	}

	public void setPdfExportFontItalic(String pdfExportFontItalic) {
		this.pdfExportFontItalic = pdfExportFontItalic;
	}

	public String getPdfExportFontBoldItalic() {
		return pdfExportFontBoldItalic;
	}

	public void setPdfExportFontBoldItalic(String pdfExportFontBoldItalic) {
		this.pdfExportFontBoldItalic = pdfExportFontBoldItalic;
	}

	public String getLastExportFormat() {
		return lastExportFormat;
	}

	public void setLastExportFormat(String lastExportFormat) {
		this.lastExportFormat = lastExportFormat;
	}

	public boolean isCheckForUpdates() {
		return checkForUpdates;
	}

	public void setCheckForUpdates(boolean checkForUpdates) {
		this.checkForUpdates = checkForUpdates;
	}

	public boolean isSecureXmlProcessing() {
		return secureXmlProcessing;
	}

	public void setSecureXmlProcessing(boolean secureXmlProcessing) {
		this.secureXmlProcessing = secureXmlProcessing;
	}

	public int getPrintPadding() {
		return printPadding;
	}

	public void setPrintPadding(int printPadding) {
		this.printPadding = printPadding;
	}

	public Point getProgram_location() {
		return program_location;
	}

	public void setProgram_location(Point program_location) {
		this.program_location = program_location;
	}

	public Dimension getProgram_size() {
		return program_size;
	}

	public void setProgram_size(Dimension program_size) {
		this.program_size = program_size;
	}

	public int getMail_split_position() {
		return mail_split_position;
	}

	public void setMail_split_position(int mail_split_position) {
		this.mail_split_position = mail_split_position;
	}

	public int getRight_split_position() {
		return right_split_position;
	}

	public void setRight_split_position(int right_split_position) {
		this.right_split_position = right_split_position;
	}

	public int getMain_split_position() {
		return main_split_position;
	}

	public void setMain_split_position(int main_split_position) {
		this.main_split_position = main_split_position;
	}

	public boolean isEnable_custom_elements() {
		return enable_custom_elements;
	}

	public void setEnable_custom_elements(boolean enable_custom_elements) {
		this.enable_custom_elements = enable_custom_elements;
	}

	public boolean isShow_grid() {
		return show_grid;
	}

	public void setShow_grid(boolean show_grid) {
		this.show_grid = show_grid;
	}

	public boolean isStart_maximized() {
		return start_maximized;
	}

	public void setStart_maximized(boolean start_maximized) {
		this.start_maximized = start_maximized;
	}

	public String getDefaultFontFamily() {
		return defaultFontFamily;
	}

	public void setDefaultFontFamily(String defaultFontFamily) {
		this.defaultFontFamily = defaultFontFamily;
	}

	public Integer getDefaultFontsize() {
		return defaultFontsize;
	}

	public void setDefaultFontsize(Integer defaultFontsize) {
		this.defaultFontsize = defaultFontsize;
	}

	public Integer getPropertiesPanelFontsize() {
		return propertiesPanelFontsize;
	}

	public void setPropertiesPanelFontsize(Integer propertiesPanelFontsize) {
		this.propertiesPanelFontsize = propertiesPanelFontsize;
	}

	public boolean isPropertiesPanelLineWrap() {
		return propertiesPanelLineWrap;
	}

	public void setPropertiesPanelLineWrap(boolean propertiesPanelLineWrap) {
		this.propertiesPanelLineWrap = propertiesPanelLineWrap;
	}

	public boolean isPropertiesPanelLineNumbers() {
		return propertiesPanelLineNumbers;
	}

	public void setPropertiesPanelLineNumbers(boolean propertiesPanelLineNumbers) {
		this.propertiesPanelLineNumbers = propertiesPanelLineNumbers;
	}

	public String getUiScale() {
		return uiScale;
	}

	public void setUiScale(String uiScale) {
		this.uiScale = uiScale;
	}

	public Integer getExportScale() {
		return exportScale;
	}

	public void setExportScale(Integer exportScale) {
		this.exportScale = exportScale;
	}

	public Integer getExportDpi() {
		return exportDpi;
	}

	public void setExportDpi(Integer exportDpi) {
		this.exportDpi = exportDpi;
	}
}
