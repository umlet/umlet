package com.baselet.plugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.IMavenProjectChangedListener;
import org.eclipse.m2e.core.project.MavenProjectChangedEvent;

import com.baselet.plugin.builder.AddRemoveUmletNatureHandler;

/**
 * Listener to add and remove the umlet nature when the maven project changes
 */
public class MavenProjectChangedListener implements IMavenProjectChangedListener {

	@Override
	public void mavenProjectChanged(MavenProjectChangedEvent[] events, IProgressMonitor pm) {
		for (MavenProjectChangedEvent event : events) {
			try {
				IProject project = event.getMavenProject().getProject();

				if (event.getKind() == MavenProjectChangedEvent.KIND_REMOVED) {
					if (ProjectConfigurator.DO_REMOVE_NATURE) {
						AddRemoveUmletNatureHandler.removeUmletNature(project);
					}
				}
				else {
					if (ProjectConfigurator.hasUmletMavenPlugin(event.getMavenProject())) {
						AddRemoveUmletNatureHandler.addUmletNature(project);
					}
					else {
						if (ProjectConfigurator.DO_REMOVE_NATURE) {
							AddRemoveUmletNatureHandler.removeUmletNature(project);
						}
					}
				}
			} catch (CoreException e) {
				MainPlugin.logError("Error while updating maven project", e);
			}
		}
	}

}
