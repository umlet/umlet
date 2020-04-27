package com.baselet.plugin.contentAssist;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.baselet.plugin.UmletPluginUtils;
import com.baselet.plugin.refactoring.JavaDocParser;
import com.baselet.plugin.refactoring.JavaDocParser.HtmlTagAttr;
import com.baselet.plugin.refactoring.JavaDocParser.HtmlTagStartNode;
import com.baselet.plugin.refactoring.JavaDocParser.JavaDocCommentNode;
import com.baselet.plugin.refactoring.JavaDocParser.JavaDocNodeBase;
import com.baselet.plugin.wizard.NewWizard;

/**
 * Proposal computer for various proposals related to image references in javadoc comments.
 */
public class ImgRefProposalComputer implements IJavaCompletionProposalComputer {

	/**
	 * Proposal replacing a piece of text
	 */
	private static class ReplacementProposal implements ICompletionProposal, ICompletionProposalExtension4 {

		private final int replacementOffset;
		private final int replacementLength;
		private final String displayString;
		private final String replacementString;
		private boolean autoInsertable = true;

		public ReplacementProposal(String displayString, String replacementString, int replacementOffset, int replacementLength) {
			this.displayString = displayString;
			this.replacementString = replacementString;
			this.replacementOffset = replacementOffset;
			this.replacementLength = replacementLength;
		}

		@Override
		public void apply(IDocument document) {
			try {
				document.replace(replacementOffset, replacementLength, replacementString);
			} catch (BadLocationException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Point getSelection(IDocument document) {
			return null;
		}

		@Override
		public String getAdditionalProposalInfo() {
			return null;
		}

		@Override
		public String getDisplayString() {
			return displayString;
		}

		@Override
		public Image getImage() {
			return null;
		}

		@Override
		public IContextInformation getContextInformation() {
			return null;
		}

		@Override
		public boolean isAutoInsertable() {
			return autoInsertable;
		}

		public ReplacementProposal setAutoInsertable(boolean autoInsertable) {
			this.autoInsertable = autoInsertable;
			return this;
		}
	}

	@Override
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		ArrayList<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
		System.out.println(context);
		if (context instanceof JavaContentAssistInvocationContext) {
			JavaContentAssistInvocationContext javaContext = (JavaContentAssistInvocationContext) context;
			try {
				if (UmletPluginUtils.hasUmletNature(javaContext.getProject())) {
					IDocument document = context.getDocument();
					if (document != null) {
						computeCompletionProposals(javaContext, document, proposals);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return proposals;
	}

	private void computeCompletionProposals(JavaContentAssistInvocationContext javaContext, IDocument document, ArrayList<ICompletionProposal> proposals) throws CoreException {
		String content = document.get();
		int offset = javaContext.getInvocationOffset();

		// try to get the javadoc of the element at the offset
		IJavaElement elementAt = javaContext.getCompilationUnit().getElementAt(offset);
		if (elementAt instanceof IMember) {
			ISourceRange range = ((IMember) elementAt).getJavadocRange();
			if (range != null) {
				// parse javadoc
				JavaDocCommentNode comment = new JavaDocParser(content, range.getOffset(), range.getOffset() + range.getLength()).comment();

				boolean inStartTag = false;
				// search the html tags
				for (JavaDocNodeBase child : comment.children) {
					if (child instanceof HtmlTagStartNode) {
						HtmlTagStartNode tag = (HtmlTagStartNode) child;
						if (tag.start <= offset && offset < tag.end) {
							// no prefix proposals within start tags
							inStartTag = true;
							if ("img".equals(tag.tagName.getValue())) {
								HtmlTagAttr srcAttr = tag.getAttr("src");
								if (srcAttr != null) {
									addTransformBetweenAbsoluteAndRelativeLinkProposals(javaContext, proposals, srcAttr);
									if (srcAttr.value.start <= offset && offset <= srcAttr.value.end) {
										addChangeSrcToExistingResourceProposals(javaContext, proposals, srcAttr);
									}
								}
							}
							break;
						}
					}
				}

				if (!inStartTag) {
					int lastSpaceIndex = content.lastIndexOf(' ', offset - 1) + 1;
					// skip the leading /**
					int javadocContentStartOffset = range.getOffset() + 3;
					lastSpaceIndex = Math.max(lastSpaceIndex, javadocContentStartOffset);
					if (lastSpaceIndex < offset) {
						String prefix = content.substring(lastSpaceIndex, offset);
						addLinkToExistingResourceProposals(javaContext, proposals, prefix, lastSpaceIndex, offset - lastSpaceIndex);
						addCreateNewImageProposal(javaContext, proposals, prefix, lastSpaceIndex, offset - lastSpaceIndex);
					}
				}
			}
		}
	}

	private void addCreateNewImageProposal(final JavaContentAssistInvocationContext javaContext, ArrayList<ICompletionProposal> proposals, String prefix, int offset, int length) {
		final IContainer parent = UmletPluginUtils.getCompilationUnitParent(javaContext.getCompilationUnit());
		if (parent == null) {
			return;
		}
		// no empty files
		if (prefix.length() == 0) {
			return;
		}
		final IFile uxfFile = parent.getFile(new Path("doc-files/" + prefix + ".uxf"));
		if (uxfFile.exists()) {
			return;
		}

		proposals.add(new ReplacementProposal("Create and link new Umlet diagram doc-files/" + prefix + ".uxf", "<img src=\"doc-files/" + prefix + ".png\" alt=\"\">", offset, length) {
			@Override
			public void apply(IDocument document) {
				super.apply(document);

				try {
					// create doc-files folder
					IFolder docFiles = parent.getFolder(new Path("doc-files"));
					if (!docFiles.exists()) {
						docFiles.create(true, true, null);
					}

					// create image file
					{
						InputStream stream = null;
						try {
							stream = NewWizard.openContentStream();

							uxfFile.create(stream, true, null);

							stream.close();
						} catch (IOException e) {
							// ignore
						} finally {
							if (stream != null) {
								try {
									stream.close();
								} catch (IOException e) {
									// swallow
								}
							}
						}
					}

					// open editor
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IDE.openEditor(page, uxfFile, true);
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
			}
		}.setAutoInsertable(false));
	}

	private void addChangeSrcToExistingResourceProposals(JavaContentAssistInvocationContext javaContext, ArrayList<ICompletionProposal> proposals, HtmlTagAttr srcAttr) throws CoreException {
		String src = srcAttr.value.getValue();
		// add proposals for resource links
		{
			int prefixStart = src.lastIndexOf('/') + 1;
			int prefixEnd = javaContext.getInvocationOffset() - srcAttr.value.start;
			if (prefixEnd >= 0 && prefixStart < prefixEnd && prefixEnd <= src.length()) {
				String prefix = src.substring(prefixStart, prefixEnd);
				for (String path : collectResourcePaths(javaContext, prefix)) {
					// skip if no change would result
					if (src.equals(path)) {
						continue;
					}
					proposals.add(new ReplacementProposal("Change link to " + path, path, srcAttr.value.start, srcAttr.value.length()));
				}
			}
		}
	}

	private void addTransformBetweenAbsoluteAndRelativeLinkProposals(JavaContentAssistInvocationContext javaContext, ArrayList<ICompletionProposal> proposals, HtmlTagAttr srcAttr) throws JavaModelException {
		final IPath javaResourceParentPath = UmletPluginUtils.getCompilationUnitParentPath(javaContext.getCompilationUnit());
		if (javaResourceParentPath == null) {
			return;
		}

		String src = srcAttr.value.getValue();
		if (UmletPluginUtils.isAbsoluteImageRef(src)) {
			// propose to transform to relative
			Path path = new Path(src.substring("{@docRoot}".length()));
			String replacement = path.makeRelativeTo(javaResourceParentPath).toString();
			proposals.add(new ReplacementProposal("Transform src to " + replacement, replacement, srcAttr.value.start, srcAttr.value.length()).setAutoInsertable(false));
		}
		else {
			// propose to transform to absolute
			String replacement = "{@docRoot}/" + javaResourceParentPath.append(src).toString();
			proposals.add(new ReplacementProposal("Transform src to " + replacement, replacement, srcAttr.value.start, srcAttr.value.length()).setAutoInsertable(false));
		}
	}

	private List<String> collectResourcePaths(final JavaContentAssistInvocationContext context, final String prefix) throws CoreException {
		final ArrayList<String> result = new ArrayList<String>();
		final IPath javaResourceParentPath = UmletPluginUtils.getCompilationUnitParentPath(context.getCompilationUnit());
		if (javaResourceParentPath == null) {
			return result;
		}

		IPackageFragmentRoot root = UmletPluginUtils.getPackageFragmentRoot(context.getCompilationUnit());

		final IResource rootResource = root.getCorrespondingResource();
		if (rootResource == null) {
			return result;
		}

		rootResource.accept(new IResourceVisitor() {
			@Override
			public boolean visit(IResource uxfResource) throws CoreException {
				if (!uxfResource.isAccessible()) {
					return false;
				}
				if (uxfResource instanceof IFile) {
					if (uxfResource.getName().endsWith(".uxf") && uxfResource.getName().toLowerCase(Locale.ENGLISH).contains(prefix.toLowerCase(Locale.ENGLISH))) {
						IFile imgFile = UmletPluginUtils.getImageForUxfPath((IFile) uxfResource);
						IPath imgPath = imgFile.getProjectRelativePath().makeRelativeTo(rootResource.getProjectRelativePath());
						result.add(UmletPluginUtils.calculateImageRef(javaResourceParentPath, imgPath));
					}
				}
				return true;
			}

		});
		return result;
	}

	private void addLinkToExistingResourceProposals(final JavaContentAssistInvocationContext context, final ArrayList<ICompletionProposal> proposals, final String prefix, final int inputOffset, final int inputLength) throws CoreException {
		List<String> collectResourcePaths = collectResourcePaths(context, prefix);
		for (int i = 0; i < collectResourcePaths.size() && i < 50; i++) {
			String path = collectResourcePaths.get(i);
			proposals.add(new ReplacementProposal("Link to " + path, "<img src=\"" + path + "\" alt=\"\">", inputOffset, inputLength));
		}
	}

	@Override
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		return Collections.emptyList();
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public void sessionStarted() {}

	@Override
	public void sessionEnded() {}

}
