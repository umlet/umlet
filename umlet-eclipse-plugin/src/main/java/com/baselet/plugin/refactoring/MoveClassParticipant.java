package com.baselet.plugin.refactoring;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.MoveParticipant;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;

import com.baselet.plugin.UmletPluginUtils;
import com.baselet.plugin.refactoring.JavaDocParser.SourceString;

public class MoveClassParticipant extends MoveParticipant {

	ICompilationUnit cu;

	@Override
	protected boolean initialize(Object element) {
		if (element instanceof ICompilationUnit) {
			cu = (ICompilationUnit) element;
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "Umlet Move Class Participant";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {
		return RefactoringStatus.create(Status.OK_STATUS);
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return null;
	}

	@Override
	public Change createPreChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		CompilationUnitChange change = new CompilationUnitChange(cu.getElementName(), cu);
		change.setKeepPreviewEdits(true);
		change.setEdit(new MultiTextEdit());

		// parse javadocs and update references
		for (ImageReference reference : UmletPluginUtils.collectUxfImgRefs(cu)) {
			SourceString srcValue = reference.srcAttr.value;
			if (UmletPluginUtils.isAbsoluteImageRef(srcValue.getValue())) {
				continue;
			}
			IPackageFragment destinationPackage;
			{
				Object destination = getArguments().getDestination();
				if (!(destination instanceof IPackageFragment)) {
					continue;
				}
				destinationPackage = (IPackageFragment) destination;
			}
			IPath parentPath = UmletPluginUtils.getCompilationUnitParentPath(cu);
			IPath imgPath = parentPath.append(new Path(srcValue.getValue()));
			IPath destinationPath = UmletPluginUtils.getPackageFragmentRootRelativePath(cu.getJavaProject(), destinationPackage.getCorrespondingResource());
			String newPath = UmletPluginUtils.calculateImageRef(destinationPath, imgPath);
			change.addEdit(new ReplaceEdit(srcValue.start, srcValue.length(), newPath));
		}

		return change;
	}

}
