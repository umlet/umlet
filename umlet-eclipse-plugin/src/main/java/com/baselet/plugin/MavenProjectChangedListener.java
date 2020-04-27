package com.baselet.plugin;

import org.apache.maven.project.MavenProject;
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

	private static String configPropertyValue(MavenProject project) {
		if (project == null) {
			return null;
		}

		String propertyValue = project.getProperties().getProperty("com.umlet.nature.enabled");
		return propertyValue;
	}

	@Override
	public void mavenProjectChanged(MavenProjectChangedEvent[] events, IProgressMonitor pm) {
		for (MavenProjectChangedEvent event : events) {
			try {
				IProject project = event.getMavenProject().getProject();

				String propertyValue = configPropertyValue(event.getMavenProject().getMavenProject());
				if ("true".equalsIgnoreCase(propertyValue)) {
					AddRemoveUmletNatureHandler.addUmletNature(project);
				}
				else if ("false".equalsIgnoreCase(propertyValue)) {
					AddRemoveUmletNatureHandler.removeUmletNature(project);
				}
			} catch (CoreException e) {
				MainPlugin.logError("Error while updating maven project", e);
			}
		}
	}

}
