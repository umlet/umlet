package com.baselet.plugin.hyperlink;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

import com.baselet.plugin.UmletPluginUtils;
import com.baselet.plugin.refactoring.JavaDocParser;
import com.baselet.plugin.refactoring.JavaDocParser.HtmlTagAttr;
import com.baselet.plugin.refactoring.JavaDocParser.HtmlTagStartNode;
import com.baselet.plugin.refactoring.JavaDocParser.JavaDocCommentNode;
import com.baselet.plugin.refactoring.JavaDocParser.JavaDocNodeBase;

/**
 * Detector for hyperlinks for image references in javadoc comments. Clicking
 * the link opens the umlet editor.
 */
public class ImageRefHyperlinkDetector extends AbstractHyperlinkDetector {

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		List<IHyperlink> result = new ArrayList<IHyperlink>();

		ITextEditor textEditor = (ITextEditor) getAdapter(ITextEditor.class);
		if (region == null || textEditor == null) {
			return null;
		}

		IEditorSite site = textEditor.getEditorSite();
		if (site == null) {
			return null;
		}

		ICompilationUnit compilationUnit;
		{
			ITypeRoot typeRoot = JavaUI.getEditorInputTypeRoot(textEditor.getEditorInput());
			if (!(typeRoot instanceof ICompilationUnit)) {
				return null;
			}
			compilationUnit = (ICompilationUnit) typeRoot;
		}

		try {
			if (!UmletPluginUtils.hasUmletNature(compilationUnit.getJavaProject())) {
				return null;
			}

			// get all javadocs
			List<ISourceRange> ranges = new ArrayList<ISourceRange>();
			collectIntersectingJavadocRanges(compilationUnit, region, ranges);
			String source = textViewer.getDocument().get();
			for (ISourceRange range : ranges) {
				JavaDocCommentNode comment = new JavaDocParser(source, range.getOffset(), range.getOffset() + range.getLength()).comment();
				for (JavaDocNodeBase child : comment.children) {
					if (!(child instanceof HtmlTagStartNode)) {
						continue;
					}
					final HtmlTagStartNode tag = (HtmlTagStartNode) child;
					if (!"img".equals(tag.tagName.getValue())) {
						continue;
					}
					final Region hyperlinkRegion = new Region(tag.start, tag.length());
					if (!doIntersect(hyperlinkRegion, region)) {
						continue;
					}
					final HtmlTagAttr srcAttr = tag.getAttr("src");
					if (srcAttr == null) {
						continue;
					}
					String src = srcAttr.value.getValue();
					final IFile diagram = UmletPluginUtils.findUmletDiagram(compilationUnit, src);
					if (diagram != null) {
						final String linkText = "Go to " + src;
						result.add(new IHyperlink() {

							@Override
							public void open() {
								IWorkbenchWindow wnd = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
								if (wnd == null) {
									return;
								}
								IWorkbenchPage page = wnd.getActivePage();
								if (page == null) {
									return;
								}
								try {
									IDE.openEditor(page, diagram);
								} catch (PartInitException e) {
									throw new RuntimeException(e);
								}
							}

							@Override
							public String getTypeLabel() {
								return linkText;
							}

							@Override
							public String getHyperlinkText() {
								return linkText;
							}

							@Override
							public IRegion getHyperlinkRegion() {
								return hyperlinkRegion;
							}
						});
					}
				}
			}
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}

		if (result.isEmpty()) {
			return null;
		}
		return result.toArray(new IHyperlink[] {});
	}

	private boolean doIntersect(Region a, IRegion b) {
		return a.getOffset() <= b.getOffset() + b.getLength() && b.getOffset() <= a.getOffset() + a.getLength();
	}

	private void collectIntersectingJavadocRanges(IJavaElement element, IRegion region, List<ISourceRange> elements) throws JavaModelException {
		if (element instanceof IParent) {
			for (IJavaElement child : ((IParent) element).getChildren()) {
				collectIntersectingJavadocRanges(child, region, elements);
			}
		}

		if (element instanceof IMember) {
			ISourceRange range = ((IMember) element).getJavadocRange();
			if (range != null) {
				if (range.getOffset() <= region.getOffset() + region.getLength() && range.getOffset() + range.getLength() >= region.getOffset()) {
					elements.add(range);
				}
			}
		}

	}

}
