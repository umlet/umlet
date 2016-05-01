package com.umlet.maven;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.ProgressMonitor;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.Scanner;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.io.OutputHandler;

/**
 * Export all .uxf files to .png images in the same directory
 */
@Mojo(name = "export", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ExportDiagramsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}", required = true, readonly = true)
    private File testSourceDirectory;

    /**
     * When set to true, this mojo is skipped
     */
    @Parameter(defaultValue = "false", required = false, readonly = false)
    private boolean skip;

    @Component
    private BuildContext buildContext;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip)
            return;
        getLog().info("Exporting diagrams ...");

        if (!Program.isInitialized()) {
            Program.init(Utils.readBuildInfo().version, RuntimeType.STANDALONE);
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ArrayList<ExportTask> tasks = new ArrayList<ExportDiagramsMojo.ExportTask>();

        queueTasks(sourceDirectory, executor, tasks);
        queueTasks(testSourceDirectory, executor, tasks);

        for (ExportTask task : tasks) {
            task.awaitFinish();
        }
        getLog().info(tasks.size() + " Diagram(s) exported");
    }

    private void queueTasks(File modelDir, ExecutorService executor, ArrayList<ExportTask> tasks) {
        if (modelDir == null)
            return;
        Scanner scanner = buildContext.newScanner(modelDir);
        scanner.setIncludes(new String[] { "**/*.uxf" });
        scanner.scan();
        String[] includedFiles = scanner.getIncludedFiles();
        if (includedFiles != null) {
            for (String includedFile : includedFiles) {
                File modelFile = new File(scanner.getBasedir(), includedFile);
                getLog().debug("exporting " + modelFile.getAbsolutePath());
                tasks.add(new ExportTask(modelFile, executor));
            }
        }
    }

    /**
     * Encapsulates the state necessary to export a single file. Handles the
     * {@link ProgressMonitor}
     */
    private class ExportTask {
        private final Future<byte[]> future;
        private File uxfFile;

        public ExportTask(final File uxfFile, ExecutorService exec) {
            this.uxfFile = uxfFile;

            future = exec.submit(new Callable<byte[]>() {

                @Override
                public byte[] call() throws Exception {

                    try {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        DiagramHandler handler = new DiagramHandler(uxfFile);
                        OutputHandler.createToStream("png", os, handler);
                        os.close();
                        return os.toByteArray();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            });
        }

        /**
         * Wait until the export has finished and create/remove markers
         */
        void awaitFinish() {
            try {
                File outFile = new File(FilenameUtils.removeExtension(uxfFile.getAbsolutePath()) + ".png");
                // wait for task to complete
                byte[] bb = future.get();

                // write result
                if (bb != null) {
                    OutputStream os = null;
                    try {
                        os = buildContext.newFileOutputStream(outFile);
                        os.write(bb);
                    } finally {
                        if (os != null)
                            os.close();
                    }
                }

            } catch (Exception e) {
                getLog().error("Error while transforming image " + uxfFile.getAbsolutePath(), e);
            }
        }
    }
}
