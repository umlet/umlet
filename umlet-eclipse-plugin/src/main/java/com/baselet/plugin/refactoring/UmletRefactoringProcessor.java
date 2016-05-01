package com.baselet.plugin.refactoring;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;

public interface UmletRefactoringProcessor {

	List<? extends Change> createChange(IProgressMonitor monitor) throws CoreException;
}
