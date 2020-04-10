package com.baselet.diagram.io;

import java.awt.Font;

import com.baselet.control.config.Config;
import com.baselet.diagram.Notifier;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.pdf.BaseFont;

class PdfFontMapper extends DefaultFontMapper {

	@Override
	public BaseFont awtToPdf(Font font) {
		String fontName;
		Config config = Config.getInstance();
		// Choose the appropriate PDF export font
		if (font == null) {
			fontName = config.getPdfExportFont();
		}
		else if (font.isBold() && !font.isItalic()) {
			fontName = config.getPdfExportFontBold();
		}
		else if (font.isItalic() && !font.isBold()) {
			fontName = config.getPdfExportFontItalic();
		}
		else if (font.isBold() && font.isItalic()) {
			fontName = config.getPdfExportFontBoldItalic();
		}
		else {
			fontName = config.getPdfExportFont();
		}
		if (fontName == null || fontName.isEmpty()) {
			return super.awtToPdf(font); // user has not specified a font to embedd
		}
		try {
			return BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (Exception e) {
			Notifier.getInstance().showError("Font for PDF invalid (using default instead): " + fontName);
			return super.awtToPdf(font); // Fall back to the default mapper
		}
	}
}