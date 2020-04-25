package com.baselet.plugin.refactoring;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.IResourceMapper;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

import com.baselet.plugin.UmletPluginUtils;

/**
 * Participant updating img tags in JavaDocs when a package is renamed.
 */
public class RenamePackageParticipant extends RenameParticipant {

	UmletRefactoringProcessorManager mgr = new UmletRefactoringProcessorManager();

	@Override
	protected boolean initialize(Object element) {
		if (!(element instanceof IPackageFragment)) {
			return false;
		}
		IPackageFragment packageFragment = (IPackageFragment) element;
		IJavaProject javaProject = packageFragment.getJavaProject();
		if (javaProject == null) {
			return false;
		}
		final IPackageFragmentRoot packageFragmentRoot = UmletPluginUtils.getPackageFragmentRoot(packageFragment);
		if (packageFragmentRoot == null) {
			return false;
		}
		if (!(getProcessor() instanceof IResourceMapper)) {
			return false;
		}
		final IResourceMapper resourceMapper = (IResourceMapper) getProcessor();

		mgr.add(new UpdateImgReferencesProcessor(javaProject) {

			@Override
			protected void calculateDestination(IFile img, ICompilationUnit referencingCompilationUnit, Destination dest) throws CoreException {
				dest.cuDestination = (IFile) resourceMapper.getRefactoredResource(referencingCompilationUnit.getCorrespondingResource());

				// Search the first parent resource of the file which has a corresponding java element.
				// Must be a package or the root.
				IJavaElement parentElement;
				IResource parent;
				{
					parent = img;
					do {
						parent = parent.getParent();
						parentElement = JavaCore.create(parent);
					} while (parentElement == null && parent != null);
				}
				if (parentElement != null && parent != null) {
					IResource refactoredParent = resourceMapper.getRefactoredResource(parent);
					if (refactoredParent instanceof IFolder) {
						dest.imgFileDestination = ((IFolder) refactoredParent).getFile(img.getFullPath().makeRelativeTo(parent.getFullPath()));
					}
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
		return "Umlet Rename Package Participant";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {
		return RefactoringStatus.create(Status.OK_STATUS);
	}

	@Override
	public Change createPreChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return mgr.createChange(pm);
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return null;
	}

}
