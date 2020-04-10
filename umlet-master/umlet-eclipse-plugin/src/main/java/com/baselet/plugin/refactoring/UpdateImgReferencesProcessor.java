package com.baselet.plugin.refactoring;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;

import com.baselet.plugin.UmletPluginUtils;

/**
 * Processor used by multiple refactoring participants to update image references
 * in JavaDoc comments.
 */
public abstract class UpdateImgReferencesProcessor implements UmletRefactoringProcessor {

	private final IJavaProject project;

	protected static class Destination {
		IFile cuDestination;
		IFile imgFileDestination;
	}

	public UpdateImgReferencesProcessor(IJavaProject project) {
		if (project == null) {
			throw new IllegalArgumentException("project may not be null");
		}
		this.project = project;
	}

	@Override
	public List<? extends Change> createChange(IProgressMonitor pm) throws CoreException {
		// calculate target location

		CompositeChange imgRefChange = new CompositeChange("Update <img> references");

		// iterate all Compilation units
		for (ICompilationUnit cu : UmletPluginUtils.collectCompilationUnits(project)) {
			if (!(cu.getCorrespondingResource() instanceof IFile)) {
				continue;
			}
			IFile cuResource = (IFile) cu.getCorrespondingResource();

			if (cu.getBuffer() == null) {
				continue;
			}
			CompilationUnitChange change = null;

			for (ImageReference reference : UmletPluginUtils.collectUxfImgRefs(cu)) {
				IFile originalImg = UmletPluginUtils.getReferencedImgFile(cu, reference.srcAttr.value.getValue());

				Destination dest = new Destination();

				dest.cuDestination = cuResource;
				dest.imgFileDestination = originalImg;

				calculateDestination(originalImg, cu, dest);

				IPath oldImgRelativePath = originalImg.getFullPath().makeRelativeTo(cuResource.getFullPath());
				IPath newImgRelativePath = dest.imgFileDestination.getFullPath().makeRelativeTo(dest.cuDestination.getFullPath());
				if (!oldImgRelativePath.equals(newImgRelativePath)) {
					// the src attribute references the diagram beeing moved, update the reference
					if (change == null) {
						change = new CompilationUnitChange(cu.getElementName(), cu);
						change.setKeepPreviewEdits(true);
						change.setEdit(new MultiTextEdit());
						imgRefChange.add(change);
					}
					IPath destinationImgPath = UmletPluginUtils.getPackageFragmentRootRelativePath(project, dest.imgFileDestination);
					IPath javaResourceParentPath = UmletPluginUtils.getPackageFragmentRootRelativePath(cu.getJavaProject(), dest.cuDestination.getParent());
					String imgRef = UmletPluginUtils.calculateImageRef(javaResourceParentPath, destinationImgPath);
					change.addEdit(new ReplaceEdit(reference.srcAttr.value.start, reference.srcAttr.value.length(), imgRef));
				}
			}
		}
		if (imgRefChange.getChildren().length == 0) {
			return Collections.emptyList();
		}
		return Collections.singletonList(imgRefChange);
	}

	/**
	 * Calculate the destination of the given umlet diagram. Return null if the diagram reference does not need to be updated
	 */
	protected void calculateDestination(IFile uxf, ICompilationUnit referencingCompilationUnit, Destination dest) throws CoreException {
		IFile uxfDest = calculateImgDestination(uxf, referencingCompilationUnit);
		if (uxfDest != null) {
			dest.imgFileDestination = uxfDest;
		}
	}

	/**
	 * Calculate the destination of the given umlet diagram. Return null if the diagram reference does not need to be updated
	 */
	protected abstract IFile calculateImgDestination(IFile uxf, ICompilationUnit referencingCompilationUnit) throws CoreException;

}
