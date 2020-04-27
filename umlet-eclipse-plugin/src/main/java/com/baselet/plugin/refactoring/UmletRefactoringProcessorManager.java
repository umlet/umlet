package com.baselet.plugin.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;

/**
 * Base class for re
 */
public class UmletRefactoringProcessorManager {

	private final List<UmletRefactoringProcessor> processors = new ArrayList<UmletRefactoringProcessor>();
	private final List<Change> additionalChanges = new ArrayList<Change>();

	public void add(UmletRefactoringProcessor processor) {
		processors.add(processor);
	}

	public void add(Change change) {
		additionalChanges.add(change);
	}

	public Change createChange(IProgressMonitor pm) throws CoreException {
		CompositeChange change = new CompositeChange("Umlet");
		for (UmletRefactoringProcessor processor : processors) {
			List<? extends Change> changes = processor.createChange(pm);
			if (changes != null) {
				change.addAll(changes.toArray(new Change[] {}));
			}
		}
		change.addAll(additionalChanges.toArray(new Change[] {}));

		if (change.getChildren().length == 0) {
			return null;
		}
		else {
			return change;
		}
	}
}
