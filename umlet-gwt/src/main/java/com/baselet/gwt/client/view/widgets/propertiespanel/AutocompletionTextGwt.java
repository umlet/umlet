package com.baselet.gwt.client.view.widgets.propertiespanel;

import com.baselet.diagram.draw.helper.theme.Theme;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.gui.AutocompletionText;
import com.baselet.gwt.client.base.Converter;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;

public class AutocompletionTextGwt extends AutocompletionText {
    CustomLogger logger = CustomLoggerFactory.getLogger(AutocompletionTextGwt.class);
    public AutocompletionTextGwt(String text, String info) {
        super(text, info);
    }

    public AutocompletionTextGwt(String text, String info, String base64Img) {
        super(text, info, base64Img);
    }

    public String getHtmlInfo() {
        String textColor = Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_FOREGROUND)).value();
        String baseText = getText() + " <span style='font-style:italic;color:" + textColor + "'>" + getInfo() + "</span>";
        if (base64Img != null) {
            baseText += " <img src='data:image/gif;base64," + base64Img + "'>";
        }
        return baseText;
    }
}
