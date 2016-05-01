package com.baselet.plugin.refactoring;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

import com.baselet.plugin.UmletPluginUtils;

/**
 * Participant updating img tags in JavaDocs when a folder is renamed.
 *
 * <p> Does not take part in package renames, this is handled by the {@link RenamePackageParticipant}.
 */
public class RenameFolderParticipant extends RenameParticipant {

	UmletRefactoringProcessorManager mgr = new UmletRefactoringProcessorManager();
	private IFolder renamedFolder;

	@Override
	protected boolean initialize(Object element) {
		// don't participate in package renames
		if ("org.eclipse.jdt.ui.renamePackageProcessor".equals(getProcessor().getIdentifier())) {
			return false;
		}
		if (!(element instanceof IFolder)) {
			return false;
		}
		renamedFolder = (IFolder) element;
		final IPath renamedFolderPath = renamedFolder.getFullPath();
		final IFolder newFolder = renamedFolder.getParent().getFolder(new Path(getArguments().getNewName()));
		IJavaProject javaProject = UmletPluginUtils.getJavaProject(renamedFolder.getProject());
		if (javaProject == null) {
			return false;
		}

		mgr.add(new UpdateImgReferencesProcessor(javaProject) {
			@Override
			protected void calculateDestination(IFile img, ICompilationUnit referencingCompilationUnit, Destination dest) throws CoreException {
				IResource cuResource = referencingCompilationUnit.getCorrespondingResource();
				if (cuResource == null) {
					return;
				}
				if (renamedFolderPath.isPrefixOf(img.getFullPath())) {
					IPath relativePath = img.getFullPath().makeRelativeTo(renamedFolderPath);
					dest.imgFileDestination = newFolder.getFile(relativePath);
				}
			}

			@Override
			protected IFile calculateImgDestination(IFile uxf, ICompilationUnit referencingCompilationUnit) throws JavaModelException {
				throw new UnsupportedOperationException();
			}

		});

		return true;
	}

	@Override
	public String getName() {
		return "Umlet rename folder participant";
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
