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
import org.eclipse.ltk.core.refactoring.resource.RenameResourceChange;

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

		if (!origFile.exists()) {
			return false;
		}

		IJavaProject javaProject = UmletPluginUtils.getJavaProject(origFile.getProject());
		if (javaProject == null) {
			return false;
		}

		if ("uxf".equals(origFile.getFileExtension())) {
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

		if ("png".equals(origFile.getFileExtension())) {
			IFile correspondingUxf = UmletPluginUtils.getUxfDiagramForImgFile(origFile);
			if (!correspondingUxf.exists()) {
				// only refactor if uxf file exists
				return false;
			}

			// update references to the renamed png
			mgr.add(new UpdateImgReferencesProcessor(javaProject) {

				@Override
				protected IFile calculateImgDestination(IFile img, ICompilationUnit referencingCompilationUnit) {
					if (origFile.equals(img)) {
						return origFile.getParent().getFile(new Path(getArguments().getNewName()));
					}
					return null;
				}
			});

			// update the corresponding .uxf file
			mgr.add(new RenameResourceChange(correspondingUxf.getFullPath(),
					new Path(getArguments().getNewName()).removeFileExtension().addFileExtension(correspondingUxf.getFileExtension()).lastSegment()));

			return true;
		}
		return false;
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
