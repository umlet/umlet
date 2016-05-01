package com.baselet.plugin;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;

import com.baselet.plugin.builder.AddRemoveUmletNatureHandler;

/**
 * Add/remove the umlet nature based on the presence of the umlet plugin
 */
public class ProjectConfigurator extends AbstractProjectConfigurator {

	static final boolean DO_REMOVE_NATURE = false;
	private static final String MAVEN_PLUGIN_GROUP = "com.umlet";
	private static final String MAVEN_PLUGIN_ARTIFACT = "umlet-maven-plugin";

	@Override
	public void configure(ProjectConfigurationRequest request, IProgressMonitor pm) throws CoreException {
		if (ProjectConfigurator.hasUmletMavenPlugin(request.getMavenProject())) {
			AddRemoveUmletNatureHandler.addUmletNature(request.getProject());
		}
		else {
			if (DO_REMOVE_NATURE) {
				AddRemoveUmletNatureHandler.removeUmletNature(request.getProject());
			}
		}
	}

	public static boolean hasUmletMavenPlugin(IMavenProjectFacade facade) {
		return hasUmletMavenPlugin(facade.getMavenProject());
	}

	public static boolean hasUmletMavenPlugin(MavenProject project) {
		if (project == null) {
			return false;
		}

		// includes plugins inherited from parent
		for (Plugin plugin : project.getBuild().getPlugins()) {
			if (MAVEN_PLUGIN_GROUP.equals(plugin.getGroupId()) && MAVEN_PLUGIN_ARTIFACT.equals(plugin.getArtifactId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Required since {@link MavenProjectChangedListener} is not notified on removal of the maven nature
	 */
	@Override
	public void unconfigure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException {
		if (DO_REMOVE_NATURE) {
			AddRemoveUmletNatureHandler.removeUmletNature(request.getProject());
		}
	}
}
