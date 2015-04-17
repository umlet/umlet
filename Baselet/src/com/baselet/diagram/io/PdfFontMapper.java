package com.baselet.diagram.io;

import java.awt.Font;

import com.baselet.control.config.Config;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.pdf.BaseFont;

class PdfFontMapper extends DefaultFontMapper {

	@Override
	public BaseFont awtToPdf(Font font) {
		try {
			Config config = Config.getInstance();
			String fontName;

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

			return BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (Exception e) {
			// Fall back to the default mapper
			return super.awtToPdf(font);
		}
	}

	@Override
	public Font pdfToAwt(BaseFont font, int size) {
		return null;
	}
}