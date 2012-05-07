package umletplugin;

import java.awt.Cursor;

import javax.swing.text.JTextComponent;

import com.baselet.diagram.DrawPanel;
import com.baselet.gui.OwnSyntaxPane;
import com.umlet.custom.CustomElementHandler;

public interface IEditor {

	DrawPanel getDiagram();

	void enableSearch(boolean enable);

	CustomElementHandler getCustomElementHandler();

	String getSelectedPaletteName();

	void selectPalette(String palette);

	void setCustomPanelEnabled(boolean enable);

	void setMailPanelEnabled(boolean enable);

	boolean isMailPanelVisible();

	void setCursor(Cursor cursor);

	void dirtyChanged();

	OwnSyntaxPane getPropertyPane();

	JTextComponent getCustomPane();

	void requestFocus();

	void repaint();

	int getMainSplitLocation();

	int getRightSplitLocation();

	int getMailSplitLocation();

	void fireDirtyProperty();

	void fireDiagramNameChanged();

}
