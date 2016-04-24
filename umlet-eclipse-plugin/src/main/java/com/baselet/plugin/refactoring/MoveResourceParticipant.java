package com.baselet.plugin.refactoring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.ISharableParticipant;
import org.eclipse.ltk.core.refactoring.participants.MoveArguments;
import org.eclipse.ltk.core.refactoring.participants.MoveParticipant;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.ltk.core.refactoring.resource.MoveResourceChange;

import com.baselet.plugin.UmletPluginUtils;

/**
 * Participant updating img tags in JavaDocs when a diagram file is moved.
 */
public class MoveResourceParticipant extends MoveParticipant implements ISharableParticipant {

	private static class Entry {
		Object element;
		MoveArguments args;

		public Entry(Object element, MoveArguments args) {
			super();
			this.element = element;
			this.args = args;
		}

	}

	private final List<Entry> entries = new ArrayList<MoveResourceParticipant.Entry>();

	@Override
	protected boolean initialize(Object element) {
		// don't participate in package renames
		if ("org.eclipse.jdt.ui.renamePackageProcessor".equals(getProcessor().getIdentifier())) {
			return false;
		}

		entries.add(new Entry(element, getArguments()));
		return true;
	}

	@Override
	public String getName() {
		return "Umlet Move Resource Participant";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {
		return RefactoringStatus.create(Status.OK_STATUS);
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		final Set<Object> affectedElements = new HashSet<Object>();
		for (Entry entry : entries) {
			affectedElements.add(entry.element);
		}

		UmletRefactoringProcessorManager mgr = new UmletRefactoringProcessorManager();
		for (Entry entry : entries) {
			Object element = entry.element;
			if (!(element instanceof IResource)) {
				continue;
			}

			IResource origResource = (IResource) element;
			final IFolder destinationFolder;
			{
				Object destination = entry.args.getDestination();
				if (!(destination instanceof IFolder)) {
					continue;
				}
				destinationFolder = (IFolder) destination;
			}

			IJavaProject javaProject = UmletPluginUtils.getJavaProject(origResource.getProject());
			if (javaProject == null) {
				continue;
			}

			if (element instanceof IFile) {
				final IFile origFile = (IFile) element;
				if (!origFile.exists()) {
					continue;
				}

				if ("uxf".equals(origFile.getFileExtension())) {
					// we're moving the diagram
					mgr.add(new UpdateImgReferencesProcessor(javaProject) {

						@Override
						protected IFile calculateImgDestination(IFile img, ICompilationUnit referencingCompilationUnit) {
							if (affectedElements.contains(img)) {
								return null;
							}
							IFile uxfFile = UmletPluginUtils.getUxfDiagramForImgFile(img);
							if (origFile.equals(uxfFile)) {
								return destinationFolder.getFile(img.getName());
							}
							return null;
						}
					});
					mgr.add(new MovePngProcessor(origFile) {

						@Override
						protected IContainer getDestinationFolder(IFile pngFile, IFile affectedDiagram) {
							if (affectedElements.contains(pngFile)) {
								return null;
							}
							return destinationFolder;
						}
					});
				}
				else if ("png".equals(origFile.getFileExtension())) {
					IFile uxfFile = UmletPluginUtils.getUxfDiagramForImgFile(origFile);
					if (!uxfFile.exists()) {
						continue;
					}

					// update references to png
					mgr.add(new UpdateImgReferencesProcessor(javaProject) {

						@Override
						protected IFile calculateImgDestination(IFile img, ICompilationUnit referencingCompilationUnit) {
							if (origFile.equals(img)) {
								return destinationFolder.getFile(img.getName());
							}
							return null;
						}
					});

					// move uxfFile
					mgr.add(new MoveResourceChange(uxfFile, destinationFolder));
				}
				continue;
			}

			if (origResource instanceof IFolder) {
				final IFolder origFolder = (IFolder) origResource;
				final IFolder newFolder = destinationFolder.getFolder(origFolder.getName());
				final IPath origFolderPath = origFolder.getFullPath();
				mgr.add(new UpdateImgReferencesProcessor(javaProject) {

					@Override
					protected IFile calculateImgDestination(IFile img, ICompilationUnit referencingCompilationUnit) {
						IPath imgPath = img.getFullPath();
						if (origFolderPath.isPrefixOf(imgPath)) {
							IPath relativePath = imgPath.makeRelativeTo(origFolderPath);
							return newFolder.getFile(relativePath);
						}
						return null;
					}
				});
				continue;
			}
		}
		return mgr.createChange(pm);
	}

	@Override
	public void addElement(Object element, RefactoringArguments arguments) {
		entries.add(new Entry(element, (MoveArguments) arguments));
	}

}
