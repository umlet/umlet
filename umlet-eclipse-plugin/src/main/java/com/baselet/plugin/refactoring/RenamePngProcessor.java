package com.baselet.plugin.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceChange;

public abstract class RenamePngProcessor implements UmletRefactoringProcessor {

	private final IFile affectedDiagram;

	public RenamePngProcessor(IFile affectedDiagram) {
		this.affectedDiagram = affectedDiagram;
	}

	@Override
	public List<? extends Change> createChange(IProgressMonitor monitor) throws CoreException {
		List<Change> result = new ArrayList<Change>();
		// rename img files with the diagram
		IContainer parent = affectedDiagram.getParent();
		if (parent != null) {
			IFile pngFile = affectedDiagram.getProject().getFile(affectedDiagram.getProjectRelativePath().removeFileExtension().addFileExtension("png"));
			if (pngFile.exists()) {
				result.add(new RenameResourceChange(pngFile.getFullPath(), getTargetname(pngFile, affectedDiagram)));
			}
		}
		return result;
	}

	protected abstract String getTargetname(IFile pngFile, IFile affectedDiagram);
}
