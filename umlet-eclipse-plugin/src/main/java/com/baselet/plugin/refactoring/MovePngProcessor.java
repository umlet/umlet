package com.baselet.plugin.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.resource.MoveResourceChange;

public abstract class MovePngProcessor implements UmletRefactoringProcessor {

	private final IFile uxfDiagram;

	public MovePngProcessor(IFile uxfDiagram) {
		this.uxfDiagram = uxfDiagram;
	}

	@Override
	public List<Change> createChange(IProgressMonitor pm) {
		List<Change> result = new ArrayList<Change>();
		// move img files with the diagram
		IContainer parent = uxfDiagram.getParent();
		if (parent != null) {
			IFile pngFile = uxfDiagram.getProject().getFile(uxfDiagram.getProjectRelativePath().removeFileExtension().addFileExtension("png"));
			if (pngFile.exists()) {
				IContainer destinationFolder = getDestinationFolder(pngFile, uxfDiagram);
				if (destinationFolder != null) {
					result.add(new MoveResourceChange(pngFile, destinationFolder));
				}
			}
		}

		return result;
	}

	protected abstract IContainer getDestinationFolder(IFile pngFile, IFile affectedDiagram);
}
