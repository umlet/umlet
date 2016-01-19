package com.baselet.plugin.refactoring;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

import com.baselet.plugin.UmletPluginUtils;

/**
 * Participant updating img tags in JavaDocs when diagram resources are renamed
 */
public class RenameFileParticipant extends RenameParticipant {

	UmletRefactoringProcessorManager mgr = new UmletRefactoringProcessorManager();
	private IFile origFile;

	@Override
	protected boolean initialize(Object element) {
		if (!(element instanceof IFile)) {
			return false;
		}

		origFile = (IFile) element;

		if (!origFile.exists() || !"uxf".equals(origFile.getFileExtension())) {
			return false;
		}
		IJavaProject javaProject = UmletPluginUtils.getJavaProject(origFile.getProject());
		if (javaProject == null) {
			return false;
		}

		mgr.add(new UpdateImgReferencesProcessor(javaProject) {

			@Override
			protected IFile calculateImgDestination(IFile img, ICompilationUnit referencingCompilationUnit) {
				IFile uxfFile = UmletPluginUtils.getUxfDiagramForImgFile(img);
				if (origFile.equals(uxfFile)) {
					return origFile.getParent().getFile(new Path(getArguments().getNewName()).removeFileExtension().addFileExtension(img.getFileExtension()));
				}
				return null;
			}
		});
		mgr.add(new RenamePngProcessor(origFile) {

			@Override
			protected String getTargetname(IFile pngFile, IFile affectedDiagram) {
				return new Path(getArguments().getNewName()).removeFileExtension().addFileExtension(pngFile.getFileExtension()).lastSegment();
			}
		});

		return true;
	}

	@Override
	public String getName() {
		return "Umlet rename resouce participant";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {
		return RefactoringStatus.create(Status.OK_STATUS);
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return mgr.createChange(pm);
	}

}
